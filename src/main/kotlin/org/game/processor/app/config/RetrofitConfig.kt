package org.game.processor.app.config

import okhttp3.OkHttpClient
import org.game.processor.app.request.DatabaseRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

@Configuration
class RetrofitConfig(
        val appConfig: AppConfig,
) {

    @Bean
    fun retrofitClient(): DatabaseRequest {

        val client =
                OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build()

        return Retrofit.Builder()
                .baseUrl(appConfig.sourceUrl)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
                .create(DatabaseRequest::class.java)
    }
}