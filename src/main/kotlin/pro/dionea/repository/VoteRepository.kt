package pro.dionea.repository

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import pro.dionea.domain.Filter
import pro.dionea.domain.Vote

interface VoteRepository : CrudRepository<Vote, Int> {

    fun findByMessageId(messageId: Long): List<Vote>

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(nativeQuery = true, value =
        """
       INSERT INTO dionea_vote(chat_id, message_id, user_id, vote) 
       VALUES (:pChatId, :pMessageId, :pUserId, :pVote)
       ON CONFLICT (chat_id, message_id, user_id) DO
       UPDATE SET vote = :pVote
    """)
    fun save(
        @Param("pChatId") chatId: Long, @Param("pMessageId") messageId: Long,
        @Param("pUserId") userId: Long, @Param("pVote") vote: Int
    )
}