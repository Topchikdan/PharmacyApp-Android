package com.example.exam

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

data class Service(val id: Int, val name: String, val price: Int, val days: Int)
data class CartItem(val service: Service, var qty: Int = 1)

class ShopVM : ViewModel() {
    private val _services = MutableLiveData(
        listOf(
            Service(1, "ПЦР-тест на РНК коронавируса", 1800, 2),
            Service(2, "Клинический анализ крови", 690, 1),
            Service(3, "Биохимический анализ крови (базовый)", 2440, 1)
        )
    )
    val services: LiveData<List<Service>> = _services

    private val _cart = MutableLiveData(mutableListOf<CartItem>())
    val cart: LiveData<List<CartItem>>   = _cart.map { it.toList() }
    val total: LiveData<Int>             = _cart.map { l -> l.sumOf { it.service.price * it.qty } }

    fun add(s: Service)  { _cart.value!!.add(CartItem(s)); _cart.value = _cart.value }
    fun inc(i: CartItem) { i.qty++; _cart.value = _cart.value }
    fun dec(i: CartItem) { if (i.qty > 1) i.qty--; _cart.value = _cart.value }
    fun rem(i: CartItem) { _cart.value!!.remove(i); _cart.value = _cart.value }
}
