package pro.dionea.repository

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import pro.dionea.domain.Contact

interface ContactRepository : CrudRepository<Contact, Int>{
    fun findByUsername(username: String): Contact?

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update dionea_contact set spam = spam + 1 where id=:cId")
    fun increaseSpam(@Param("cId") id: Int)

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update dionea_contact set ham = ham + 1 where id = :cId")
    fun increaseHam(@Param("cId") id: Int)
}