package com.abbosidev.domain.admin

import com.abbosidev.domain.telegram.ViewService
import com.abbosidev.domain.user.Duty
import com.abbosidev.domain.user.User
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.vertx.core.eventbus.EventBus
import jakarta.enterprise.context.ApplicationScoped
import java.time.LocalDate

@ApplicationScoped
class AdminService(
    private val bus: EventBus,
    private val viewService: ViewService,
) {
    suspend fun sendNotificationMessage(): Boolean {
        bus.publish("check_duties_order", Any())
        val duty = Duty.find("date", LocalDate.now()).firstResult().awaitSuspending()
        val nextNumber = if (duty!!.user.number == 6) 1 else duty.user.number + 1
        val nextUser = User.findById(nextNumber).awaitSuspending()
        viewService.yourDutyIsToday(duty.user, nextUser!!)
        return true
    }
}