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
@RequestMapping("/filter")
class FilterControl(val filterService: FilterService,
                    val keyService: KeyService,
                    val kvalueService: KValueService,
                    val spamAnalysis: SpamAnalysis
) {

    @GetMapping("/view/{filterId}")
    fun viewPage(@PathVariable("filterId") filterId: Int, model: Model): String {
        model["filter"] = filterService.findById(filterId)
        val keys = keyService.findByFilterId(filterId)
        model["keys"] = keys
        model["kvalue"] = kvalueService
            .findInKeys(keys.map { it.id!! }.toList())
            .groupBy { it.key.id }
        return "filter/view"
    }

    @GetMapping("/create")
    fun createPage(): String {
        return "filter/create"
    }

    @PostMapping("/create")
    fun addFilter(@ModelAttribute filter: Filter): String {
        filterService.add(filter)
        return "redirect:/index"
    }
}