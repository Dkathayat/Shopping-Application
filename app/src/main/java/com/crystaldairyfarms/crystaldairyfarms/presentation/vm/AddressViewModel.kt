package com.crystaldairyfarms.crystaldairyfarms.presentation.vm

import androidx.lifecycle.ViewModel
import com.crystaldairyfarms.crystaldairyfarms.data.Address
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class AddressViewModel @Inject constructor() : ViewModel() {

    private val _addresses = MutableStateFlow<List<Address>>(emptyList())
    val addresses: StateFlow<List<Address>> = _addresses.asStateFlow()

    fun addAddress(houseNo: String, area: String, city: String, pincode: String, phone: String = "") {
        if (_addresses.value.size >= 3) return
        val isFirst = _addresses.value.isEmpty()
        _addresses.update {
            it + Address(
                id = System.currentTimeMillis().toString(),
                houseNo = houseNo.trim(),
                area = area.trim(),
                city = city.trim(),
                pincode = pincode.trim(),
                phone = phone.trim(),
                isDefault = isFirst
            )
        }
    }

    fun updateAddress(updated: Address) {
        _addresses.update { list -> list.map { if (it.id == updated.id) updated else it } }
    }

    fun deleteAddress(id: String) {
        val current = _addresses.value
        val deleted = current.find { it.id == id } ?: return
        var remaining = current.filter { it.id != id }
        if (deleted.isDefault && remaining.isNotEmpty()) {
            remaining = remaining.mapIndexed { i, a -> if (i == 0) a.copy(isDefault = true) else a }
        }
        _addresses.value = remaining
    }

    fun setDefault(id: String) {
        _addresses.update { list -> list.map { it.copy(isDefault = it.id == id) } }
    }
}