package com.rag.poc.rabbitmq.message

data class RagMessage(
    val keyword: String,
    val prompt: String,
)
