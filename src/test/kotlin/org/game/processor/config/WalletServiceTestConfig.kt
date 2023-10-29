package org.game.processor.config

import org.game.processor.database.repository.AccountRepository
import org.game.processor.database.repository.WalletRepository
import org.game.processor.database.service.WalletService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WalletServiceTestConfig {
    @Bean
    fun walletService(walletRepository: WalletRepository, accountRepository: AccountRepository): WalletService {
        return WalletService(walletRepository, accountRepository)
    }
}