package com.abbosidev.domain.user

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanionBase
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntityBase
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "users",
    uniqueConstraints = [UniqueConstraint(columnNames = ["phone", "member", "telegram_id"])],
)
class User(
    @Enumerated(EnumType.STRING)
    val member: Member,
    @Id
    val phone: String,
    val telegramId: Long? = null,
) : PanacheEntityBase {
    companion object : PanacheCompanionBase<User, String>
}

enum class Member {
    SARVAR,
    JAVOHIR,
    DILMUROD,
    ABBOS,
    AMIRXON,
    NIZOMIDDIN,
}