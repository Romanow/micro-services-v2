package ru.romanow.inst.services.store.domain

import java.util.*
import javax.persistence.*


@Entity
@Table(name = "users", indexes = [
    Index(name = "idx_user_name", columnList = "name", unique = true),
    Index(name = "idx_user_uid", columnList = "uid", unique = true)
])
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    @Column(nullable = false, unique = true)
    val name: String,

    @Column(nullable = false, length = 40, unique = true)
    val uid: UUID
)
