package ru.devsp.app.mtgcollections.model.api

import android.arch.lifecycle.LiveData

import retrofit2.http.GET
import retrofit2.http.Query
import ru.devsp.app.mtgcollections.model.tools.ApiResponse
import ru.devsp.app.mtgcollections.model.objects.Card

/**
 *
 * Created by gen on 02.10.2017.
 */

interface CardApi {

    @GET("/v1/cards")
    fun getCard(@Query("set") set: String, @Query("number") number: String): LiveData<ApiResponse<List<Card>>>

    @GET("/v1/cards")
    fun getCardByName(@Query("set") set: String, @Query("name") name: String): LiveData<ApiResponse<List<Card>>>

    @GET("/v1/cards")
    fun getCardsBySet(@Query("set") set: String, @Query("page") page: Int, @Query("pageSize") pages: Int): LiveData<ApiResponse<List<Card>>>

    @GET("/v1/cards")
    fun getCard(@Query("multiverseid") set: String): LiveData<ApiResponse<List<Card>>>

}