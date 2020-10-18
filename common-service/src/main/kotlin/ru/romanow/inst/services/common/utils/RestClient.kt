package ru.romanow.inst.services.common.utils

import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.util.*
import java.util.Optional.ofNullable

typealias Mapper<R> = (body: String) -> R

class RestClient(private val restTemplate: RestTemplate) {
    private val logger = LoggerFactory.getLogger(RestClient::class.java)

    fun <RESP> get(url: String, responseClass: Class<RESP>): RequestBuilder<RESP> =
        RequestBuilder(HttpMethod.GET, url, null, responseClass)

    fun <RESP> post(url: String, requestBody: Any?, responseClass: Class<RESP>): RequestBuilder<RESP> =
        RequestBuilder(HttpMethod.POST, url, requestBody, responseClass)

    fun <RESP> delete(url: String, responseClass: Class<RESP>): RequestBuilder<RESP> =
        RequestBuilder(HttpMethod.DELETE, url, null, responseClass)

    fun <RESP> patch(url: String, requestBody: Any?, responseClass: Class<RESP>): RequestBuilder<RESP> =
        RequestBuilder(HttpMethod.PATCH, url, requestBody, responseClass)


    inner class RequestBuilder<RESP>(
        private val method: HttpMethod,
        private val url: String,
        private val requestBody: Any? = null,
        private val responseClass: Class<RESP>
    ) {
        private var commonExceptionMapping: Mapper<out RuntimeException>? = null
        private var exceptionMapping: MutableMap<HttpStatus, Mapper<RuntimeException>> = EnumMap(HttpStatus::class.java)

        fun exceptionMapping(status: HttpStatus, mapper: Mapper<out RuntimeException>) =
            apply { this.exceptionMapping[status] = mapper }

        fun commonExceptionMapping(mapper: Mapper<out RuntimeException>) =
            apply { this.commonExceptionMapping = mapper }

        fun execute(): Optional<RESP> {
            try {
                val response = restTemplate.exchange(buildRequest(), responseClass)
                return ofNullable(response.body)
            } catch (exception: HttpStatusCodeException) {
                val status = exception.statusCode
                val reason = exception.statusText

                logger.error("Request to '$url' failed with client error: $status:$reason")

                when {
                    exceptionMapping.containsKey(status) -> throw exceptionMapping[status]!!.invoke(exception.responseBodyAsString)
                    commonExceptionMapping != null -> throw commonExceptionMapping!!.invoke(exception.responseBodyAsString)
                    else -> throw exception
                }
            }
        }

        private fun buildRequest(): RequestEntity<*> {
            val uri = UriComponentsBuilder.fromHttpUrl(url).build().toUri()
            val request = RequestEntity.method(method, uri)
            return if (requestBody != null) {
                request
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
            } else {
                request.build()
            }
        }
    }
}