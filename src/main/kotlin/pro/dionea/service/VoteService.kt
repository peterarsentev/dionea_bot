package pro.dionea.service

import org.springframework.stereotype.Service
import pro.dionea.domain.Vote
import pro.dionea.repository.VoteRepository

@Service
class VoteService(val voteRepository: VoteRepository) {
    fun save(vote: Vote) {
        voteRepository.save(vote.chatId!!, vote.messageId!!, vote.userId!!, vote.vote)
    }

    fun findByMessageId(messageId: Long): List<Vote>
            = voteRepository.findByMessageId(messageId)
}
