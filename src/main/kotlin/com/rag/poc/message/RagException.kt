package com.rag.poc.message

import com.rag.poc.base.exception.BaseException
import com.rag.poc.base.exception.BaseMsgType

class RagException(
    type: BaseMsgType,
    exception: Throwable,
    data: Any? = null,
    params: Array<Any>? = null,
) : BaseException(
        type,
        exception,
        data,
    ) {
    companion object {
        fun withType(type: BaseMsgType): RagException = RagException(type, RuntimeException())
    }
}
