package model.websocket

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

sealed class WebSocketMessage {

    @JsonDeserialize(using = Wrapper.Deserializer::class)
    data class Wrapper<T : Wrapper.Response>(
        val response: T
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        abstract class Response {
            data class Result(
                @JsonProperty("result")
                val result: List<String>?,
                @JsonProperty("id")
                val id: Int
            ) : Response()

            @JsonIgnoreProperties(ignoreUnknown = true)
            data class Error(
                @JsonProperty("error")
                val error: Error,
                @JsonProperty("id")
                val id: Int
            ) : Response() {
                @JsonIgnoreProperties(ignoreUnknown = true)
                data class Error(
                    @JsonProperty("code")
                    val code: Int,
                    @JsonProperty("msg")
                    val msg: String
                ) : Response()
            }
        }

        class Deserializer : JsonDeserializer<Wrapper<Response>>() {
            override fun deserialize(jp: JsonParser, ctx: DeserializationContext): Wrapper<Response>? {
                val node = jp.codec.readTree<JsonNode>(jp)
                val json = node.toString()
                node["result"]?.let {
                    val result = JsonToObject.convert(json, Response.Result::class.java)
                    return Wrapper(response = result)
                }
                node["error"]?.let {
                    val result = JsonToObject.convert(json, Response.Error::class.java)
                    return Wrapper(response = result)
                }
                return null
            }
        }
    }

    data class Request(
        @JsonProperty("method")
        val method: Method,
        @JsonProperty("params")
        val params: List<String>,
        @JsonProperty("id")
        val id: Int
    ) : WebSocketMessage()


    enum class Method {
        SUBSCRIBE, UNSUBSCRIBE, LIST_SUBSCRIPTIONS, SET_PROPERTY, GET_PROPERTY
    }
}