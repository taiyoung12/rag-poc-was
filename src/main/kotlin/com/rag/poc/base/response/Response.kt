package com.rag.poc.base.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.rag.poc.base.exception.BaseMsgType

data class Response<T>(
    @JsonProperty("code") val code: String,
    @JsonProperty("message") val message: String = "",
    @JsonProperty("data") val data: T?,
    @JsonProperty("key") val key: String,
) {
    companion object {
        fun <T> create(
            msgType: BaseMsgType,
            data: T,
        ): Response<T> =
            Response(
                code = msgType.getCode(),
                message = msgType.getKey(),
                data = data,
                key = msgType.getKey(),
            )
    }
}
