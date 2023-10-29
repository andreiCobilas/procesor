package org.game.processor.app.model.request

import com.fasterxml.jackson.annotation.JsonProperty

data class SpinMeta (
        @JsonProperty("lines") val lines: Int,
        @JsonProperty("betPerLine") val betPerLine: Int,
        @JsonProperty("totalBet") val totalBet: Int,
        @JsonProperty("symbolMatrix") val symbolMatrix: List<List<Int>>
)
