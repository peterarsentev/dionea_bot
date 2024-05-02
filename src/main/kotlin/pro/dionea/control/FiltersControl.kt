package pro.dionea.control

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import pro.dionea.domain.Filter
import pro.dionea.service.FilterService
import pro.dionea.service.KValueService
import pro.dionea.service.KeyService
import pro.dionea.service.SpamAnalysis

@Controller
@RequestMapping("/filters")
class FiltersControl(val filterService: FilterService) {
    @GetMapping("/")
    fun viewPage(model: Model): String {
        model["filters"] = filterService.getAll()
        return "filters/view"
    }
}