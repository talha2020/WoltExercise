package com.example.woltexercise.api

import com.example.woltexercise.BuildConfig
import com.example.woltexercise.data.ApiError
import com.example.woltexercise.data.ApiResponse
import com.example.woltexercise.data.Venues
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import okhttp3.logging.HttpLoggingInterceptor

class ApiClient {

    private val client = HttpClient(OkHttp) {
        engine {
            if (BuildConfig.DEBUG){
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY

                addInterceptor(logging)
            }
        }
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
        expectSuccess = true
    }

    suspend fun getVenues(
        lat: Double,
        lng: Double
    ): ApiResponse<Venues>{
        return try {
            val url = ApiEndPoints.GET_VENUE
            val res = client.get<Venues>(url) {
                parameter("lat", lat)
                parameter("lon", lng)
            }
            // Not handling all the error cases here for the purpose of this assignment.
            ApiResponse(data = res)
        } catch (ex: Exception) {
            ex.printStackTrace()
            ApiResponse(error = ApiError(message = ex.message))
        }
    }
}