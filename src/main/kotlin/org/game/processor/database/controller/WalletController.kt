package org.game.processor.database.controller

import org.game.processor.general.model.Account
import org.game.processor.database.service.WalletService
import org.game.processor.general.model.Wallet
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/wallets")
class WalletController(private val walletService: WalletService) {
    @PostMapping
    fun createWallet(@RequestBody wallet: Wallet): Wallet {
        return walletService.createWallet(wallet)
    }

    @PostMapping("/{walletId}/accounts")
    fun addAccountToWallet(@PathVariable walletId: Long, @RequestBody account: Account): Wallet {
        return walletService.addAccountToWallet(walletId, account)
    }

    @GetMapping("/{walletId}/accounts/{accountId}")
    fun getAccountFromWallet(@PathVariable walletId: Long, @PathVariable accountId: Long): Account {
        return walletService.getAccountFromWallet(walletId, accountId)
    }

    @PutMapping("/{walletId}/accounts/{accountId}")
    fun updateAccountInWallet(
        @PathVariable walletId: Long,
        @PathVariable accountId: Long,
        @RequestParam balance: Double
    ): Account {
        return walletService.updateAccountInWallet(walletId, accountId, balance)
    }

    @DeleteMapping("/{walletId}")
    fun deleteWallet(@PathVariable walletId: Long) {
        walletService.deleteWallet(walletId)
    }

    @GetMapping("/{walletId}")
    fun getWallet(@PathVariable walletId: Long): Wallet {
       return walletService.getWallet(walletId)
    }
}