package org.game.processor.app.service

import org.game.processor.app.model.response.OperationResponse
import org.game.processor.app.model.request.Request
import org.game.processor.app.model.response.BalanceResponse
import org.game.processor.app.model.response.DebitData
import org.game.processor.app.util.dict.CurrencyDictionary
import org.game.processor.general.model.Account
import org.game.processor.general.model.Wallet
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class GatewayService(
    private val databaseService: DatabaseService,
    private val currencyDictionary: CurrencyDictionary
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun balance(request: Request): BalanceResponse? {
        val gameSessionId = request.data.gameSessionId
        val currency = request.data.currency
        logger.info("Started balance request with gameSessionId={}", gameSessionId)

        val wallet = getWalletFromDb(request.data.gameSessionId, currency) ?: return null
        val account = getAccountForWallet(wallet, currency) ?: return null
        return createBalanceResponse(request.token, wallet, account)
    }

    fun debit(request: Request): OperationResponse {
        val gameSessionId = request.data.gameSessionId
        val currency = request.data.currency

        logger.info("Started debit request with gameSessionId={}", gameSessionId)

        val transactionId = request.data.transactionId
            ?: return createFailedOperationResponse(request.token, "No transactionId for id:$gameSessionId")

        val wallet = getWalletAndCheckFailure("debit", gameSessionId, currency)
            ?: return createFailedOperationResponse(request.token, "Failed to obtain wallet for id:$gameSessionId")

        val account = getAccountAndCheckFailure(wallet, currency, "debit", gameSessionId)
            ?: return createFailedOperationResponse(
                request.token,
                "Failed to obtain account for id:$gameSessionId and currency:$currency"
            )

        val amount = request.data.amount

        if (isOperationValid(account.balance, amount)) {
            val newBalance = account.balance - amount
            logger.info("*************** $newBalance")
            updateBalanceAndCheckFailure(wallet.userId, account.id, newBalance)
            return createSuccessfulOperationResponse(
                request.token,
                wallet,
                getAccountForWallet(wallet, currency)!!,
                transactionId
            )
        }

        return createFailedOperationResponse(
            request.token,
            "Insufficient funds to finish transaction on account with id:$gameSessionId and currency:$currency"
        )
    }

    fun credit(request: Request): OperationResponse {
        val gameSessionId = request.data.gameSessionId
        val currency = request.data.currency

        logger.info("Started credit request with gameSessionId={}", gameSessionId)

        val transactionId = request.data.transactionId
            ?: return createFailedOperationResponse(request.token, "No transactionId for id:$gameSessionId")

        val wallet = getWalletAndCheckFailure("credit", gameSessionId, currency)
            ?: return createFailedOperationResponse(request.token, "Failed to obtain wallet for id:$gameSessionId")

        val account = getAccountAndCheckFailure(wallet, currency, "credit", gameSessionId)
            ?: return createFailedOperationResponse(
                request.token,
                "Failed to obtain account for id:$gameSessionId and currency:$currency"
            )

        val amount = request.data.amount
        val newBalance = account.balance + amount
        updateBalanceAndCheckFailure(wallet.userId, account.id, newBalance)

        return createSuccessfulOperationResponse(
            request.token,
            wallet,
            getAccountForWallet(wallet, currency)!!,
            transactionId
        )
    }

    private fun getWalletAndCheckFailure(operationType: String, gameSessionId: Long, currency: String): Wallet? {
        logger.info("Started $operationType request with gameSessionId={}", gameSessionId)
        return getWalletFromDb(gameSessionId, currency)
    }

    private fun getAccountAndCheckFailure(
        wallet: Wallet,
        currency: String,
        operationType: String,
        gameSessionId: Long
    ): Account? {
        val account = getAccountForWallet(wallet, currency)
        if (account == null) {
            logger.error("Failed to obtain account for id:$gameSessionId and currency:$currency in $operationType operation")
        }
        return account
    }

    private fun updateBalanceAndCheckFailure(userId: Long, accountId: Long, newBalance: Double) {
        val updated = databaseService.updateBalance(userId, accountId, newBalance)
        if (!updated) {
            logger.error("Failed to update balance for userId:$userId and accountId:$accountId")
        }
    }


    private fun createFailedOperationResponse(token: String, errorMsg: String, error: String = ""): OperationResponse =
        OperationResponse(
            token = token,
            isSuccess = false,
            error = error,
            errorMsg = errorMsg,
            data = null
        )


    private fun createSuccessfulOperationResponse(
        token: String, wallet: Wallet, account: Account, transactionId: String
    ): OperationResponse {
        return OperationResponse(
            token = token,
            isSuccess = true,
            error = null,
            errorMsg = null,
            data = DebitData(
                transactionId = transactionId,
                userNick = wallet.userNick,
                amount = account.balance,
                denomination = account.denomination,
                maxWin = wallet.maxWin,
                currency = account.currency
            )
        )
    }

    private fun isOperationValid(balance: Double, amount: Double): Boolean {
        return (balance > amount)
    }

    private fun isValidCurrency(currencyName: String): Boolean {
        val currency = currencyDictionary.findByName(currencyName)
        return (currency != null)
    }

    private fun getWalletFromDb(gameSessionId: Long, currencyName: String): Wallet? {
        return if (isValidCurrency(currencyName)) {
            databaseService.getValidWallet(gameSessionId)
        } else {
            logger.error("Invalid currency: {} for gameSessionId: {}", currencyName, gameSessionId)
            null
        }
    }

    private fun getAccountForWallet(wallet: Wallet, currency: String): Account? {
        val accountId = wallet.accounts.find { it.currency == currency }?.id ?: return null
        return databaseService.getAccount(wallet.userId, accountId)
    }

    private fun createBalanceResponse(token: String, wallet: Wallet, account: Account): BalanceResponse {
        return BalanceResponse(
            userNick = wallet.userNick,
            userId = wallet.userId,
            amount = account.balance,
            denomination = account.denomination,
            maxWin = wallet.maxWin,
            currency = account.currency,
            jpKey = token
        )
    }
}