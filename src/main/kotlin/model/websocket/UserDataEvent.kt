package model.websocket

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

sealed class UserDataEvent : WebSocketEvent() {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class OrderTradeUpdate(
        @JsonProperty("e") val eventType: String,
        @JsonProperty("E") val eventTime: Long,
        @JsonProperty("T") val transactionTime: Long,
        @JsonProperty("o") val order: Order,
    ) : UserDataEvent() {
        @JsonIgnoreProperties(ignoreUnknown = true)
        data class Order(
            @JsonProperty("s") val symbol: String,
            @JsonProperty("c") val clientOrderId: String,
            @JsonProperty("S") val side: OrderSide,
            @JsonProperty("o") val orderType: OrderType,
            @JsonProperty("f") val timeInForce: OrderTimeInForce,
            @JsonProperty("q") val originalQuantity: BigDecimal,
            @JsonProperty("p") val originalPrice: BigDecimal,
            @JsonProperty("sp") val stopPrice: BigDecimal,
            @JsonProperty("x") val executionType: OrderExecutionType,
            @JsonProperty("X") val orderStatus: OrderStatus,
            @JsonProperty("i") val orderId: Long,
            @JsonProperty("l") val orderLastFilledQuantity: BigDecimal,
            @JsonProperty("z") val orderFilledAccumulatedQuantity: BigDecimal,
            @JsonProperty("L") val lastFilledPrice: BigDecimal,
            @JsonProperty("N") val commissionAsset: String?,
            @JsonProperty("n") val commission: BigDecimal?,
            @JsonProperty("T") val orderTradeTime: Long,
            @JsonProperty("t") val tradeId: Long,
            @JsonProperty("b") val bidsNotional: BigDecimal,
            @JsonProperty("a") val askNotional: BigDecimal,
            @JsonProperty("m") val isMakerSide: Boolean,
            @JsonProperty("R") val isReduceOnly: Boolean,
            @JsonProperty("wt") val stopPriceWorkingType: OrderWorkingType,
            @JsonProperty("ot") val originalOrderType: OrderType,
            @JsonProperty("ps") val positionSide: OrderPositionSide,
            @JsonProperty("cp") val isCloseAll: Boolean,
            @JsonProperty("ap") val activationPrice: BigDecimal,
            @JsonProperty("cr") val callbackRate: BigDecimal?,
            @JsonProperty("pP") val pP: Boolean,
            @JsonProperty("si") val si: Long,
            @JsonProperty("ss") val ss: Long,
            @JsonProperty("rp") val realizedProfit: BigDecimal
        ) : UserDataEvent() {
            enum class OrderSide {
                BUY, SELL
            }

            enum class OrderType {
                MARKET, LIMIT, STOP, TAKE_PROFIT, LIQUIDATION, TRAILING_STOP_MARKET
            }

            enum class OrderExecutionType {
                NEW, CANCELED, CALCULATED, EXPIRED, TRADE
            }

            enum class OrderStatus {
                NEW, PARTIALLY_FILLED, FILLED, CANCELED, EXPIRED, NEW_INSURANCE, NEW_ADL
            }

            enum class OrderTimeInForce {
                GTC, IOC, FOK, GTX
            }

            enum class OrderWorkingType {
                MARK_PRICE, CONTRACT_PRICE
            }

            enum class OrderPositionSide {
                LONG, SHORT, BOTH
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class AccountUpdate(
        @JsonProperty("e") val eventType: String,
        @JsonProperty("E") val eventTime: Long,
        @JsonProperty("T") val transactionTime: Long,
        @JsonProperty("a") val data: Data,
    ) : UserDataEvent() {
        @JsonIgnoreProperties(ignoreUnknown = true)
        data class Data(
            @JsonProperty("m") val eventReason: EventReason,
            @JsonProperty("B") val balances: List<Balance>,
            @JsonProperty("P") val positions: List<Position>,
        ) {
            @JsonIgnoreProperties(ignoreUnknown = true)
            data class Balance(
                @JsonProperty("a") val asset: String,
                @JsonProperty("wb") val walletBalance: BigDecimal,
                @JsonProperty("cw") val crossWalletBalance: BigDecimal,
                @JsonProperty("bc") val balanceChangeExceptPnLAndCommission: BigDecimal
            )

            @JsonIgnoreProperties(ignoreUnknown = true)
            data class Position(
                @JsonProperty("s") val symbol: String,
                @JsonProperty("pa") val positionAmount: BigDecimal,
                @JsonProperty("ep") val entryPrice: BigDecimal,
                @JsonProperty("cr") val preFeeAccumulatedRealized: BigDecimal,
                @JsonProperty("up") val unrealizedPnL: BigDecimal,
                @JsonProperty("mt") val marginType: String,
                @JsonProperty("iw") val isolatedWallet: BigDecimal,
                @JsonProperty("ps") val positionSide: PositionSide,
                @JsonProperty("ma") val ma: String,
            ) {
                enum class PositionSide {
                    LONG, SHORT, BOTH
                }
            }

            enum class EventReason {
                DEPOSIT,
                WITHDRAW,
                ORDER,
                FUNDING_FEE,
                WITHDRAW_REJECT,
                ADJUSTMENT,
                INSURANCE_CLEAR,
                ADMIN_DEPOSIT,
                ADMIN_WITHDRAW,
                MARGIN_TRANSFER,
                MARGIN_TYPE_CHANGE,
                ASSET_TRANSFER,
                OPTIONS_PREMIUM_FEE,
                OPTIONS_SETTLE_PROFIT,
                AUTO_EXCHANGE
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class AccountConfigUpdate(
        @JsonProperty("e") val eventType: String,
        @JsonProperty("E") val eventTime: Long,
        @JsonProperty("T") val transactionTime: Long,
        @JsonProperty("ac") val leverageOfTradePair: Ac?,
        @JsonProperty("ai") val multiAssetMode: Ai?,
    ) : UserDataEvent() {
        @JsonIgnoreProperties(ignoreUnknown = true)
        data class Ac(
            @JsonProperty("s") val symbol: String,
            @JsonProperty("l") val leverage: Int,
        )

        data class Ai(
            @JsonProperty("j") val isMultiAssetMode: Boolean,
        )
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class MarginCall(
        @JsonProperty("e") val eventType: String,
        @JsonProperty("E") val eventTime: Long,
        @JsonProperty("cw") val crossWalletBalance: BigDecimal?,
        @JsonProperty("p") val positions: List<Position>,
    ) : UserDataEvent() {
        @JsonIgnoreProperties(ignoreUnknown = true)
        data class Position(
            @JsonProperty("s") val symbol: String,
            @JsonProperty("ps") val positionSide: PositionSide,
            @JsonProperty("pa") val positionAmount: BigDecimal,
            @JsonProperty("mt") val marginType: String,
            @JsonProperty("iw") val isolatedWallet: BigDecimal?,
            @JsonProperty("mp") val markPrice: BigDecimal,
            @JsonProperty("up") val unrealizedPnL: BigDecimal,
            @JsonProperty("mm") val maintenanceMarginRequired: BigDecimal,
        ) {
            enum class PositionSide {
                LONG, SHORT, BOTH
            }
        }
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    data class DataStreamExpired(
        @JsonProperty("e") val eventType: String,
        @JsonProperty("E") val eventTime: Long,
    ) : UserDataEvent()
}