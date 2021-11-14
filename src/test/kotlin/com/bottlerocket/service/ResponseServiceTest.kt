package com.bottlerocket.service

import com.bottlerocket.module.httpClient
import com.bottlerocket.util.fullContainer
import io.ktor.application.ApplicationCall
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.SendCountExceedException
import io.ktor.client.features.ServerResponseException
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.utils.io.ByteReadChannel
import io.mockk.coJustRun
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class ResponseServiceTest {
    private val statusCapture = slot<HttpStatusCode>()

    @MockK
    private lateinit var call: ApplicationCall

    @MockK
    private lateinit var containerService: ContainerService

    @InjectMockKs
    private lateinit var underTest: ResponseService

    init {
        mockkStatic("io.ktor.response.ApplicationResponseFunctionsKt")
    }

    @Test
    fun `GIVEN success WHEN listContainers THEN responds containers`() {
        // Given
        every { containerService.listContainers() } returns listOf(fullContainer)
        coJustRun { call.respond(any()) }

        // When
        runBlocking { underTest.listContainers(call) }

        // Then
        verify(exactly = 1) {
            containerService.listContainers()
        }
        confirmVerified(containerService)
    }

    @Test
    fun `GIVEN ClientRequestException WHEN listContainers THEN responds BadRequest`() {
        // Given
        val exception: ClientRequestException = getClientRequestException()

        every { containerService.listContainers() } throws exception
        coJustRun { call.respondText(any(), status = capture(statusCapture)) }

        // When
        runBlocking { underTest.listContainers(call) }

        // Then
        verify(exactly = 1) {
            containerService.listContainers()
        }
        confirmVerified(containerService)

        assertEquals(HttpStatusCode.BadRequest, statusCapture.captured, "HTTP status code should equal expected")
    }

    @Test
    fun `GIVEN RedirectResponseException WHEN listContainers THEN responds BadGateway`() {
        // Given
        val exception: RedirectResponseException = getRedirectResponseException()

        mockkStatic("io.ktor.response.ApplicationResponseFunctionsKt")
        every { containerService.listContainers() } throws exception
        coJustRun { call.respondText(any(), status = capture(statusCapture)) }

        // When
        runBlocking { underTest.listContainers(call) }

        // Then
        verify(exactly = 1) {
            containerService.listContainers()
        }
        confirmVerified(containerService)

        assertEquals(HttpStatusCode.BadGateway, statusCapture.captured, "HTTP status code should equal expected")
    }

    @Test
    fun `GIVEN ServerResponseException WHEN listContainers THEN responds BadGateway`() {
        // Given
        val exception: ServerResponseException = getServerResponseException()

        mockkStatic("io.ktor.response.ApplicationResponseFunctionsKt")
        every { containerService.listContainers() } throws exception
        coJustRun { call.respondText(any(), status = capture(statusCapture)) }

        // When
        runBlocking { underTest.listContainers(call) }

        // Then
        verify(exactly = 1) {
            containerService.listContainers()
        }
        confirmVerified(containerService)

        assertEquals(HttpStatusCode.BadGateway, statusCapture.captured, "HTTP status code should equal expected")
    }

    /* ********************************************************************************************************** *
     *                                             Private utility functions                                      *
     * ********************************************************************************************************** */

    private fun getClientRequestException(): ClientRequestException = runBlocking {
        val mockEngine = MockEngine {
            respond(
                content = ByteReadChannel(""),
                status = HttpStatusCode.NotFound
            )
        }
        val client: HttpClient = httpClient(mockEngine)

        try {
            client.get("https://anywebsite.com")
        } catch (e: ClientRequestException) {
            return@runBlocking e
        }
    }

    private fun getRedirectResponseException(): RedirectResponseException = runBlocking {
        val mockEngine = MockEngine {
            respond(
                content = ByteReadChannel(""),
                status = HttpStatusCode.UseProxy
            )
        }
        val client: HttpClient = httpClient(mockEngine)

        try {
            client.get("https://anywebsite.com")
        } catch (e: RedirectResponseException) {
            return@runBlocking e
        }
    }

    private fun getServerResponseException(): ServerResponseException = runBlocking {
        val mockEngine = MockEngine {
            respond(
                content = ByteReadChannel(""),
                status = HttpStatusCode.InternalServerError
            )
        }
        val client: HttpClient = httpClient(mockEngine)

        try {
            client.get("https://anywebsite.com")
        } catch (e: ServerResponseException) {
            return@runBlocking e
        }
    }
}
