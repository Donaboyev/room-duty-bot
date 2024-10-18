package com.abbosidev.domain.telegram

import com.abbosidev.domain.user.UserService
import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.extensions.utils.types.buttons.replyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.requestContactButton
import dev.inmo.tgbotapi.types.toChatId
import dev.inmo.tgbotapi.utils.row
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class ViewService(
    private val bot: TelegramBot,
    private val userService: UserService,
) {
    suspend fun startCommand(chatId: Long) {
        val user = userService.getUserByChatId(chatId)
        if (user != null) {

        } else {
            bot.sendMessage(
                chatId.toChatId(),
                "Iltimos, kontaktingizni yuboring",
                replyMarkup = replyKeyboard(resizeKeyboard = true) {
                    row {
                        requestContactButton("Kontaktim")
                    }
                }
            )
        }
    }
}