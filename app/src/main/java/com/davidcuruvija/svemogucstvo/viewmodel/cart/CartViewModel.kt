package com.davidcuruvija.svemogucstvo.viewmodel.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import com.davidcuruvija.svemogucstvo.model.cart.CartItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import com.davidcuruvija.svemogucstvo.repo.CartRepository
import com.davidcuruvija.svemogucstvo.model.store.StoreAddressDto
import com.davidcuruvija.svemogucstvo.model.store.StoreCartItemDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

sealed interface CheckoutState {
    data object Idle : CheckoutState
    data object Loading : CheckoutState
    data class Success(val orderId : Int) : CheckoutState
    data class Error(val message : String) : CheckoutState
}

@HiltViewModel
class CartViewModel @Inject constructor(private val cartRepository : CartRepository) : ViewModel() {
    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items : StateFlow<List<CartItem>> = _items.asStateFlow()

    private val _checkoutState = MutableStateFlow<CheckoutState>(CheckoutState.Idle)
    val checkoutState : StateFlow<CheckoutState> = _checkoutState.asStateFlow()

    private val quantityUpdateJobs = mutableMapOf<String, Job>()
    private val confirmedItemsBeforeUpdate = mutableMapOf<String, List<CartItem>>()

    private val _errorEvents = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val errorEvents : SharedFlow<String> = _errorEvents.asSharedFlow()

    init {
        loadCart()
    }

    private fun loadCart() {
        viewModelScope.launch {
            try {
                _items.value = cartRepository.getCartItems()
            } catch (e: Exception) {
                _items.value = emptyList()
            }
        }
    }

    val itemCount : StateFlow<Int> = _items
        .map { items ->
            items.sumOf { it.quantity }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    val total : StateFlow<Long> = _items
        .map { items ->
            items.sumOf { item ->
                (item.price.toLongOrNull() ?: 0L) * item.quantity
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0L
        )

    fun addItem(item : CartItem) {
        val currentItems = _items.value
        val existingItem = currentItems.find { it.variationId == item.variationId }

        if (existingItem != null) {
            _items.value = currentItems.map {
                if (it.variationId == item.variationId) {
                    it.copy(quantity = it.quantity + item.quantity)
                } else {
                    it
                }
            }
        } else {
            _items.value = currentItems + item
        }

        viewModelScope.launch {
            try {
                val cart = cartRepository.addItem(
                    variationId = item.variationId,
                    quantity = item.quantity
                )
                _items.value = cart.items.map { it.toCartItem() }
            } catch (e : Exception) {
                _items.value = currentItems
                Log.e(
                    "CartViewModel",
                    "Failed to add item to cart",
                    e
                )
            }
        }
    }

    fun increaseQuantity(key : String) {
        val item = _items.value.find { it.key == key } ?: return
        updateQuantityDebounced(key, item.quantity + 1)
    }

    fun decreaseQuantity(key : String) {
        val item = _items.value.find { it.key == key } ?: return
        if (item.quantity <= 1) {
            removeItem(key)
            return
        }

        updateQuantityDebounced(key, item.quantity - 1)
    }

    // Rapid taps race independent network calls that can resolve out of order and
    // clobber a newer quantity with a stale one, so pending updates for the same
    // item are cancelled and coalesced into a single request once taps settle.
    private fun updateQuantityDebounced(key : String, newQuantity : Int) {
        if (quantityUpdateJobs[key] == null) {
            confirmedItemsBeforeUpdate[key] = _items.value
        }

        _items.value = _items.value.map {
            if (it.key == key) it.copy(quantity = newQuantity) else it
        }

        quantityUpdateJobs[key]?.cancel()
        quantityUpdateJobs[key] = viewModelScope.launch {
            delay(400.milliseconds)

            val latestQuantity = _items.value.find { it.key == key }?.quantity
            val fallback = confirmedItemsBeforeUpdate.remove(key) ?: _items.value
            quantityUpdateJobs.remove(key)

            if (latestQuantity == null) return@launch

            try {
                val cart = cartRepository.updateItemQuantity(key, latestQuantity)
                _items.value = cart.items.map { it.toCartItem() }
            } catch (e : Exception) {
                Log.e(
                    "CartViewModel",
                    "Failed to update quantity for $key to $latestQuantity",
                    e
                )
                _items.value = fallback
                _errorEvents.tryEmit(e.message ?: "Unable to update quantity")
            }
        }
    }

    fun removeItem(key : String) {
        quantityUpdateJobs.remove(key)?.cancel()
        confirmedItemsBeforeUpdate.remove(key)

        val currentItems = _items.value
        _items.value = currentItems.filter { it.key != key }

        viewModelScope.launch {
            try {
                val cart = cartRepository.removeItem(key)
                _items.value = cart.items.map { it.toCartItem() }
            } catch (e : Exception) {
                _items.value = currentItems
            }
        }
    }

    fun placeOrder(
        email : String,
        firstName : String,
        lastName : String,
        address : String,
        apartment : String,
        city : String,
        postalCode : String,
        phone : String,
        customerNote : String
    ) {
        _checkoutState.value = CheckoutState.Loading

        val shippingAddress = StoreAddressDto(
            first_name = firstName,
            last_name = lastName,
            address_1 = address,
            address_2 = apartment,
            city = city,
            postcode = postalCode,
            country = "RS",
            phone = phone.takeIf { it.isNotBlank() }
        )

        val billingAddress = shippingAddress.copy(
            email = email,
            phone = phone.takeIf { it.isNotBlank() }
        )

        viewModelScope.launch {
            try {
                val response = cartRepository.checkout(
                    billingAddress = billingAddress,
                    shippingAddress = shippingAddress,
                    paymentMethod = "cod",
                    customerNote = customerNote
                )
                _items.value = emptyList()
                _checkoutState.value = CheckoutState.Success(response.order_id)
            } catch (e : Exception) {
                Log.e(
                    "CartViewModel",
                    "Failed to place order",
                    e
                )
                _checkoutState.value = CheckoutState.Error(
                    e.message ?: "Failed to place order"
                )
            }
        }
    }

    fun resetCheckoutState() {
        _checkoutState.value = CheckoutState.Idle
    }

    private fun StoreCartItemDto.toCartItem() : CartItem {
        val color = this.variation
            .firstOrNull { it.attribute.equals("color", ignoreCase = true) }
            ?.value
            ?.let { android.text.Html.fromHtml(it, android.text.Html.FROM_HTML_MODE_LEGACY).toString() }
            ?.trim()?.trim('"', '“', '”')

        val size = this.variation
            .firstOrNull { it.attribute.equals("size", ignoreCase = true) }
            ?.value
            ?.let { android.text.Html.fromHtml(it, android.text.Html.FROM_HTML_MODE_LEGACY).toString() }
            ?.trim()?.trim('"', '“', '”')

        return CartItem(
            key = this.key,
            variationId = this.id,
            productId = 0,
            productName = this.name,
            imageUrl = this.images.firstOrNull()?.src,
            color = color,
            size = size,
            price = this.prices.price,
            quantity = this.quantity
        )
    }
}