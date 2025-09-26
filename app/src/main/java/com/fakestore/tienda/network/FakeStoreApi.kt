package com.fakestore.tienda.network

import com.fakestore.tienda.model.Product
import retrofit2.http.GET

interface FakeStoreApi {
    @GET("products")
    suspend fun getProducts(): List<Product>
}