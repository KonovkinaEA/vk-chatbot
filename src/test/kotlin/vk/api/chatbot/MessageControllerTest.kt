package vk.api.chatbot

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import vk.api.chatbot.controller.MessageController
import vk.api.chatbot.model.Incoming
import vk.api.chatbot.model.Message
import vk.api.chatbot.model.Object
import vk.api.chatbot.service.VkService

@ExtendWith
class MessageControllerTest {

    private val vkService = mockk<VkService>(relaxed = true)

    private val messageController = MessageController(vkService)
    private val mockMvc = MockMvcBuilders.standaloneSetup(messageController).build()

    private val mapper = jacksonObjectMapper()

    @Test
    fun `should not call vkService sendMessage when type is not message_new`() {
        val incoming = Incoming(
            type = "message_new",
            `object` = Object(
                message = Message(
                    text = "Hello",
                    peerId = 12345
                )
            ),
            v = "5.131"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(incoming.copy(type = "other_type")))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("ok"))

        verify(exactly = 0) { vkService.sendMessage(any(), any(), any()) }
    }

    @ParameterizedTest
    @MethodSource("incomingObjects")
    fun `should handle null values in incoming object`(incoming: Incoming) {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(incoming))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("ok"))

        verify {
            vkService.sendMessage(
                incoming.`object`?.message?.text,
                incoming.`object`?.message?.peerId,
                incoming.v
            )
        }
    }

    companion object {
        @JvmStatic
        fun incomingObjects() = listOf(
            Incoming(type = "message_new", `object` = null, v = "5.199"),
            Incoming(type = "message_new", `object` = Object(message = null), v = "5.199"),
            Incoming(
                type = "message_new",
                `object` = Object(message = Message(text = null, peerId = null)),
                v = "5.199"
            ),
            Incoming(
                type = "message_new",
                `object` = Object(message = Message(text = "Hello", peerId = null)),
                v = "5.199"
            ),
            Incoming(
                type = "message_new",
                `object` = Object(message = Message(text = null, peerId = 12345)),
                v = "5.199"
            ),
            Incoming(
                type = "message_new",
                `object` = Object(message = Message(text = "Hello", peerId = 12345)),
                v = "5.199"
            )
        )
    }
}
