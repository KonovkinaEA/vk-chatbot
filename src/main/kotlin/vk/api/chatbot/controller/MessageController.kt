package vk.api.chatbot.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import vk.api.chatbot.model.Incoming
import vk.api.chatbot.service.VkService

@RestController
@RequestMapping("/")
class MessageController(@Autowired val vkService: VkService) {

    @PostMapping
    fun send(@RequestBody incoming: Incoming): String {
        if (incoming.type == "message_new") vkService.sendMessage(
            incoming.`object`?.message?.text,
            incoming.`object`?.message?.peerId,
            incoming.v
        )
        return "ok"
    }
}
