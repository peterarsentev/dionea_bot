package pro.dionea.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pro.dionea.domain.Filter
import pro.dionea.domain.Spam
import pro.dionea.repository.FilterRepository
import pro.dionea.repository.SpamRepository

@Service
class SpamService(val spamRepository: SpamRepository) {
    fun getAll(): List<Spam>
            = spamRepository.findAll().toList()

    fun add(spam: Spam) {
        spamRepository.save(spam)
    }

    fun findById(spamId: Int): Spam?
    = spamRepository.findByIdOrNull(spamId)
}