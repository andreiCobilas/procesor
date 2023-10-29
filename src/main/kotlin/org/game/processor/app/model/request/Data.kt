package org.game.processor.app.model.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Data(
        @JsonProperty("transactionId") val transactionId: String? = null,
        @JsonProperty("gameSessionId") val gameSessionId: Long,
        @JsonProperty("currency") val currency: String,
        @JsonProperty("amount") val amount: Double = 0.0,
        @JsonProperty("betId") val betId: String? = null,
        @JsonProperty("spinMeta") val spinMeta: SpinMeta? = null,
        @JsonProperty("betMeta") val betMeta: BetMeta? = null,
        @JsonProperty("notes") val notes: String? = null
)

