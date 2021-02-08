package io.getstream.chat.docs.kotlin

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.getstream.sdk.chat.viewmodel.ChannelHeaderViewModel
import io.getstream.chat.android.ui.channel.list.header.ChannelListHeaderView
import io.getstream.chat.android.ui.channel.list.header.viewmodel.ChannelListHeaderViewModel
import io.getstream.chat.android.ui.channel.list.header.viewmodel.bindView
import io.getstream.chat.android.ui.messages.header.MessagesHeaderView
import io.getstream.chat.android.ui.messages.header.bindView

class Android {

    /**
     * @see <a href="https://getstream.io/chat/docs/android/channel_list_header_view">Channel List Header View</a>
     */
    public class ChannelListHeader(private val channelListHeaderView: ChannelListHeaderView) : Fragment() {

        fun bindingWithViewModel() {
            // Get ViewModel
            val viewModel: ChannelListHeaderViewModel by viewModels()
            // Bind it with ChannelListHeaderView
            viewModel.bindView(channelListHeaderView, viewLifecycleOwner)
        }
    }

    /**
     * @see <a href="https://getstream.io/chat/docs/android/messages_header_view">Messages Header View</a>
     */
    public class MessageListHeader(private val messagesHeaderView: MessagesHeaderView) : Fragment() {

        fun bindingWithViewModel() {
            // Get ViewModel
            val viewModel: ChannelHeaderViewModel by viewModels()
            // Bind it with MessagesHeaderView
            viewModel.bindView(messagesHeaderView, viewLifecycleOwner)
        }
    }
}
