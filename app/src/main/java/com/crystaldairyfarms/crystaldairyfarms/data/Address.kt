package com.crystaldairyfarms.crystaldairyfarms.data

data class Address(
    val id: String,
    val houseNo: String,
    val area: String,
    val city: String,
    val pincode: String,
    val isDefault: Boolean = false
) {
    val displayText: String get() = "$houseNo, $area, $city - $pincode"
}