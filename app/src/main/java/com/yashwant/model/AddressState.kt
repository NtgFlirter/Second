package com.yashwant.model

data class AddressState(
    val zipCode: String = "",
    val country: String = "",
    val state: String = "",
    val city: String = "",
    val street: String = "",
    val addressType: String = "Home" // Default
)