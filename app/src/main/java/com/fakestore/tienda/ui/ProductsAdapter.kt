package com.fakestore.tienda.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fakestore.tienda.R
import com.fakestore.tienda.databinding.ItemProductBinding
import com.fakestore.tienda.model.Product
import java.util.Locale

class ProductsAdapter : ListAdapter<Product, ProductsAdapter.ProductViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            val context = binding.root.context
            binding.textTitle.text = product.title
            binding.textCategory.text = product.category.uppercase(Locale.getDefault())
            binding.textPrice.text = context.getString(R.string.product_price, product.price)
            binding.textRatingValue.text = context.getString(R.string.product_rating_value, product.rating.rate)
            binding.textRatingCount.text = context.getString(R.string.product_rating_count, product.rating.count)
            binding.textDescription.text = product.description
            binding.imageProduct.load(product.image) {
                crossfade(true)
                placeholder(R.drawable.ic_image_placeholder)
                error(R.drawable.ic_image_placeholder)
            }
        }
    }

    private companion object DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem == newItem
    }
}