package org.game.processor.general.model

import javax.persistence.*

@Entity
data class Wallet(
        @Id
        val userId: Long = 0,
        val userNick: String = "default",
        val maxWin: Int = 2000,

        @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
        @JoinColumn(name = "wallet_id")
        var accounts: List<Account> = mutableListOf()
)

