package com.abbosidev.domain.user

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanionBase
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntityBase
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "duties")
class Duty(
    @OneToOne
    val user: User,
    @Id
    val date: LocalDate = LocalDate.now(),
) : PanacheEntityBase {
    companion object : PanacheCompanionBase<Duty, LocalDate>
}