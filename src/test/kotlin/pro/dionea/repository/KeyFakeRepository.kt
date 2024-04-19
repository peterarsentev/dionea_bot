package pro.dionea.repository

import org.springframework.test.fake.CrudRepositoryFake
import pro.dionea.domain.Filter
import pro.dionea.domain.Key

class KeyFakeRepository : CrudRepositoryFake<Key, Int>(), KeyRepository {
    override fun findByFilterId(filterId: Int): List<Key>
            = memory.filter { it.value.filter.id == filterId }
        .map { it.value }
        .toList()
}
