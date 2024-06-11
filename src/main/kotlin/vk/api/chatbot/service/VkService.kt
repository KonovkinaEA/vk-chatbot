package vk.api.chatbot.service

import org.slf4j.Logger
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import vk.api.chatbot.config.VkProperties
import vk.api.chatbot.model.Result
import kotlin.random.Random

@Service
class VkService(
    private val vkProperties: VkProperties,
    private val restTemplate: RestTemplate,
    private val logger: Logger,
) {
    fun sendMessage(text: String?, peerId: Long?, version: String?): Result {
        val url = UriComponentsBuilder.newInstance()
            .scheme("https")
            .host("api.vk.com")
            .path("/method/messages.send")
            .queryParam("peer_id", peerId)
            .queryParam("message", text?.let { "Вы сказали: $it" })
            .queryParam("v", version ?: vkProperties.version)
            .queryParam("random_id", Random.nextInt(100))
            .queryParam("access_token", vkProperties.token)
            .build()
            .toUriString()

        return try {
            logger.info("Sending message: $url")
            restTemplate.getForObject(url, Result::class.java) ?: Result(ERROR_CODE_UNKNOWN)
        } catch (e: RestClientException) {
            logger.error("Failed to send message: ${e.message}", e)
            Result(ERROR_CODE_INTERNAL_ERROR)
        }
    }

    companion object {
        const val ERROR_CODE_UNKNOWN = 104
        const val ERROR_CODE_INTERNAL_ERROR = 500
    }
}
