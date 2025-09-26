package com.fakestore.tienda.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakestore.tienda.data.ProductRepository
import com.fakestore.tienda.network.FakeStoreService
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ProductViewModel : ViewModel() {

    private val repository = ProductRepository(FakeStoreService.api)

    private val _uiState = MutableLiveData(ProductUiState(isLoading = true))
    val uiState: LiveData<ProductUiState> = _uiState

    init {
        fetchProducts()
    }

    fun refreshProducts() {
        fetchProducts()
    }

    private fun fetchProducts() {
        val currentState = _uiState.value ?: ProductUiState()
        _uiState.value = currentState.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {
                val products = repository.fetchProducts()
                _uiState.postValue(
                    currentState.copy(
                        isLoading = false,
                        products = products,
                        errorMessage = null
                    )
                )
            } catch (exception: Exception) {
                val message = when (exception) {
                    is IOException -> "Error de conexión. Verifica tu acceso a internet."
                    is HttpException -> "Error en el servidor. Código ${exception.code()}"
                    else -> "Error inesperado al cargar los productos."
                }
                _uiState.postValue(
                    currentState.copy(
                        isLoading = false,
                        errorMessage = message
                    )
                )
            }
        }
    }
}