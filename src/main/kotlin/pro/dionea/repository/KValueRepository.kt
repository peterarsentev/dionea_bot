package pro.dionea.repository

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import pro.dionea.domain.KValue

interface KValueRepository : CrudRepository<KValue, Int>{
    fun findByKeyIdIn(keys: List<Int>): List<KValue>

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update dionea_key_value set value=:pValue where id=:pId")
    fun update(@Param("pValue") value: String , @Param("pId") id: Int)
}