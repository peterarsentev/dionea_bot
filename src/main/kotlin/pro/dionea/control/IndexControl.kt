package pro.dionea.control

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import pro.dionea.service.ChatService
import pro.dionea.service.FilterService
import pro.dionea.service.SpamService

@Controller
class IndexControl(val filterService: FilterService,
                   val spamService: SpamService,
    val chatService: ChatService) {

    @GetMapping(path = ["/", "/index"])
    fun indexPage(model : Model): String {
        model["chats"] = chatService.getAll()
        model["filters"] = filterService.getAll()
        model["spam"] = spamService.findTop10ByOrderByTimeDesc()
        return "index"
    }
}