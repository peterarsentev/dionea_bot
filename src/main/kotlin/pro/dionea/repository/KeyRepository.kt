package pro.dionea.repository

import org.springframework.data.repository.CrudRepository
import pro.dionea.domain.Key

interface KeyRepository : CrudRepository<Key, Int>{
    fun findByFilterId(filterId: Int): List<Key>
}