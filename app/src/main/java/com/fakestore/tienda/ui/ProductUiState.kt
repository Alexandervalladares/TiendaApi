package com.fakestore.tienda.ui

import com.fakestore.tienda.model.Product

data class ProductUiState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val errorMessage: String? = null
)