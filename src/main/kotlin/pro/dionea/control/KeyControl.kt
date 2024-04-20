package pro.dionea.control

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import pro.dionea.domain.Filter
import pro.dionea.domain.Key
import pro.dionea.service.FilterService
import pro.dionea.service.KeyService

@Controller
@RequestMapping("/key")
class KeyControl(val filterService: FilterService, val keyService: KeyService) {

    @GetMapping("/create/{filterId}")
    fun viewPage(@PathVariable("filterId") filterId: Int, model: Model): String {
        model["filter"] = filterService.findById(filterId)
        return "/key/create"
    }

    @PostMapping("/create/{filterId}")
    fun add(@PathVariable("filterId") filterId: Int, @ModelAttribute key: Key): String {
        key.filter.id = filterId
        keyService.add(key)
        return "redirect:/filter/view/$filterId"
    }
}