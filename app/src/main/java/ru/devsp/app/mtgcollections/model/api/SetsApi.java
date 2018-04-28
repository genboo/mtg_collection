package ru.devsp.app.mtgcollections.model.api;

import android.arch.lifecycle.LiveData;

import java.util.List;

import retrofit2.http.GET;
import ru.devsp.app.mtgcollections.model.tools.ApiResponse;
import ru.devsp.app.mtgcollections.model.objects.Set;

/**
 *
 * Created by gen on 02.10.2017.
 */

public interface SetsApi {

    @GET("/v1/sets")
    LiveData<ApiResponse<List<Set>>> getSets();

}
