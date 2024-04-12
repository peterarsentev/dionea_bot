package pro.dionea.repository

import org.springframework.test.fake.CrudRepositoryFake
import pro.dionea.domain.Filter

class FilterFakeRepository : CrudRepositoryFake<Filter, Int>(), FilterRepository
