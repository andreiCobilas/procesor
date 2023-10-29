package org.game.processor.app.model.request

import com.fasterxml.jackson.annotation.JsonProperty

data class BetMeta(
        @JsonProperty("bets") val bets: List<Bet>
)
