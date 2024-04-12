package pro.dionea.repository

import org.springframework.test.fake.CrudRepositoryFake
import pro.dionea.domain.KValue

class KValueFakeRepository : CrudRepositoryFake<KValue, Int>(), KValueRepository {
    override fun findByKeyIdIn(keys: List<Int>): List<KValue>
            = memory.filter { keys.contains(it.value.key.id) }
        .map { it.value }
        .toList()

    override fun update(value: String, id: Int) {
        TODO("Not yet implemented")
    }

    override fun findByKeyFilterId(filterId: Int): List<KValue>
            = memory.filter { it.value.key.filter.id == filterId }
        .map { it.value }
        .toList()

    override fun findByKeyId(keyId: Int): List<KValue>
            = memory.filter { it.value.key.id == keyId }
        .map { it.value }
        .toList()
}
