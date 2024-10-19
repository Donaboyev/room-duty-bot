package com.abbosidev.domain.user

import com.abbosidev.infrastructure.util.withTransaction
import io.quarkus.panache.common.Page
import io.quarkus.panache.common.Sort
import io.quarkus.vertx.ConsumeEvent
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.smallrye.mutiny.vertx.UniHelper
import io.vertx.core.eventbus.EventBus
import jakarta.enterprise.context.ApplicationScoped
import java.time.LocalDate

@ApplicationScoped
class DutyService(
    private val bus: EventBus,
) {
    suspend fun getTodaysDuty(chatId: Long): Duty =
        UniHelper.toUni(
            bus.request<Duty>("get_todays_duty", chatId)
                .map { it.body() }
        ).awaitSuspending()

    suspend fun getLastTenDaysDuties(chatId: Long): List<Duty> =
        UniHelper.toUni(
            bus.request<List<Duty>>("get_last_ten_days_duties", chatId)
                .map { it.body() }
        ).awaitSuspending()

    @ConsumeEvent("get_todays_duty")
    fun getTodaysDutyEvent(chatId: Long) =
        withTransaction {
            val today = LocalDate.now()
            val lastDuty = Duty.find("date <= ?1", Sort.descending("date"), LocalDate.now())
                .firstResult().awaitSuspending()
            val lastDate = lastDuty!!.date
            val diff = today.dayOfYear - lastDate.dayOfYear
            if (diff > 0) {
                var userNumber = lastDuty.user.number
                for (i in 1..diff) {
                    userNumber++
                    if (userNumber > 6) {
                        userNumber = 1
                    }
                    Duty(
                        User.find("number", userNumber).firstResult().awaitSuspending()!!,
                        lastDate.plusDays(i.toLong())
                    ).persist<Duty>().awaitSuspending()
                }
            }
            Duty.find("date = ?1", Sort.descending("date"), today)
                .firstResult().awaitSuspending()
        }

    @ConsumeEvent("get_last_ten_days_duties")
    fun getLastTenDaysDutiesEvent(chatId: Long) =
        withTransaction {
            Duty.find("date < ?1",Sort.descending("date"), LocalDate.now())
                .page(Page.ofSize(10)).list().awaitSuspending()
        }
}