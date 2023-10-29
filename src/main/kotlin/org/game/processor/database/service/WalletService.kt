package org.game.processor.database.service

import org.game.processor.database.exception.AccountNotFoundException
import org.game.processor.database.exception.WalletNotFoundException
import org.game.processor.general.model.Account
import org.game.processor.general.model.Wallet
import org.game.processor.database.repository.AccountRepository
import org.game.processor.database.repository.WalletRepository
import org.springframework.stereotype.Service

@Service
class WalletService(private val walletRepository: WalletRepository, private val accountRepository: AccountRepository) {

    fun createWallet(wallet: Wallet): Wallet {
        return walletRepository.save(wallet)
    }

    fun addAccountToWallet(walletId: Long, account: Account): Wallet {
        val wallet = walletRepository.findById(walletId).orElseThrow { WalletNotFoundException("Wallet not found") }
        wallet.accounts = wallet.accounts.plus(account)
        return walletRepository.save(wallet)
    }

    fun updateAccountBalance(accountId: Long, newBalance: Double): Account {
        val account =
            accountRepository.findById(accountId).orElseThrow { AccountNotFoundException("Account not found") }
        account.balance = newBalance
        return accountRepository.save(account)
    }

    fun getAccountFromWallet(walletId: Long, accountId: Long): Account {
        val wallet = walletRepository.findById(walletId).orElseThrow { WalletNotFoundException("Wallet not found") }
        return wallet.accounts.find { it.id == accountId }
            ?: throw AccountNotFoundException("Account not found in the wallet")
    }

    fun updateAccountInWallet(walletId: Long, accountId: Long, newBalance: Double): Account {
        val wallet = walletRepository.findById(walletId).orElseThrow { WalletNotFoundException("Wallet not found") }
        val account = wallet.accounts.find { it.id == accountId }
            ?: throw AccountNotFoundException("Account not found in the wallet")

        account.balance = newBalance
        accountRepository.save(account)
        return account
    }

    fun deleteWallet(walletId: Long) {
        val wallet = walletRepository.findById(walletId).orElseThrow { WalletNotFoundException("Wallet not found") }
        walletRepository.delete(wallet)
    }

    fun getWallet(walletId: Long): Wallet {
        return walletRepository.findById(walletId).orElseThrow { WalletNotFoundException("Wallet not found") }
    }
}
