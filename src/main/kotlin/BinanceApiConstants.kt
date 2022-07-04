/**
 * Constants used throughout Binance's API.
 */
object BinanceApiConstants {
    const val REST_API_URL_TESTNET = "https://testnet.binancefuture.com"
    const val WEBSOCKET_URL_TESTNET = "wss://stream.binancefuture.com"
    const val API_KEY_HEADER = "X-MBX-APIKEY"
    const val ENDPOINT_SECURITY_TYPE_APIKEY = "APIKEY"
    const val ENDPOINT_SECURITY_TYPE_APIKEY_HEADER = "$ENDPOINT_SECURITY_TYPE_APIKEY: #"
    const val ENDPOINT_SECURITY_TYPE_SIGNED = "SIGNED"
    const val ENDPOINT_SECURITY_TYPE_SIGNED_HEADER = "$ENDPOINT_SECURITY_TYPE_SIGNED: #"
    const val USER_RECEIVING_WINDOW = 60000L
}