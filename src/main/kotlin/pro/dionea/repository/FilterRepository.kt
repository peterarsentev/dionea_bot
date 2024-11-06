package pro.dionea.repository

import org.springframework.data.repository.CrudRepository
import pro.dionea.domain.Filter

interface FilterRepository : CrudRepository<Filter, Int>
