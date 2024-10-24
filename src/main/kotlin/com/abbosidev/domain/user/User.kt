package com.abbosidev.domain.user

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanionBase
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntityBase
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User(
    @Id
    val number: Int,
    val phone: String,
    var telegramId: Long? = null,
    val firstname: String? = null,
    val lastname: String? = null,
) : PanacheEntityBase {
    companion object : PanacheCompanionBase<User, Int>
}