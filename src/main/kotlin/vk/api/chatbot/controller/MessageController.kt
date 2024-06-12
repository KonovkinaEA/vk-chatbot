package vk.api.chatbot.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import vk.api.chatbot.config.VkProperties
import vk.api.chatbot.model.Incoming
import vk.api.chatbot.service.VkService

@RestController
@RequestMapping("/")
class MessageController(val vkService: VkService, private val vkProperties: VkProperties) {

    @PostMapping
    fun send(@RequestBody incoming: Incoming): String {
        when (incoming.type) {
            "message_new" -> vkService.sendMessage(
                incoming.`object`?.message?.text,
                incoming.`object`?.message?.peerId,
                incoming.v
            )
            "confirmation" -> return vkProperties.confirmation
        }

        return "ok"
    }
}
