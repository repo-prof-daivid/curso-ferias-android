package com.example.listadecompras

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.listadecompras.databinding.ItemListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PurchaseListItemViewHolder(
    private val itemListBinding: ItemListBinding
) : RecyclerView.ViewHolder(itemListBinding.root) {

    fun bind(
        purchaseListItem: PurchaseListItem,
        position: Int,
        onDelete: (position: Int) -> Unit
    ) {
        itemListBinding.txtName.text = purchaseListItem.name
        itemListBinding.txtQtd.text = purchaseListItem.qtd
        itemListBinding.txtBrand.text = purchaseListItem.band
        itemListBinding.txtMeasurementUnit.text = purchaseListItem.measurementUnit
        itemListBinding.cbPurchased.isChecked = purchaseListItem.purchased

        itemListBinding.btnDelete.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("USERS").document(auth.uid.toString()).collection("PURCHASE_LIST")
                .document(purchaseListItem.id).delete()
                .addOnSuccessListener {
                    onDelete.invoke(position)
                }
                .addOnFailureListener {
                    Toast.makeText(
                        itemListBinding.root.context,
                        "Não foi possível deletar esse item!",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
        itemListBinding.cbPurchased.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isShown) {
                val auth = FirebaseAuth.getInstance()
                val firestore = FirebaseFirestore.getInstance()
                purchaseListItem.purchased = isChecked
                firestore.collection("USERS").document(auth.uid.toString()).collection("PURCHASE_LIST")
                    .document(purchaseListItem.id).set(purchaseListItem)
                    .addOnSuccessListener {
                        Toast.makeText(
                            itemListBinding.root.context,
                            "Item atualizado com sucesso!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    .addOnFailureListener { purchaseListItem.purchased = false }
            }
        }
    }

}