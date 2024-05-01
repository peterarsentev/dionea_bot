package pro.dionea.repository

import org.springframework.data.repository.CrudRepository
import pro.dionea.domain.Role

interface RoleRepository : CrudRepository<Role, Int>{
    fun findByName(name: String): Role?
}