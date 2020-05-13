package br.eti.rafaelcouto.temporada.network.config

import br.eti.rafaelcouto.temporada.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

object AuthenticatorClient {
    private const val APIKEY_FIELD = "api_key"

    fun build(): OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val original = chain.request()
                val originalUrl = original.url

                val newUrl = originalUrl.newBuilder()
                    .addQueryParameter(APIKEY_FIELD, BuildConfig.API_KEY)
                    .build()

                val builder = original.newBuilder().url(newUrl)

                return chain.proceed(builder.build())
            }
        })

        if (BuildConfig.DEBUG) addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
    }.build()
}
