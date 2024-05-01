package pro.dionea.repository

import org.springframework.data.repository.CrudRepository
import pro.dionea.domain.Contact

interface ContactRepository : CrudRepository<Contact, Int>{
    fun findByUsername(username: String): Contact?
}