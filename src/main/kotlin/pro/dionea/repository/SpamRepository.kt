package pro.dionea.repository

import org.springframework.data.repository.CrudRepository
import pro.dionea.domain.Spam

interface SpamRepository : CrudRepository<Spam, Int>{
}