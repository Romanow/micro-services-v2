package ru.romanow.inst.services.store.service

import ru.romanow.inst.services.store.domain.User
import java.util.*

interface UserService {
    fun getUserById(userId: UUID): User
    fun checkUserExists(userId: UUID): Boolean
}