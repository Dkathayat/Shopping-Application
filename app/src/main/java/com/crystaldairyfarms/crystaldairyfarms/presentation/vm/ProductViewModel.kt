package com.crystaldairyfarms.crystaldairyfarms.presentation.vm

import androidx.lifecycle.ViewModel
import com.crystaldairyfarms.crystaldairyfarms.data.FirebaseProduct
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class ProductState {
    object Loading : ProductState()
    data class Success(val products: List<FirebaseProduct>) : ProductState()
    data class Error(val message: String) : ProductState()
}

// Stable IDs so the cart always maps to the same item across sessions.
val localFallbackProducts = listOf(
    FirebaseProduct("local_1",  "Liquid Milk",        "Crystal Dairy", "1 litre",  55.0,  "🥛", "Dairy"),
    FirebaseProduct("local_2",  "Pure Ghee",          "Crystal Dairy", "500 gm",  280.0,  "🫙", "Dairy"),
    FirebaseProduct("local_3",  "Fresh Paneer",       "Crystal Dairy", "200 gm",   90.0,  "🧀", "Dairy"),
    FirebaseProduct("local_4",  "Curd (Dahi)",        "Crystal Dairy", "400 gm",   45.0,  "🥣", "Dairy"),
    FirebaseProduct("local_5",  "Butter",             "Crystal Dairy", "100 gm",   55.0,  "🧈", "Dairy"),
    FirebaseProduct("local_6",  "Buttermilk (Chaas)", "Crystal Dairy", "500 ml",   30.0,  "🥤", "Dairy"),
    FirebaseProduct("local_7",  "Milk Powder",        "Crystal Dairy", "200 gm",   95.0,  "🍼", "Dairy"),
    FirebaseProduct("local_8",  "Cheese Slice",       "Crystal Dairy", "200 gm",  120.0,  "🧇", "Dairy"),
    FirebaseProduct("local_9",  "Yogurt",             "Crystal Dairy", "200 gm",   40.0,  "🍶", "Dairy"),
    FirebaseProduct("local_10", "Khoa (Mawa)",        "Crystal Dairy", "250 gm",  140.0,  "🍮", "Dairy"),
)

@HiltViewModel
class ProductViewModel @Inject constructor() : ViewModel() {

    // Start with local items immediately — no loading spinner, user always sees content.
    private val _state = MutableStateFlow<ProductState>(ProductState.Success(localFallbackProducts))
    val state: StateFlow<ProductState> = _state.asStateFlow()

    init {
        connectToFirebase()
    }

    private fun connectToFirebase() {
        // Wrapped in try-catch: getInstance() throws DatabaseException when the
        // Realtime Database hasn't been created in the Firebase console yet.
        try {
            val ref = FirebaseDatabase.getInstance().getReference("products")
            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = snapshot.children.mapNotNull { child ->
                        child.getValue(FirebaseProduct::class.java)?.copy(id = child.key ?: "")
                    }
                    // Only replace fallback when Firebase actually has products.
                    if (list.isNotEmpty()) {
                        _state.value = ProductState.Success(list)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Network/permission error — fallback items already visible, nothing to do.
                }
            })
        } catch (_: Exception) {
            // Firebase not configured or SDK init failed — fallback already showing.
        }
    }
}