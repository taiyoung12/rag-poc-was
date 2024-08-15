package com.rag.poc.message

import com.rag.poc.base.exception.BaseMsgType

enum class MsgType(
    private val code: String,
    private val key: String,
) : BaseMsgType {
    SUCCESS_REQUEST_LLM_MODEL("SUCCESS_REQUEST_LLM_MODEL", "답변 생성에 성공하였습니다."),

    DOES_NOT_EXIST_QUEUE("DOES_NOT_EXIST_QUEUE", "큐를 찾지 못하였습니다."),
    QUEUE_MANAGEMENT_ERROR("QUEUE_MANAGEMENT_ERROR", "큐 관리 중 오류가 발생하였습니다."),
    RABBITMQ_SEND_ERROR("RABBITMQ_SEND_ERROR", "RabbitMQ 전송 중 오류가 발생하였습니다."),
    RABBITMQ_RECEIVE_ERROR("RABBITMQ_RECEIVE_ERROR", "RabbitMQ 수신 중 오류가 발생하였습니다."),

    FAILED_TO_CREATE_LLM_MODEL("FAILED_TO_CREATE_LLM_MODEL", "LLM 모델 생성에 실패하였습니다."),
    ;

    override fun getCode(): String = this.code

    override fun getKey(): String = this.key
}
