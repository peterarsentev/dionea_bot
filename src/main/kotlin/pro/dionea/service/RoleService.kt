package pro.dionea.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pro.dionea.domain.Role
import pro.dionea.repository.RoleRepository

@Service
class RoleService(val roleRepository: RoleRepository) {
    fun findByName(name: String): Role?
            = roleRepository.findByName(name)
}