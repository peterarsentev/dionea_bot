package pro.dionea.repository

import org.springframework.data.repository.CrudRepository
import pro.dionea.domain.User

interface UserRepository : CrudRepository<User, Int>{
}