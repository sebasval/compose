package com.mercadolibre.product.domain.usecase.getcars

import com.mercadolibre.data.common.Resource
import com.mercadolibre.data.model.ProductItem
import com.mercadolibre.data.repository.ProductRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetProductItemUseCase @Inject constructor(
    private val repository: ProductRepository,
    private val defaultDispatcher: CoroutineDispatcher
) {

    private var productItem = listOf<ProductItem>()

    suspend operator fun invoke(): Flow<Resource<List<ProductItem>>> =
        flow<Resource<List<ProductItem>>> {
            try {
                emit(Resource.loading())
                productItem = repository.getProducts().results
                emit(Resource.success(productItem))
            } catch (e: Throwable) {
                emit(Resource.error(e))
            }
        }.flowOn(defaultDispatcher)

    suspend operator fun invoke(
        query: String
    ): Flow<List<ProductItem>> = flow {
        filter(flowOf(productItem), query).collect { emit(it) }
    }

    private fun filter(
        response: Flow<List<ProductItem>>,
        query: String
    ): Flow<List<ProductItem>> {
        return response.map { it.filter { productItem -> productItem.title.contains(query) } }
            .flowOn(defaultDispatcher)
    }
}

