package vk.api.chatbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import vk.api.chatbot.config.VkProperties

@SpringBootApplication
@EnableConfigurationProperties(VkProperties::class)
class VkChatbotApplication

fun main(args: Array<String>) {
    runApplication<VkChatbotApplication>(*args)
}
