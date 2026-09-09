package com.davidcuruvija.svemogucstvo.viewmodel.cart

import androidx.lifecycle.ViewModel
import com.davidcuruvija.svemogucstvo.model.cart.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope

class CartViewModel : ViewModel() {

    private val _items = MutableStateFlow<List<CartItem>>(emptyList())

    val items : StateFlow<List<CartItem>> = _items.asStateFlow()

    val itemCount : StateFlow<Int> = _items
        .map { items ->
            items.sumOf { it.quantity }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    val subtotal : StateFlow<Long> = _items
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

    val shipping : Long = 500L

    val total : StateFlow<Long> = subtotal
        .map { subtotal ->
            subtotal + shipping
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = shipping
        )

    fun addItem(item : CartItem) {
        val existingItem = _items.value.find {
            it.variationId == item.variationId
        }

        if (existingItem != null) {
            _items.value = _items.value.map {
                if (it.variationId == item.variationId) {
                    it.copy(
                        quantity = it.quantity + item.quantity
                    )
                } else {
                    it
                }
            }
        } else {
            _items.value += item
        }
    }

    fun increaseQuantity(variationId : Int) {
        _items.value = _items.value.map { item ->
            if (item.variationId == variationId) {
                item.copy(
                    quantity = item.quantity + 1
                )
            } else {
                item
            }
        }
    }

    fun decreaseQuantity(variationId : Int) {
        _items.value = _items.value.mapNotNull { item ->
            if (item.variationId == variationId) {
                if (item.quantity > 1) {
                    item.copy(
                        quantity = item.quantity - 1
                    )
                } else {
                    null
                }
            } else {
                item
            }
        }
    }

    fun removeItem(variationId : Int) {
        _items.value = _items.value.filter {
            it.variationId != variationId
        }
    }
}