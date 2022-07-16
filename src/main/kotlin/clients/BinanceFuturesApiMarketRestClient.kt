package clients

import CANDLESTICK_CHART_INTERVAL
import service.BinanceApiService
import service.BinanceApiServiceMarketData

class BinanceFuturesApiMarketRestClient(
    apiUrl: String,
) {

    private val service =
        BinanceApiService.createService(BinanceApiServiceMarketData::class.java, apiUrl)

    fun exchangeInfo() = BinanceApiService.executeSync(service.exchangeInfo())

    fun candlesTick(
        symbol: String,
        interval: CANDLESTICK_CHART_INTERVAL,
        startTime: Long? = null,
        endTime: Long? = null,
        limit: Int? = null
    ) = BinanceApiService.executeSync(
        service.candlesTick(
            symbol,
            interval.apiRepresentation,
            startTime,
            endTime,
            limit
        )
    )

    fun markPrice(symbol: String? = null) = BinanceApiService.executeSync(service.markPrice(symbol))

}