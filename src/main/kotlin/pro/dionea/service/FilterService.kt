package pro.dionea.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pro.dionea.domain.Filter
import pro.dionea.repository.FilterRepository

@Service
class FilterService(val filterRepository: FilterRepository) {
    fun getAll(): List<Filter>
            = filterRepository.findAll().toList()

    fun add(filter: Filter) {
        filterRepository.save(filter)
    }

    fun findById(filterId: Int): Filter?
    = filterRepository.findByIdOrNull(filterId)
}