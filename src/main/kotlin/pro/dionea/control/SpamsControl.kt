package pro.dionea.control

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.*
import pro.dionea.domain.Filter
import pro.dionea.service.SpamService

@Controller
@RequestMapping("/spams")
class SpamsControl(val spamService: SpamService) {
    @GetMapping("/view")
    fun viewPage(model: Model): String {
        model["spams"] = spamService.findAllByOrderByTimeDesc()
        return "spams/view"
    }
}