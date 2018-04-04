package ru.devsp.app.mtgcollections.model.api;

import android.support.annotation.Nullable;

import java.io.IOException;

import retrofit2.Response;
import ru.devsp.app.mtgcollections.tools.Logger;

/**
 *
 * Created by gen on 01.09.2017.
 */

public class ApiResponse<T> {

    public final int code;

    @Nullable
    public final T body;

    @Nullable
    public final String errorMessage;

    public ApiResponse(Throwable error) {
        code = 500;
        body = null;
        errorMessage = error.getMessage();
    }

    public ApiResponse(Response<T> response) {
        code = response.code();
        if(response.isSuccessful()) {
            body = response.body();
            errorMessage = null;
        } else {
            String message = null;
            if (response.errorBody() != null) {
                try {
                    message = response.errorBody().string();
                } catch (IOException ignored) {
                    Logger.INSTANCE.e(ignored);
                }
            }
            if (message == null || message.trim().length() == 0) {
                message = response.message();
            }
            errorMessage = message;
            body = null;
        }
    }
}
