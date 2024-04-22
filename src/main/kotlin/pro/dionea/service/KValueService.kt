package pro.dionea.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pro.dionea.domain.KValue
import pro.dionea.repository.KValueRepository

@Service
class KValueService(val kvalueRepository: KValueRepository) {
    fun getAll(): List<KValue>
            = kvalueRepository.findAll().toList()

    fun add(kvalue: KValue) : KValue
            = kvalueRepository.save(kvalue)

    fun findInKeys(keys: List<Int>): List<KValue>
            = kvalueRepository.findByKeyIdIn(keys)

    fun findById(id: Int): KValue?
            = kvalueRepository.findByIdOrNull(id)

    fun update(kvalue: KValue)
            = kvalueRepository.update(kvalue.value, kvalue.id!!)

    fun findByKeyFilterId(filterId: Int): List<KValue>
            = kvalueRepository.findByKeyFilterId(filterId)

    fun findByKeyId(keyId: Int): Set<String>
            = kvalueRepository.findByKeyId(keyId)
        .map { it.value }
        .toSet()

    fun deleteById(id: Int) {
        kvalueRepository.deleteById(id)
    }
}