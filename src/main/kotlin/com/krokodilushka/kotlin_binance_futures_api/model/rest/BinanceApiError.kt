package com.krokodilushka.kotlin_binance_futures_api.model.rest

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class BinanceApiError(
    @JsonProperty("code")
    val code: Int,
    @JsonProperty("msg")
    val msg: String?
)