package ru.devsp.app.mtgcollections.model.api.tools;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Вытаскивает ответ из тэга result
 * Created by gen on 01.09.2017.
 */

public class ResultTypeAdapterFactory implements TypeAdapterFactory {

    private static final String RESULT_TAG_CARD = "cards";
    private static final String RESULT_TAG_SETS = "sets";

    public <T> TypeAdapter<T> create(Gson gson, final TypeToken<T> type) {

        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);

        return new TypeAdapter<T>() {

            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            public T read(JsonReader in) throws IOException {
                JsonElement jsonElement = elementAdapter.read(in);
                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    if (jsonObject.has(RESULT_TAG_CARD)) {
                        jsonElement = jsonObject.get(RESULT_TAG_CARD);
                    } else if (jsonObject.has(RESULT_TAG_SETS)) {
                        jsonElement = jsonObject.get(RESULT_TAG_SETS);
                    }
                }

                return delegate.fromJsonTree(jsonElement);
            }
        }.nullSafe();
    }
}
