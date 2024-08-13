package com.rag.poc.base.response

import com.rag.poc.base.exception.BaseException
import com.rag.poc.base.exception.BaseMsgType

class Exception(
    type: BaseMsgType,
    exception: Throwable,
    data: Any? = null,
) : BaseException(
        type,
        exception,
        data,
    ) {
    companion object {
        fun with(type: BaseMsgType): Exception =
            Exception(
                type,
                RuntimeException(),
                null,
            )
    }
}
