package com.rag.poc.base.exception

import com.rag.poc.base.response.Response

open class BaseException(
    type: BaseMsgType,
    e: Throwable,
    data: Any? = null,
) : RuntimeException() {
    val body: Response<Any?> = Response.create(type, data)

    init {
        super.initCause(e)
    }
}
