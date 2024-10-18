package com.abbosidev.domain.user

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import jakarta.persistence.Entity
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "duties")
class Duty(
    @OneToOne
    val user: User,
    val date: LocalDate = LocalDate.now(),
) : PanacheEntity() {
    companion object : PanacheCompanion<Duty>
}