package org.game.processor

import org.game.processor.config.WalletServiceTestConfig
import org.game.processor.database.exception.AccountNotFoundException
import org.game.processor.database.exception.WalletNotFoundException
import org.game.processor.general.model.Account
import org.game.processor.general.model.Wallet
import org.game.processor.database.repository.AccountRepository
import org.game.processor.database.repository.WalletRepository
import org.game.processor.database.service.WalletService
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.testng.Assert.assertThrows
import java.util.Optional
@SpringBootTest
@ContextConfiguration(classes = [WalletServiceTestConfig::class])
class WalletServiceTests {

    @Autowired
    private lateinit var walletService: WalletService

    @MockBean
    private lateinit var walletRepository: WalletRepository

    @MockBean
    private lateinit var accountRepository: AccountRepository

    private lateinit var testWallet: Wallet

    @BeforeEach
    fun setup() {
        val account1 = Account(id = 1, balance = 100.0)
        val account2 = Account(id = 2, balance = 200.0)

        testWallet = Wallet(userId = 1, userNick = "test", accounts = mutableListOf(account1, account2))

        Mockito.`when`(walletRepository.save(testWallet)).thenReturn(testWallet)
        Mockito.`when`(accountRepository.save(account1)).thenReturn(account1)
        Mockito.`when`(accountRepository.save(account2)).thenReturn(account2)
    }

    @AfterEach
    fun cleanup() {
        Mockito.`when`(walletRepository.findById(testWallet.userId)).thenReturn(Optional.of(testWallet))
        walletService.deleteWallet(testWallet.userId)
    }

    // Test for creating a wallet
    @Test
    fun `create a wallet`() {
        val createdWallet = walletService.createWallet(testWallet)
        //Assert that the wallet was created correctly
        assertNotNull(createdWallet)
        assertEquals(2, createdWallet.accounts.size)
        assertEquals(1, createdWallet.userId)
        assertEquals(1, createdWallet.accounts[0].id)
        assertEquals(2, createdWallet.accounts[1].id)
    }

    // Test for updating the account value
    @Test
    fun `update account balance in wallet`() {
        val updatedBalance = 300.0
        val accountIdToUpdate = 1L

        // Assume 'testWallet' is available in the walletRepository
        Mockito.`when`(walletRepository.findById(testWallet.userId)).thenReturn(Optional.of(testWallet))

        // Update the account balance
        val updatedAccount = walletService.updateAccountInWallet(testWallet.userId, accountIdToUpdate, updatedBalance)

        assertEquals(updatedBalance, updatedAccount.balance)
    }

    // Test for error scenarios - Account not found in wallet
    @Test
    fun `update account balance in wallet - Account not found`() {
        val updatedBalance = 300.0
        val accountIdToUpdate = 3L // Assuming this account ID doesn't exist in the testWallet

        // Assume 'testWallet' is available in the walletRepository
        Mockito.`when`(walletRepository.findById(testWallet.userId)).thenReturn(Optional.of(testWallet))

        // Verify that an EntityNotFoundException is thrown when trying to update a non-existent account
        assertThrows(AccountNotFoundException::class.java) {
            walletService.updateAccountInWallet(testWallet.userId, accountIdToUpdate, updatedBalance)
        }
    }

    // Test for error scenarios - Wallet not found
    @Test
    fun `update account balance in wallet - Wallet not found`() {
        val updatedBalance = 300.0
        val accountIdToUpdate = 1L

        // Assume 'testWallet' is not available in the walletRepository
        Mockito.`when`(walletRepository.findById(testWallet.userId)).thenReturn(Optional.empty())

        // Verify that an EntityNotFoundException is thrown when trying to update an account in a non-existent wallet
        assertThrows(WalletNotFoundException::class.java) {
            walletService.updateAccountInWallet(testWallet.userId, accountIdToUpdate, updatedBalance)
        }
    }
}
