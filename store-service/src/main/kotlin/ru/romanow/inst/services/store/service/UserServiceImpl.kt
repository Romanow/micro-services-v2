package ru.romanow.inst.services.store.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.romanow.inst.services.store.domain.User
import ru.romanow.inst.services.store.repository.UserRepository
import java.util.*
import javax.persistence.EntityNotFoundException

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
): UserService {

    @Transactional(readOnly = true)
    override fun getUserById(userId: UUID): User {
        return userRepository.findByUid(userId)
            .orElseThrow { EntityNotFoundException("User with id '$userId' not found") }
    }

    @Transactional(readOnly = true)
    override fun checkUserExists(userId: UUID): Boolean {
        return userRepository.findByUid(userId).isPresent
    }
}