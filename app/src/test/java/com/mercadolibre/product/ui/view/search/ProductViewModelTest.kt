package com.mercadolibre.product.ui.view.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mercadolibre.product.MainDispatcherRule
import com.mercadolibre.product.domain.usecase.getcars.GetProductItemUseCase
import com.mercadolibre.data.api.ApiService
import com.mercadolibre.data.model.MercadoLibreResponse
import com.mercadolibre.data.model.ProductItem
import com.mercadolibre.data.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*
import java.io.IOException

/**
 * Unit tests for the [ProductViewModel].
 */
@ExperimentalCoroutinesApi
class ProductViewModelTest {

    @Mock
    lateinit var apiService: ApiService

    @Mock
    lateinit var repository: ProductRepository

    @Mock
    lateinit var getProductItemUseCase: GetProductItemUseCase

    @Mock
    lateinit var viewModel: ProductViewModel

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Overrides Dispatchers.Main used in Coroutines
    @get:Rule
    var coroutineRule = MainDispatcherRule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = ProductRepository(apiService)
        getProductItemUseCase = GetProductItemUseCase(repository, coroutineRule.testDispatcher)
        viewModel = ProductViewModel(getProductItemUseCase)
    }

    @Test
    fun getProduct_isSuccess() = runTest {
        val mockRepository = mock<ProductRepository>()
        val mockProductList = listOf(
            ProductItem("Producto 1", "new", "http://url-to-thumbnail1.com", 10000),
            ProductItem("Producto 2", "used", "http://url-to-thumbnail2.com", 20000)
        )

        val mockResponse = MercadoLibreResponse(mockProductList)
        whenever(mockRepository.getProducts(any())).thenReturn(mockResponse)

        val getProductItemUseCase = GetProductItemUseCase(mockRepository, Dispatchers.Unconfined)
        val viewModel = ProductViewModel(getProductItemUseCase)

        viewModel.searchProducts("query de ejemplo")


        advanceUntilIdle()
        val currentState = viewModel.state.value
        assertTrue(currentState is ProductListUiStateReady && currentState.productList == mockProductList)
    }


    @Test
    fun getProducts_isFail() = runTest {
        whenever(repository.getProducts(any())) doAnswer {
            throw IOException()
        }
        viewModel.getProducts()
        getProductItemUseCase().catch {
            assertEquals(ProductListUiStateError(), viewModel.state.value)
        }
    }

}