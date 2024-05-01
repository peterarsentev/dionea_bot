package pro.dionea.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pro.dionea.domain.User
import pro.dionea.repository.RoleRepository
import pro.dionea.repository.UserRepository

@Service
class UserService(val userRepository: UserRepository,
                  val roleRepository: RoleRepository) {

    fun add(user: User): User {
        user.role = roleRepository.findByName("ROLE_USER")!!
        return userRepository.save(user)
    }

    fun findById(userId: Int): User?
            = userRepository.findByIdOrNull(userId)
}