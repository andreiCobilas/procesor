package org.game.processor.general.model

import javax.persistence.*

@Entity
data class Account(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column
    var balance: Double = 0.0,
    val denomination: Int = 8,
    val currency: String = "EUR",
)
