package pro.dionea.control

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import pro.dionea.domain.KValue
import pro.dionea.service.KeyService
import pro.dionea.service.KValueService

@Controller
@RequestMapping("/kvalue")
class KValueControl(val keyService: KeyService,
                    val kvalueService: KValueService) {
    @GetMapping("/create/{keyId}")
    fun createPage(@PathVariable("keyId") keyId: Int, model: Model): String {
        model["key"] = keyService.findById(keyId)
        return "kvalue/create"
    }

    @PostMapping("/create/{keyId}")
    fun add(@PathVariable("keyId") keyId: Int,
            @ModelAttribute kvalue: KValue): String {
        val key = keyService.findById(keyId)
        kvalue.key.id = keyId
        kvalueService.add(kvalue)
        return "redirect:/filter/view/${key!!.filter.id}"
    }

    @GetMapping("/update/{id}")
    fun updatePage(@PathVariable("id") id: Int, model: Model): String {
        model["kvalue"] = kvalueService.findById(id)
        return "kvalue/update"
    }

    @PostMapping("/update/{id}")
    fun update(@PathVariable("id") id: Int,
            @ModelAttribute kvalue: KValue): String {
        val kval = kvalueService.findById(id)
        kval!!.value = kvalue.value
        kvalueService.update(kvalue)
        return "redirect:/filter/view/${kval.key.filter.id}"
    }
}