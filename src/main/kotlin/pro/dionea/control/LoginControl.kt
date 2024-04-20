package pro.dionea.control

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LoginControl {
    @GetMapping("/login")
    fun loginPage(): String {
        return "/login"
    }
}