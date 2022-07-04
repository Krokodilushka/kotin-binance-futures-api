import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

object JsonToObject {

    private var mapper = ObjectMapper().registerKotlinModule()

    fun <T> convert(json: String?, clazz: Class<T>): T = mapper.readValue(json, clazz)

    fun <T> convert(json: String?, clazz: TypeReference<T>): T = mapper.readValue(json, clazz)
}