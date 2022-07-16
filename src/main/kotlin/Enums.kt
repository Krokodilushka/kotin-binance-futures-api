enum class SYMBOL_TYPE {
    FUTURE
}

enum class CONTRACT_TYPE {
    PERPETUAL,
    CURRENT_MONTH,
    NEXT_MONTH,
    CURRENT_QUARTER,
    NEXT_QUARTER,
    PERPETUAL_DELIVERING
}

enum class CONTRACT_STATUS {
    PENDING_TRADING,
    TRADING,
    PRE_DELIVERING,
    DELIVERING,
    DELIVERED,
    PRE_SETTLE,
    SETTLING,
    CLOSE
}

enum class ORDER_STATUS {
    NEW,
    PARTIALLY_FILLED,
    FILLED,
    CANCELED,
    REJECTED,
    EXPIRED
}

enum class ORDER_TYPE {
    LIMIT,
    MARKET,
    STOP,
    STOP_MARKET,
    TAKE_PROFIT,
    TAKE_PROFIT_MARKET,
    TRAILING_STOP_MARKET
}

enum class ORDER_SIDE {
    BUY,
    SELL
}

enum class POSITION_SIDE {
    BOTH,
    LONG,
    SHORT
}

enum class TIME_IN_FORCE {
    GTC,
    IOC,
    FOK,
    GTX
}

enum class WORKING_TYPE {
    MARK_PRICE,
    CONTRACT_PRICE
}

enum class RESPONSE_TYPE {
    ACK,
    RESULT
}

enum class CANDLESTICK_CHART_INTERVAL(val apiRepresentation: String) {
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
    MO1("1M"),
}

enum class RATE_LIMIT {
    REQUEST_WEIGHT,
    ORDERS
}

enum class RATE_LIMIT_INTERVAL(val headersRepresentation: String) {
    MINUTE("1m"), SECOND("1s")
}

enum class MARGIN_TYPE {
    ISOLATED, CROSSED
}