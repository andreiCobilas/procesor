package org.game.processor.app.model.response

import com.fasterxml.jackson.annotation.JsonProperty

data class OperationResponse(
        @JsonProperty("token") val token: String,
        @JsonProperty("isSuccess") val isSuccess: Boolean,
        @JsonProperty("error") val error: String?,
        @JsonProperty("errorMsg") val errorMsg: String?,
        @JsonProperty("data") val data: DebitData?
)