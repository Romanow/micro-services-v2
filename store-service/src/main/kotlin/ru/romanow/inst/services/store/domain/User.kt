package ru.romanow.inst.services.store.domain

import com.google.common.base.Objects
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "users", indexes = [
    Index(name = "idx_user_name", columnList = "name", unique = true),
    Index(name = "idx_user_user_uid", columnList = "user_uid", unique = true)
])
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column(nullable = false, unique = true)
    var name: String? = null,

    @Column(name = "user_uid", nullable = false, length = 40, unique = true)
    var userUid: UUID? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        return Objects.equal(name, other.name) &&
            Objects.equal(userUid, other.userUid)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(name, userUid)
    }

    override fun toString(): String {
        return "User(id=$id, name=$name, userUid=$userUid)"
    }
}
