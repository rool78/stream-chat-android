package io.getstream.chat.android.livedata.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.getstream.chat.android.livedata.entity.ReactionEntity


@Dao
interface ReactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reactionEntity: ReactionEntity)

    @Query(
        "SELECT * FROM stream_chat_reaction " +
                "WHERE stream_chat_reaction.syncStatus IN (-1, 2)"
    )
    suspend fun selectSyncNeeded(): List<ReactionEntity>

    @Query(
        "SELECT * FROM stream_chat_reaction " +
                "WHERE stream_chat_reaction.messageid = :messageId AND userId = :userId AND type = :type"
    )
    suspend fun select(messageId: String, userId: String, type: String): ReactionEntity?


}