package com.davidcuruvija.svemogucstvo.viewmodel.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import com.davidcuruvija.svemogucstvo.model.cart.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import com.davidcuruvija.svemogucstvo.repo.CartRepository
import com.davidcuruvija.svemogucstvo.model.store.StoreCartItemDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val cartRepository : CartRepository) : ViewModel() {
    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items : StateFlow<List<CartItem>> = _items.asStateFlow()

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
        val newQuantity = item.quantity + 1
        val currentItems = _items.value

        _items.value = currentItems.map {
            if (it.key == key) it.copy(quantity = newQuantity) else it
        }

        viewModelScope.launch {
            try {
                val cart = cartRepository.updateItemQuantity(key, newQuantity)
                _items.value = cart.items.map { it.toCartItem() }
            } catch (e : Exception) {
                _items.value = currentItems
            }
        }
    }

    fun decreaseQuantity(key : String) {
        val item = _items.value.find { it.key == key } ?: return
        if (item.quantity <= 1) {
            removeItem(key)
            return
        }

        val newQuantity = item.quantity - 1
        val currentItems = _items.value

        _items.value = currentItems.map {
            if (it.key == key) it.copy(quantity = newQuantity) else it
        }

        viewModelScope.launch {
            try {
                val cart = cartRepository.updateItemQuantity(key, newQuantity)
                _items.value = cart.items.map { it.toCartItem() }
            } catch (e : Exception) {
                _items.value = currentItems
            }
        }
    }

    fun removeItem(key : String) {
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