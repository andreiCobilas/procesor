package org.game.processor.app.repository

import org.game.processor.general.model.Wallet
import org.springframework.data.jpa.repository.JpaRepository

interface GatewayRepository : JpaRepository<Wallet, Long>