package com.abbosidev.domain.user

import io.quarkus.panache.common.Sort
import io.smallrye.mutiny.coroutines.awaitSuspending
import jakarta.enterprise.context.ApplicationScoped
import java.time.LocalDate

@ApplicationScoped
class DutyService {
    suspend fun getTodaysDuty(chatId: Long) {
        val lastDuty = Duty.find("date < ?1", Sort.descending("date"), LocalDate.now())
            .firstResult().awaitSuspending()
    }
}