package com.rag.poc.rabbitmq.queue

import com.rag.poc.message.MsgType
import com.rag.poc.message.RagException
import org.springframework.amqp.AmqpConnectException
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.stereotype.Service

@Service
class QueueManagement(
    private val rabbitAdmin: RabbitAdmin,
) {
    fun createRagQueue(): Queue {
        val queueName = QueueName.RAG.name
        val queue = Queue(queueName, true)
        rabbitAdmin.declareQueue(queue)
        return queue
    }

    fun getQueue(queueName: QueueName): Queue {
        return try {
            if (isQueueExists(queueName.name)) {
                Queue(queueName.name, true)
            } else {
                throw RagException.withType(MsgType.DOES_NOT_EXIST_QUEUE)
            }
        } catch (e: AmqpConnectException) {
            throw e
        }
    }

    fun isQueueExists(queueName: String): Boolean {
        val properties = rabbitAdmin.getQueueProperties(queueName)
        return properties.isNotEmpty()
    }
}
