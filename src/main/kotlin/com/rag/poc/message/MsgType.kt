package com.rag.poc.message

import com.rag.poc.base.exception.BaseMsgType

enum class MsgType(
    private val code: String,
    private val key: String,
) : BaseMsgType {
    SUCCESS_REQUEST_LLM_MODEL("SUCCESS_REQUEST_LLM_MODEL", "답변 생성에 성공하였습니다."),
    ;

    override fun getCode(): String = this.code

    override fun getKey(): String = this.key
}
