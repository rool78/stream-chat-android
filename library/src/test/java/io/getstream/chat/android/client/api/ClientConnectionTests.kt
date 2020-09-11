package io.getstream.chat.android.client.api

import android.content.Context
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.ChatClientImpl
import io.getstream.chat.android.client.errors.ChatError
import io.getstream.chat.android.client.events.ConnectedEvent
import io.getstream.chat.android.client.events.DisconnectedEvent
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.logger.ChatLogger
import io.getstream.chat.android.client.models.EventType
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.client.notifications.ChatNotifications
import io.getstream.chat.android.client.notifications.handler.ChatNotificationHandler
import io.getstream.chat.android.client.parser.ChatParserImpl
import io.getstream.chat.android.client.socket.ChatSocket
import io.getstream.chat.android.client.socket.InitConnectionListener
import io.getstream.chat.android.client.token.FakeTokenManager
import io.getstream.chat.android.client.utils.UuidGeneratorImpl
import io.getstream.chat.android.client.utils.observable.JustObservable
import io.getstream.chat.android.client.utils.safeArgThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import java.util.Date

internal class ClientConnectionTests {

    val userId = "test-id"
    val connectionId = "connection-id"
    val user = User().apply { id = userId }
    val token = "token"
    val context = mock(Context::class.java)

    val config = ChatClientConfig(
        "api-key",
        "hello.http",
        "cdn.http",
        "socket.url",
        1000,
        1000,
        false,
        ChatLogger.Config(ChatLogLevel.NOTHING, null),
        ChatNotificationHandler(context),
        FakeTokenManager(token)
    )

    val connectedEvent = ConnectedEvent(
        EventType.HEALTH_CHECK,
        Date(),
        user,
        connectionId
    )

    lateinit var api: ChatApi
    lateinit var socket: ChatSocket
    lateinit var retrofitApi: RetrofitApi
    lateinit var retrofitCdnApi: RetrofitCdnApi
    lateinit var client: ChatClient
    lateinit var logger: ChatLogger
    lateinit var notificationsManager: ChatNotifications
    lateinit var initConnectionListener: InitConnectionListener
    lateinit var observable: JustObservable

    @Before
    fun before() {
        socket = mock(ChatSocket::class.java)
        retrofitApi = mock(RetrofitApi::class.java)
        retrofitCdnApi = mock(RetrofitCdnApi::class.java)
        logger = mock(ChatLogger::class.java)
        notificationsManager = mock(ChatNotifications::class.java)
        initConnectionListener = mock(InitConnectionListener::class.java)
        api = ChatApiImpl(
            config.apiKey,
            retrofitApi,
            retrofitCdnApi,
            ChatParserImpl(),
            UuidGeneratorImpl()
        )
        observable = JustObservable(DisconnectedEvent(EventType.CONNECTION_DISCONNECTED, Date()))
        `when`(socket.events()).thenReturn(observable)

        client = ChatClientImpl(
            config,
            api,
            socket,
            notificationsManager
        )
    }

    @Test
    fun successConnection() {
        client.setUser(user, token)

        verify(socket, times(1)).connect(user)
    }

    @Test
    fun `Should not connect and report error when user is already set`() {
        observable.subscription.onNext(connectedEvent)

        client.setUser(user, token, initConnectionListener)

        verify(socket, never()).connect(user)

        val error = ChatError("User cannot be set until previous one is disconnected.")
        verify(initConnectionListener).onError(safeArgThat(error, { it.message == error.message }))
    }

    @Test
    fun connectAndDisconnect() {
        client.setUser(user, token)

        client.disconnect()

        verify(socket, times(1)).disconnect()
    }
}
