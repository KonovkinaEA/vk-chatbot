package vk.api.chatbot.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "vk")
class VkProperties {
    lateinit var version: String
    lateinit var token: String
}
