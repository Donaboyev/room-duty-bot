package com.abbosidev.domain.user

import com.abbosidev.infrastructure.util.withSession
import io.quarkus.vertx.ConsumeEvent
import io.smallrye.mutiny.coroutines.awaitSuspending
import io.smallrye.mutiny.vertx.UniHelper
import io.vertx.core.eventbus.EventBus
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class UserService(
    private val bus: EventBus,
) {
    suspend fun getUserByChatId(chatId: Long): User? =
        UniHelper.toUni(
            bus.request<User?>("get_user_by_chat_id", chatId)
                .map { it.body() }
        ).awaitSuspending()


    @ConsumeEvent("get_user_by_chat_id")
    fun getUserByChatIdEvent(chatId: Long) =
        withSession {
            User.find("telegramId", chatId).firstResult().awaitSuspending()
        }
}