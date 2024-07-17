package com.example.listadecompras

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listadecompras.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var purchaseListItemAdapter: PurchaseListItemRecyclerViewAdapter
    private val purchaseListItems: ArrayList<PurchaseListItem> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("USERS").document(auth.uid.toString()).collection("PURCHASE_LIST")
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    Toast.makeText(
                        this@HomeActivity,
                        "Até o momento você ainda não tem itens la sua lista, começe o cadastro!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    result.forEach { item ->
                        val purchaseListItem = item.toObject<PurchaseListItem>()
                        purchaseListItem.id = item.id
                        purchaseListItems.add(purchaseListItem)
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this@HomeActivity,
                    "Infelizmente não foi possível carregar a lista!!! + ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
            .addOnCompleteListener {
                setUpRecyclerView()
            }
        setUpView()
    }

    private fun setUpView() {
        // todo validar campos
        // todo construir elemento da lista val item = PurchaseListItem(...)
        // todo adidionar no firebase
        // todo notificar a mudança da lista para que seja refletido no layout
        // todo "firestore.collection("USERS").document(auth.uid.toString()).collection("PURCHASE_LIST").document().set(item)"
    }

    private fun setUpRecyclerView() {
        purchaseListItemAdapter = PurchaseListItemRecyclerViewAdapter(
            items = purchaseListItems,
            onDelete = { position ->
                purchaseListItems.remove(purchaseListItems[position])
                purchaseListItemAdapter.notifyItemRemoved(position)
            }
        )
        binding.rvListShopping.layoutManager = LinearLayoutManager(this@HomeActivity)
        binding.rvListShopping.adapter = purchaseListItemAdapter
    }
}