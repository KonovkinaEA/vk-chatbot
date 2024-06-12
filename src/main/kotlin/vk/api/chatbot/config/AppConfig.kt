package vk.api.chatbot.config

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class AppConfig {
    @Bean
    fun restTemplate() = RestTemplate()

    @Bean
    fun logger() = LoggerFactory.getLogger("VkChatbotLogger")
}
