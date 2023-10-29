package org.game.processor.app.request

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import okhttp3.ResponseBody
import org.game.processor.app.exception.SourceException
import org.game.processor.general.model.Account
import org.game.processor.general.model.Wallet

import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Recover
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import retrofit2.Call

@Service
class DatabaseRequestHandler(
    private val retrofitClient: DatabaseRequest,
    private val objectMapper: ObjectMapper
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Retryable(
        value = [Exception::class],
        backoff = Backoff(delay = 2000, random = true),
        maxAttempts = 4,
        listeners = ["retryListener"]
    )
    fun getWallet(walletId: Long): Wallet {
        val jsonString = executeCall(
            callerFunction = "createWallet",
            callId = walletId,
            call = retrofitClient.getWallet(walletId)
        )
        return parseWalletResponse(jsonString)
    }

    @Retryable(
        value = [Exception::class],
        backoff = Backoff(delay = 2000, random = true),
        maxAttempts = 4,
        listeners = ["retryListener"]
    )
    fun getAccountFromWallet(walletId: Long, accountId: Long): Account {
        val jsonString = executeCall(
            callerFunction = "createWallet",
            callId = accountId,
            call = retrofitClient.getAccountFromWallet(walletId, accountId)
        )
        return parseAccountResponse(jsonString)
    }

    @Retryable(
        value = [Exception::class],
        backoff = Backoff(delay = 2000, random = true),
        maxAttempts = 4,
        listeners = ["retryListener"]
    )
    fun updateAccountInWallet(walletId: Long, accountId: Long, newBalance: Double): String {
        return executeCall(
            callerFunction = "updateAccountInWallet",
            callId = accountId,
            call = retrofitClient.updateAccountInWallet(walletId, accountId, newBalance)
        )
    }

    private fun parseWalletResponse(response: String): Wallet {
        return objectMapper.readValue(response, Wallet::class.java)
    }

    private fun parseAccountResponse(response: String): Account {
        return objectMapper.readValue(response, Account::class.java)
    }

    private fun executeCall(callerFunction: String, callId: Long, call: Call<ResponseBody>): String {
        val response = try {
            call.execute()
        } catch (err: Exception) {
            throw SourceException(callerFunction, callId, "Execution occurred during call.execute", err)
        }

        val responseBody = response.body()
        if (response.code() != 200) {
            throw SourceException(
                callerFunction,
                callId,
                "Exception occurred because of returned  status_code: ${response.code()}",
                null
            )
        } else if (responseBody == null) {
            throw SourceException(callerFunction, callId, "Exception occurred because a Null return value", null)
        } else {
            logger.debug("Request by {} was successfully performed on source {}", callerFunction, callId)
            return responseBody.string()
        }
    }

    @Recover
    fun recover(error: Exception): String {
        if (error is SourceException) {
            logger.error(
                "All attempts have failed for function: {} with source: {}",
                error.callerFunction,
                error.id
            )
        } else {
            logger.error(
                "All attempts have failed with unknown error, with error message: {}",
                error.message
            )
        }
        throw error
    }
}