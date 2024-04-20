package pro.dionea.control

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import pro.dionea.service.FilterService

@Controller
class IndexControl(val filterService: FilterService) {

    @GetMapping(path = ["/", "/index"])
    fun indexPage(model : Model): String {
        model["filters"] = filterService.getAll()
        return "/index"
    }
}