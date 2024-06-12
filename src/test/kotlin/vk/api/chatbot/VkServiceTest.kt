package vk.api.chatbot

import io.mockk.*
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.Logger
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import vk.api.chatbot.config.VkProperties
import vk.api.chatbot.model.Result
import vk.api.chatbot.service.VkService
import kotlin.random.Random

@ExtendWith(MockKExtension::class)
class VkServiceTest {

    private val vkProperties = mockk<VkProperties> {
        every { token } returns TOKEN
        every { version } returns DEFAULT_VERSION
    }
    private val expectedResult = mockk<Result>()
    private val restTemplate = mockk<RestTemplate> {
        every { getForObject(any<String>(), Result::class.java) } returns expectedResult
    }
    private val logger = mockk<Logger>(relaxed = true)

    private val vkService = VkService(vkProperties, restTemplate, logger)

    @BeforeEach
    fun setUp() {
        mockkObject(Random.Default)
        every { Random.nextInt(100) } returns RANDOM_INT
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `sendMessage should return result on success`() {
        val expectedUrl = "https://api.vk.com/method/messages.send?peer_id=$PEER_ID&message=Вы сказали: $TEXT&" +
                "v=$VERSION&random_id=$RANDOM_INT&access_token=$TOKEN"

        val result = vkService.sendMessage(TEXT, PEER_ID, VERSION)

        verify { logger.info("Sending message: $expectedUrl") }
        assertEquals(expectedResult, result)
    }

    @Test
    fun `sendMessage should return unknown error result when null is returned`() {
        val expectedUrl = "https://api.vk.com/method/messages.send?peer_id=$PEER_ID&message=Вы сказали: $TEXT&" +
                "v=$VERSION&random_id=$RANDOM_INT&access_token=$TOKEN"

        every { restTemplate.getForObject(any<String>(), Result::class.java) } returns null

        val result = vkService.sendMessage(TEXT, PEER_ID, VERSION)

        verify { logger.info("Sending message: $expectedUrl") }
        assertEquals(VkService.ERROR_CODE_UNKNOWN, result.response)
    }

    @Test
    fun `sendMessage should return internal error result on exception`() {
        val expectedUrl = "https://api.vk.com/method/messages.send?peer_id=$PEER_ID&message=Вы сказали: $TEXT&" +
                "v=$VERSION&random_id=$RANDOM_INT&access_token=$TOKEN"

        every {
            restTemplate.getForObject(
                expectedUrl,
                Result::class.java
            )
        } throws RestClientException("Test exception")

        val result = vkService.sendMessage(TEXT, PEER_ID, VERSION)

        verify { logger.info("Sending message: $expectedUrl") }
        verify { logger.error("Failed to send message: Test exception", any<RestClientException>()) }
        assertEquals(VkService.ERROR_CODE_INTERNAL_ERROR, result.response)
    }

    @Test
    fun `sendMessage should use default version from properties if not provided`() {
        val expectedUrl = "https://api.vk.com/method/messages.send?peer_id=$PEER_ID&message=Вы сказали: $TEXT&" +
                "v=$DEFAULT_VERSION&random_id=$RANDOM_INT&access_token=$TOKEN"

        val result = vkService.sendMessage(TEXT, PEER_ID, null)

        verify { logger.info("Sending message: $expectedUrl") }
        assertEquals(expectedResult, result)
    }

    @Test
    fun `sendMessage should not set query parameter for text when it is empty`() {
        val expectedUrl = "https://api.vk.com/method/messages.send?peer_id=$PEER_ID&message&" +
                "v=$VERSION&random_id=$RANDOM_INT&access_token=$TOKEN"

        val result = vkService.sendMessage(null, PEER_ID, VERSION)

        verify { logger.info("Sending message: $expectedUrl") }
        assertEquals(expectedResult, result)
    }

    companion object {
        private const val RANDOM_INT = 15
        private const val PEER_ID = 12345L
        private const val TEXT = "Hello"
        private const val VERSION = "5.199"
        private const val DEFAULT_VERSION = "5.131"
        private const val TOKEN = "token"
    }
}
