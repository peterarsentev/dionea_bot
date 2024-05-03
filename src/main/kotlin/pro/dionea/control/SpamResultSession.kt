package pro.dionea.control

import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.SessionScope
import pro.dionea.dto.SpamReason

@SessionScope
@Component
class SpamResultSession {
    var result: SpamReason? = null
    var text: String = ""
}