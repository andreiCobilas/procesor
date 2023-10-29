package org.game.processor.app.model.request

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Request(
        @JsonProperty("token") val token: String,
        @JsonProperty("data") val data: Data
)
