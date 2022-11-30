package com.krokodilushka.kotlin_binance_futures_api

import com.fasterxml.jackson.annotation.JsonValue

enum class SymbolTypeEnum {
    FUTURE
}

enum class ContractTypeEnum {
    PERPETUAL,
    CURRENT_MONTH,
    NEXT_MONTH,
    CURRENT_QUARTER,
    NEXT_QUARTER,
    PERPETUAL_DELIVERING
}

enum class ContractStatusEnum {
    PENDING_TRADING,
    TRADING,
    PRE_DELIVERING,
    DELIVERING,
    DELIVERED,
    PRE_SETTLE,
    SETTLING,
    CLOSE
}

enum class OrderStatusEnum {
    NEW,
    PARTIALLY_FILLED,
    FILLED,
    CANCELED,
    REJECTED,
    EXPIRED
}

enum class OrderTypeEnum {
    LIMIT,
    MARKET,
    STOP,
    STOP_MARKET,
    TAKE_PROFIT,
    TAKE_PROFIT_MARKET,
    TRAILING_STOP_MARKET
}

enum class OrderSideEnum {
    BUY,
    SELL
}

enum class PositionSideEnum {
    BOTH,
    LONG,
    SHORT
}

enum class TimeInForceEnum {
    GTC,
    IOC,
    FOK,
    GTX
}

enum class WorkingTypeEnum {
    MARK_PRICE,
    CONTRACT_PRICE
}

enum class ResponseTypeEnum {
    ACK,
    RESULT
}

enum class CandlesTickChartIntervalEnum(val apiRepresentation: String) {
    M1("1m"),
    M3("3m"),
    M5("5m"),
    M15("15m"),
    M30("30m"),
    H1("1h"),
    H2("2h"),
    H4("4h"),
    H6("6h"),
    H8("8h"),
    H12("12h"),
    D1("1d"),
    D3("3d"),
    W1("1w"),
    MO1("1M");

    @JsonValue
    fun continuousContractKlineEventRepresentation(): String? {
        return apiRepresentation
    }
}

enum class RateLimitEnum {
    REQUEST_WEIGHT,
    ORDERS
}

enum class RateLimitIntervalEnum(val headersRepresentation: String) {
    MINUTE("1m"), SECOND("1s")
}

enum class MarginTypeEnum {
    ISOLATED, CROSSED
}

enum class ContinuousContractKlineTypeEnum {
    PERPETUAL,
    CURRENT_QUARTER,
    NEXT_QUARTER,
}