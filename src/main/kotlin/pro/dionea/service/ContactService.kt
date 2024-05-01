package pro.dionea.service

import org.springframework.stereotype.Service
import pro.dionea.domain.Contact
import pro.dionea.repository.ContactRepository

@Service
class ContactService(val contactRepository: ContactRepository) {

    fun add(contact: Contact) : Contact = contactRepository.save(contact)

    fun findByName(username: String): Contact?
            = contactRepository.findByUsername(username)
}