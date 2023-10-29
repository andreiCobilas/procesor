package org.game.processor.app.model.request

import com.fasterxml.jackson.annotation.JsonProperty

data class Bet(
        @JsonProperty("balls") val balls: List<Int>,
        @JsonProperty("colors") val colors: List<String>,
        @JsonProperty("amount") val amount: Int
)
