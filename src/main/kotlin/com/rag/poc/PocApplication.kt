package com.rag.poc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableFeignClients(basePackages = ["com.rag.poc.util"])
class PocApplication

fun main(args: Array<String>) {
    runApplication<PocApplication>(*args)
}
