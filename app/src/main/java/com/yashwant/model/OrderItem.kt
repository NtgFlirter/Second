package com.yashwant.model

data class OrderItem(
    val orderId: String = "",
    val userId: String = "",
    val items: List<CartItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val orderTime: Long = 0L,
    val expectedDeliveryTime: Long = 0L, // Time in milliseconds when it will arrive
    val status: String = "Pending"
)