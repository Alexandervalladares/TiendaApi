package com.fakestore.tienda

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.fakestore.tienda.databinding.ActivityMainBinding
import com.fakestore.tienda.ui.ProductViewModel
import com.fakestore.tienda.ui.ProductsAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflar el layout con ViewBinding UNA sola vez
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Aplicar paddings para barras del sistema y RETORNAR insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets  // ← importante: retornar los insets
        }

        // RecyclerView
        val productsAdapter = ProductsAdapter()
        binding.recyclerProducts.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = productsAdapter
        }

        // Swipe-to-refresh
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshProducts()
        }

        // Observa el estado de UI
        viewModel.uiState.observe(this) { state ->
            binding.progressBar.isVisible = state.isLoading && state.products.isEmpty()
            binding.swipeRefresh.isRefreshing = state.isLoading && state.products.isNotEmpty()
            binding.recyclerProducts.isVisible = state.products.isNotEmpty()
            productsAdapter.submitList(state.products)

            when {
                state.isLoading -> {
                    binding.textStatus.isVisible = true
                    binding.textStatus.text = getString(R.string.loading_products)
                }
                state.errorMessage != null -> {
                    binding.textStatus.isVisible = true
                    binding.textStatus.text = state.errorMessage
                }
                state.products.isEmpty() -> {
                    binding.textStatus.isVisible = true
                    binding.textStatus.text = getString(R.string.no_products_available)
                }
                else -> binding.textStatus.isVisible = false
            }

            if (!state.isLoading) binding.swipeRefresh.isRefreshing = false
        }
    }
}
