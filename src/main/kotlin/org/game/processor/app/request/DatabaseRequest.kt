package org.game.processor.app.request

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface DatabaseRequest {
    @GET("wallets/{walletId}")
    fun getWallet(@Path("walletId") walletId: Long): Call<ResponseBody>

    @GET("wallets/{walletId}/accounts/{accountId}")
    fun getAccountFromWallet(
        @Path("walletId") walletId: Long,
        @Path("accountId") accountId: Long
    ): Call<ResponseBody>

    @PUT("wallets/{walletId}/accounts/{accountId}")
    fun updateAccountInWallet(
        @Path("walletId") walletId: Long,
        @Path("accountId") accountId: Long,
        @Query("balance") balance: Double
    ): Call<ResponseBody>
}
