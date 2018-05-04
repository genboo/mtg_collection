package ru.devsp.app.mtgcollections.model.api

import android.arch.lifecycle.LiveData

import retrofit2.http.GET
import ru.devsp.app.mtgcollections.model.tools.ApiResponse
import ru.devsp.app.mtgcollections.model.objects.Set

/**
 *
 * Created by gen on 02.10.2017.
 */

interface SetsApi {

    @get:GET("/v1/sets")
    val sets: LiveData<ApiResponse<List<Set>>>

}