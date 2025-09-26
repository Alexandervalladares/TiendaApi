package com.fakestore.tienda.data

import com.fakestore.tienda.model.Product
import com.fakestore.tienda.network.FakeStoreApi

class ProductRepository(private val api: FakeStoreApi) {

    suspend fun fetchProducts(): List<Product> = api.getProducts()
}