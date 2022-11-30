package clients

import CandlesTickChartIntervalEnum
import ContinuousContractKlineTypeEnum
import service.BinanceApiService
import service.BinanceApiServiceMarketData

class BinanceFuturesApiMarketRestClient(
    apiUrl: String,
) {

    private val service =
        BinanceApiService.createService(BinanceApiServiceMarketData::class.java, apiUrl)

    fun time() = BinanceApiService.executeSync(service.time())

    fun exchangeInfo() = BinanceApiService.executeSync(service.exchangeInfo())

    fun kline(
        symbol: String,
        interval: CandlesTickChartIntervalEnum,
        startTime: Long? = null,
        endTime: Long? = null,
        limit: Int? = null
    ) = BinanceApiService.executeSync(
        service.kline(
            symbol,
            interval.apiRepresentation,
            startTime,
            endTime,
            limit
        )
    )

    fun continuousContractKline(
        pair: String,
        contractType: ContinuousContractKlineTypeEnum,
        interval: CandlesTickChartIntervalEnum,
        startTime: Long? = null,
        endTime: Long? = null,
        limit: Int? = null
    ) = BinanceApiService.executeSync(
        service.continuousContractKline(
            pair,
            contractType.toString(),
            interval.apiRepresentation,
            startTime,
            endTime,
            limit
        )
    )

    fun markPrice(symbol: String? = null) = BinanceApiService.executeSync(service.markPrice(symbol))

    fun price() = BinanceApiService.executeSync(service.price())

}