package pro.dionea.control

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import pro.dionea.service.*


@Controller
class IndexControl(val filterService: FilterService,
                   val spamService: SpamService,
                   val spamAnalysis: SpamAnalysis,
                   val chatService: ChatService,
                   val spamResult: SpamResultSession
) {

    @GetMapping(path = ["/", "/index"])
    fun indexPage(model : Model): String {
        model["text"] = spamResult.text
        model["result"] = spamResult.result
        model["chats"] = chatService.getAll()
        model["filters"] = filterService.getAll()
        model["spam"] = spamService.findTop10ByOrderByTimeDesc()
        return "index"
    }

    @PostMapping("/check")
    fun checkPage(@RequestParam("text") text: String, model: Model): String {
        spamResult.text = text
        spamResult.result = spamAnalysis.isSpam(text)
        return "redirect:/index"
    }

}