package pro.dionea.control

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import pro.dionea.service.FilterService

@Controller
@RequestMapping("/filters")
class FiltersControl(val filterService: FilterService) {
    @GetMapping("/")
    fun viewPage(model: Model): String {
        model["filters"] = filterService.getAll()
        return "filters/view"
    }
}
