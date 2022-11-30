package com.krokodilushka.kotlin_binance_futures_api.clients

import com.krokodilushka.kotlin_binance_futures_api.BinanceApiConstants
import com.krokodilushka.kotlin_binance_futures_api.MarginTypeEnum
import com.krokodilushka.kotlin_binance_futures_api.OrderSideEnum
import com.krokodilushka.kotlin_binance_futures_api.OrderTypeEnum
import com.krokodilushka.kotlin_binance_futures_api.PositionSideEnum
import com.krokodilushka.kotlin_binance_futures_api.ResponseTypeEnum
import com.krokodilushka.kotlin_binance_futures_api.TimeInForceEnum
import com.krokodilushka.kotlin_binance_futures_api.WorkingTypeEnum
import com.krokodilushka.kotlin_binance_futures_api.service.BinanceApiService
import com.krokodilushka.kotlin_binance_futures_api.service.BinanceApiServiceUser
import java.math.BigDecimal

class BinanceFuturesApiUserRestClient(
    apiKey: String,
    secret: String,
    apiUrl: String,
) {

    private val service =
        BinanceApiService.createService(BinanceApiServiceUser::class.java, apiKey, secret, apiUrl)

    fun openOrders(symbol: String? = null) = BinanceApiService.executeSync(
        service.openOrders(
            symbol,
            BinanceApiConstants.USER_RECEIVING_WINDOW,
            System.currentTimeMillis()
        )
    )

    fun positions(symbol: String? = null) = BinanceApiService.executeSync(
        service.positions(
            symbol,
            BinanceApiConstants.USER_RECEIVING_WINDOW,
            System.currentTimeMillis()
        )
    )

    fun newOrder(
        symbol: String,
        side: OrderSideEnum,
        positionSide: PositionSideEnum? = null,
        type: OrderTypeEnum,
        timeInForce: TimeInForceEnum? = null,
        quantity: BigDecimal? = null,
        reduceOnly: Boolean? = null,
        price: BigDecimal? = null,
        newClientOrderId: String? = null,
        stopPrice: BigDecimal? = null,
        closePosition: Boolean? = null,
        activationPrice: BigDecimal? = null,
        callbackRate: BigDecimal? = null,
        workingType: WorkingTypeEnum? = null,
        newOrderRespType: ResponseTypeEnum? = null,
    ) = BinanceApiService.executeSync(
        service.newOrder(
            symbol,
            side,
            positionSide,
            type,
            timeInForce,
            quantity?.stripTrailingZeros()?.toPlainString(),
            reduceOnly,
            price?.stripTrailingZeros()?.toPlainString(),
            newClientOrderId,
            stopPrice?.stripTrailingZeros()?.toPlainString(),
            closePosition,
            activationPrice?.stripTrailingZeros()?.toPlainString(),
            callbackRate?.stripTrailingZeros()?.toPlainString(),
            workingType,
            newOrderRespType,
            BinanceApiConstants.USER_RECEIVING_WINDOW,
            System.currentTimeMillis()
        )
    )

    fun deleteOrder(
        symbol: String,
        orderId: Long? = null,
        origClientOrderId: String? = null
    ) = BinanceApiService.executeSync(
        service.deleteOrder(
            symbol,
            orderId.toString(),
            origClientOrderId,
            BinanceApiConstants.USER_RECEIVING_WINDOW,
            System.currentTimeMillis()
        )
    )

    fun futuresAccountBalance() = BinanceApiService.executeSync(
        service.futuresAccountBalance(
            BinanceApiConstants.USER_RECEIVING_WINDOW,
            System.currentTimeMillis()
        )
    )

    fun currentMultiAssetsMode() = BinanceApiService.executeSync(
        service.currentMultiAssetsMode(
            BinanceApiConstants.USER_RECEIVING_WINDOW,
            System.currentTimeMillis()
        )
    )

    fun accountInformation() = BinanceApiService.executeSync(
        service.accountInformation(
            BinanceApiConstants.USER_RECEIVING_WINDOW,
            System.currentTimeMillis()
        )
    )

    fun startUserDataStream() =
        BinanceApiService.executeSync(service.startUserDataStream())

    fun keepAliveUserDataStream() =
        BinanceApiService.executeSync(service.keepAliveUserDataStream())

    fun closeUserDataStream(listenKey: String) =
        BinanceApiService.executeSync(service.closeUserDataStream(listenKey))

    fun leverageBracket(symbol: String? = null) = BinanceApiService.executeSync(
        service.leverageBracket(symbol, BinanceApiConstants.USER_RECEIVING_WINDOW, System.currentTimeMillis())
    )

    fun changeInitialLeverage(symbol: String, leverage: Int) = BinanceApiService.executeSync(
        service.changeInitialLeverage(
            symbol,
            leverage.toString(),
            BinanceApiConstants.USER_RECEIVING_WINDOW,
            System.currentTimeMillis()
        )
    )

    fun changeMarginType(symbol: String, marginTypeEnum: MarginTypeEnum) = BinanceApiService.executeSync(
        service.changeMarginType(
            symbol,
            marginTypeEnum.toString(),
            BinanceApiConstants.USER_RECEIVING_WINDOW,
            System.currentTimeMillis()
        )
    )
}