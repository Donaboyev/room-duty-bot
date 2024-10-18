package com.abbosidev.domain.user

import com.abbosidev.infrastructure.util.withSession
import com.abbosidev.infrastructure.util.withTransaction
import io.quarkus.vertx.ConsumeEvent
import io.smallrye.mutiny.Uni
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

    suspend fun getUserByPhone(phone: String): User? =
        UniHelper.toUni(
            bus.request<User?>("get_user_by_phone", phone)
                .map { it.body() }
        ).awaitSuspending()

    suspend fun saveUserChatId(user: User, chatId: Long) {
        bus.publish("save_user_chat_id", chatId to user.number)
    }

    @ConsumeEvent("get_user_by_chat_id")
    fun getUserByChatIdEvent(chatId: Long) =
        withSession {
            User.find("telegramId", chatId).firstResult().awaitSuspending()
        }

    @ConsumeEvent("get_user_by_phone")
    fun getUserByPhoneEvent(phone: String) =
        withSession {
            User.find("phone", phone).firstResult().awaitSuspending()
        }

    @ConsumeEvent("save_user_chat_id")
    fun saveUserChatIdEvent(info: Pair<Long, Int>): Uni<Void> =
        withTransaction {
            User.update("telegramId = ?1 where number = ?2", info.first, info.second).awaitSuspending()
        }.replaceWithVoid()
}