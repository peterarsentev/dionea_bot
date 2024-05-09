package pro.dionea.service

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import pro.dionea.domain.Contact
import pro.dionea.repository.ContactRepository

@Service
class ContactService(val contactRepository: ContactRepository) {

    fun findIfNotCreate(message: Message) : Contact {
        val name = message.from.userName ?: "unknown"
        return findByName(name)
            ?: add(
                Contact().apply {
                    tgUserId = message.from.id
                    username = name
                    firstName = message.from.firstName
                    lastName = message.from.lastName ?: ""
                }
            )
    }

    fun increaseCountOfMessages(contact: Contact, spam: Boolean) {
        if (spam) {
            contactRepository.increaseSpam(contact.id!!)
        } else {
            contactRepository.increaseHam(contact.id!!)
        }
    }

    fun add(contact: Contact) : Contact = contactRepository.save(contact)

    fun findByName(username: String): Contact?
            = contactRepository.findByUsername(username)
}