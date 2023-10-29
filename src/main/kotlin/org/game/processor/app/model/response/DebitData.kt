package org.game.processor.app.model.response

import com.fasterxml.jackson.annotation.JsonProperty

data class DebitData(
        @JsonProperty("transactionId") val transactionId: String,
        @JsonProperty("userNick") val userNick: String,
        @JsonProperty("amount") val amount: Double,
        @JsonProperty("denomination") val denomination: Int,
        @JsonProperty("maxWin") val maxWin: Int,
        @JsonProperty("currency") val currency: String
)
