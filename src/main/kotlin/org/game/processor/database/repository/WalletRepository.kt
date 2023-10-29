package org.game.processor.database.repository

import org.game.processor.general.model.Wallet
import org.springframework.data.jpa.repository.JpaRepository

interface WalletRepository : JpaRepository<Wallet, Long>
