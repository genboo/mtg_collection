package ru.devsp.app.mtgcollections.model.api;

import android.arch.lifecycle.LiveData;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.devsp.app.mtgcollections.model.tools.ApiResponse;
import ru.devsp.app.mtgcollections.model.objects.Card;

/**
 *
 * Created by gen on 02.10.2017.
 */

public interface CardApi {

    @GET("/v1/cards")
    LiveData<ApiResponse<List<Card>>> getCard(@Query("set") String set, @Query("number") String number);

    @GET("/v1/cards")
    LiveData<ApiResponse<List<Card>>> getCardByName(@Query("set") String set, @Query("name") String name);

    @GET("/v1/cards")
    LiveData<ApiResponse<List<Card>>> getCardsBySet(@Query("set") String set, @Query("page") int page, @Query("pageSize") int pages);

    @GET("/v1/cards")
    LiveData<ApiResponse<List<Card>>> getCard(@Query("multiverseid") String set);

}
