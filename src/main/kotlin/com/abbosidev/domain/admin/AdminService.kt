package com.abbosidev.domain.admin

import com.abbosidev.domain.telegram.ViewService
import com.abbosidev.domain.user.DutyService
import com.abbosidev.domain.user.UserService
import com.abbosidev.infrastructure.util.withTransaction
import io.quarkus.vertx.ConsumeEvent
import io.smallrye.mutiny.Uni
import io.vertx.core.eventbus.EventBus
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class AdminService(
    private val bus: EventBus,
    private val viewService: ViewService,
    private val dutyService: DutyService,
    private val userService: UserService,
) {
    suspend fun sendNotificationMessage(): Boolean {
        bus.publish("send_notification_message_event", Any())
        return true
    }

    @ConsumeEvent("send_notification_message_event")
    fun sendNotificationMessageEvent(any: Any): Uni<Void> =
        withTransaction {
            val duty = dutyService.getTodaysDuty()
            val nextNumber = if (duty.user.number == 6) 1 else duty.user.number + 1
            val nextUser = userService.getUserByNumber(nextNumber)
            viewService.yourDutyIsToday(duty.user, nextUser)
        }.replaceWithVoid()
}