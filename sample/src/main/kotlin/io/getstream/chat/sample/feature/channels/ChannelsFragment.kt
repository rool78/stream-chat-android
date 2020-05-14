package io.getstream.chat.sample.feature.channels

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.getstream.sdk.chat.Chat
import com.getstream.sdk.chat.binding.bindView
import com.getstream.sdk.chat.viewmodel.ChannelsViewModelImpl
import io.getstream.chat.android.client.errors.ChatError
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.client.socket.InitConnectionListener
import io.getstream.chat.android.livedata.ChatDomain
import io.getstream.chat.sample.R
import io.getstream.chat.sample.common.image
import io.getstream.chat.sample.common.name
import io.getstream.chat.sample.data.user.UserRepository
import kotlinx.android.synthetic.main.fragment_channels.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class ChannelsFragment : Fragment(R.layout.fragment_channels) {

    private val userRepo: UserRepository by inject()
    private val viewModel: ChannelsViewModelImpl by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val loggedInUser = userRepo.user
        val user = User().apply {
            id = loggedInUser.id
            image = loggedInUser.image
            name = loggedInUser.name
        }

        ChatDomain.Builder(requireActivity().applicationContext, Chat.getInstance().client, user)
                .offlineEnabled()
                .userPresenceEnabled()
                .build()

        val client = Chat.getInstance().client
        client.setUser(user, loggedInUser.token, object : InitConnectionListener() {
            override fun onSuccess(data: ConnectionData) {
                Timber.d("Chat User set successfully")
            }
            override fun onError(error: ChatError) {
                Timber.e(error)
            }
        })

        viewModel.bindView(channelsList, this)
    }
}