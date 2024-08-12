package com.rag.poc.conifg

import feign.Request
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class FeignConfig {
    @Bean
    fun requestOptions(): Request.Options {
        return Request.Options(
            Duration.ofSeconds(60),
            Duration.ofSeconds(60),
            false,
        )
    }
}
