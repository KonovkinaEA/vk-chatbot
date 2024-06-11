package vk.api.chatbot.service

import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import vk.api.chatbot.config.VkProperties
import vk.api.chatbot.model.Incoming
import vk.api.chatbot.model.Result
import kotlin.random.Random

@Service
class VkService(
    @Autowired val vkProperties: VkProperties,
    @Autowired val restTemplate: RestTemplate,
    @Autowired val logger: Logger
) {

    fun sendMessage(incoming: Incoming): Result {
        val message = "Вы сказали: ${incoming.`object`?.message?.text ?: ""}"
        val peerId = incoming.`object`?.message?.peer_id ?: 0L
        val version = incoming.v ?: vkProperties.version
        val url = "https://api.vk.com/method/messages.send?v=$version&peer_id=$peerId&message=$message&random_id=" +
                "${Random.nextInt(100)}&access_token=${vkProperties.token}"

        return try {
            logger.info("Sending message: $url")
            restTemplate.getForObject(url, Result::class.java) ?: Result(104)
        } catch (e: RestClientException) {
            logger
            Result(500)
        }
    }
}