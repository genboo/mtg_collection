package ru.devsp.app.mtgcollections.di.modules


import com.google.gson.GsonBuilder
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.devsp.app.mtgcollections.BuildConfig
import ru.devsp.app.mtgcollections.model.api.CardApi
import ru.devsp.app.mtgcollections.model.api.SetsApi
import ru.devsp.app.mtgcollections.model.tools.LiveDataCallAdapterFactory
import ru.devsp.app.mtgcollections.model.tools.ResultTypeAdapterFactory

/**
 * Инициализация Retrofit
 * Created by gen on 12.09.2017.
 */

@Module
class RetrofitModule {

    /**
     * Базовый провайдер Retrofit
     * @return Retrofit
     */
    @Singleton
    @Provides
    internal fun provideRetrofit(): Retrofit {
        val httpClient = OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)

        setDebugOptions(httpClient)

        val builder = GsonBuilder()
        builder.registerTypeAdapterFactory(ResultTypeAdapterFactory())

        return Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create(builder.create()))
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .client(httpClient.build())
                .build()
    }

    /**
     * API получения карт
     * @param retrofit Retrofit
     * @return API
     */
    @Singleton
    @Provides
    internal fun provideCardApi(retrofit: Retrofit): CardApi {
        return retrofit.create(CardApi::class.java)
    }

    /**
     * API получения сетов
     * @param retrofit Retrofit
     * @return API
     */
    @Singleton
    @Provides
    internal fun provideSetsApi(retrofit: Retrofit): SetsApi {
        return retrofit.create(SetsApi::class.java)
    }

    /**
     * Устанавливает опции для отладки
     * @param httpClient Клиент
     */
    private fun setDebugOptions(httpClient: OkHttpClient.Builder) {
        if (BuildConfig.DEBUG_MODE) {
            //Логгер в режиме отладки для всех запросов
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.HEADERS
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
        }
    }

    companion object {
        private const val READ_TIMEOUT: Long = 15
        private const val CONNECT_TIMEOUT: Long = 15
    }
}
