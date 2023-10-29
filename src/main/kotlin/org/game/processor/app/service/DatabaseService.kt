package org.game.processor.app.service

import org.game.processor.app.request.DatabaseRequestHandler
import org.game.processor.general.model.Account
import org.game.processor.general.model.Wallet
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DatabaseService(private val databaseRequestHandler: DatabaseRequestHandler) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getValidWallet(walletId: Long): Wallet? {
        return try {
            databaseRequestHandler.getWallet(walletId)
        } catch (err: Exception) {
            logger.error("Failed to obtain wallet from database with id:{}", walletId, err)
            null
        }
    }

    fun getAccount(userId: Long, accountId: Long): Account? {
        return try {
            databaseRequestHandler.getAccountFromWallet(userId, accountId)
        } catch (err: Exception) {
            logger.error("Failed to obtain account with id:{} for wallet:{} from database", accountId, userId, err)
            null
        }
    }

    fun updateBalance(userId: Long, accountId: Long, newBalance: Double): Boolean {
        return try {
            databaseRequestHandler.updateAccountInWallet(userId, accountId, newBalance)
            true
        } catch (err: Exception) {
            logger.error("Failed to update account with id:{} for wallet:{} from database", accountId, userId, err)
            false
        }
    }
}