package pro.dionea.service

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.User
import pro.dionea.domain.Contact
import pro.dionea.repository.ContactRepository

@Service
class ContactService(val contactRepository: ContactRepository) {

    fun findIfNotCreate(user: User) : Contact
            = findByTgUserId(user.id)
        ?: save(
            Contact().apply {
                tgUserId = user.id
                username = user.userName ?: "unknown"
                firstName = user.firstName
                lastName = user.lastName ?: ""
            }
        )

    fun increaseCountOfMessages(contact: Contact, spam: Boolean) {
        if (spam) {
            contactRepository.increaseSpam(contact.id!!)
        } else {
            contactRepository.increaseHam(contact.id!!)
        }
    }

    fun save(contact: Contact) : Contact
            = contactRepository.save(contact)

    fun findByTgUserId(tgUserId: Long): Contact?
            = contactRepository.findByTgUserId(tgUserId)

    fun shouldBeBanned(tgUserId: Long): Boolean {
        val contact = findByTgUserId(tgUserId) ?: return false
        return contact.spam - contact.ham > 3
    }
}