package com.krokodilushka.kotlin_binance_futures_api.service

import com.krokodilushka.kotlin_binance_futures_api.RateLimitIntervalEnum
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.krokodilushka.kotlin_binance_futures_api.BinanceApiConstants
import com.krokodilushka.kotlin_binance_futures_api.model.rest.BinanceApiError
import okhttp3.*
import org.apache.commons.codec.binary.Hex
import retrofit2.Call
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object BinanceApiService {

    val httpClient: OkHttpClient = OkHttpClient.Builder()
//            .pingInterval(20, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val converterFactory = JacksonConverterFactory.create(ObjectMapper().registerKotlinModule())
    private val errorBodyConverter = converterFactory.toErrorBodyConverter()

    fun <S> createService(serviceClass: Class<S>, baseUrl: String): S {
        return httpClient.retrofitBuilder(baseUrl).build().create(serviceClass)
    }

    fun <S> createService(serviceClass: Class<S>, apiKey: String, secret: String, baseUrl: String): S {
        return AuthenticationInterceptor(apiKey, secret).let {
            httpClient.newBuilder().addInterceptor(it).build()
        }.retrofitBuilder(baseUrl).build().create(serviceClass)
    }

    fun <T> executeSync(call: Call<T>): Response<T> {
        val response = call.execute()
        if (response.isSuccessful) {
            return response
        } else {
            val apiError = errorBodyConverter.convert(response.errorBody()!!)!!
            throw BinanceApiException(call.request(), response, apiError)
        }
    }

    private fun OkHttpClient.retrofitBuilder(baseUrl: String) = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(converterFactory)
        .client(this)

    private fun Converter.Factory.toErrorBodyConverter() = responseBodyConverter(
        BinanceApiError::class.java,
        arrayOfNulls(0),
        null
    ) as Converter<ResponseBody, BinanceApiError>

    data class ResponseWithIpLimits<T>(
        val response: Response<T>,
        val usedLimits: List<Limit>
    ) {
        data class Limit(
            val interval: RateLimitIntervalEnum,
            val used: Int,
        )
    }

    class BinanceApiException(
        val request: Request,
        val response: Response<*>,
        val apiError: BinanceApiError
    ) : RuntimeException() {

        override val message: String
            get() = "Http code: ${response.code()}. Api error code: ${apiError.code}. Api error message: \"${apiError.msg}\""
    }

    class AuthenticationInterceptor(private val apiKey: String?, private val secret: String?) : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val original = chain.request()
            val newRequestBuilder = original.newBuilder()
            val isApiKeyRequired = original.header(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_APIKEY) != null
            val isSignatureRequired = original.header(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED) != null
            newRequestBuilder.removeHeader(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_APIKEY)
                .removeHeader(BinanceApiConstants.ENDPOINT_SECURITY_TYPE_SIGNED)

            // Endpoint requires sending a valid API-KEY
            if (isApiKeyRequired || isSignatureRequired) {
                require(null !== apiKey) {
                    "apiKey required"
                }
                newRequestBuilder.addHeader(BinanceApiConstants.API_KEY_HEADER, apiKey!!)
            }

            // Endpoint requires signing the payload
            if (isSignatureRequired) {
                val payload = original.url().encodedQuery()
                if (null !== payload && payload.isNotEmpty()) {
                    val sig = sign(payload, secret!!)
                    val signedUrl = original.url().newBuilder().addQueryParameter("signature", sig).build()
                    newRequestBuilder.url(signedUrl)
                }
            }

            // Build new request after adding the necessary authentication information
            val newRequest = newRequestBuilder.build()
            return chain.proceed(newRequest)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || javaClass != other.javaClass) return false
            val that = other as AuthenticationInterceptor
            return apiKey == that.apiKey &&
                    secret == that.secret
        }

        override fun hashCode(): Int {
            return Objects.hash(apiKey, secret)
        }

        companion object {
            fun sign(message: String, secret: String): String {
                return try {
                    val sha256_HMAC = Mac.getInstance("HmacSHA256")
                    val secretKeySpec = SecretKeySpec(secret.toByteArray(), "HmacSHA256")
                    sha256_HMAC.init(secretKeySpec)
                    String(Hex.encodeHex(sha256_HMAC.doFinal(message.toByteArray())))
                } catch (e: Exception) {
                    throw RuntimeException("Unable to sign message.", e)
                }
            }
        }

    }
}

fun Headers.extractIpLimits(): List<BinanceApiService.ResponseWithIpLimits.Limit> {
    val ipLimits = toMultimap().filter { it.key.startsWith("x-mbx-used-weight-") }.map { map ->
        val interval = map.key.substringAfterLast("x-mbx-used-weight-")
        val intervalEnum = RateLimitIntervalEnum.values().find { it.headersRepresentation == interval }!!
        BinanceApiService.ResponseWithIpLimits.Limit(intervalEnum, map.value.first().toInt())
    }
    return ipLimits
}
