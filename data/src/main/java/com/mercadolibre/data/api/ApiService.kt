package com.mercadolibre.data.api

import com.mercadolibre.data.model.MercadoLibreResponse
import retrofit2.http.*

interface ApiService {

    @GET("sites/MCO/search?q=celulares")
    suspend fun getProducts(): MercadoLibreResponse
}
