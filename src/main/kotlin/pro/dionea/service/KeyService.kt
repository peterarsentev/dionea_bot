package pro.dionea.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pro.dionea.domain.Filter
import pro.dionea.domain.Key
import pro.dionea.repository.KeyRepository

@Service
class KeyService(val keyRepository: KeyRepository) {
    fun getAll(): List<Key>
            = keyRepository.findAll().toList()

    fun add(key: Key) {
        keyRepository.save(key)
    }

    fun findByFilterId(filterId: Int): List<Key>
            = keyRepository.findByFilterId(filterId)

    fun findById(keyId: Int): Key?
            = keyRepository.findByIdOrNull(keyId)
}