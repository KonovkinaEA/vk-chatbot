package vk.api.chatbot.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Message(
    @JsonProperty("peer_id") val peerId: Long?,
    val text: String?
)
