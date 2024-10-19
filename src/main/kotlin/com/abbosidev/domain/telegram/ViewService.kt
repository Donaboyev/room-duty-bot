package com.abbosidev.domain.telegram

import com.abbosidev.domain.user.UserService
import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.extensions.api.send.setMessageReaction
import dev.inmo.tgbotapi.extensions.utils.types.buttons.replyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.requestContactButton
import dev.inmo.tgbotapi.extensions.utils.types.buttons.simpleButton
import dev.inmo.tgbotapi.types.MessageId
import dev.inmo.tgbotapi.types.message.MarkdownParseMode
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
            bot.sendMessage(
                chatId.toChatId(),
                "Salom, ${user.firstname} ${user.lastname}! \uD83D\uDE0A",
                replyMarkup = replyKeyboard(resizeKeyboard = true) {
                    row {
                        simpleButton(Commands.TODAY)
                        simpleButton(Commands.ME)
                    }
                    row { simpleButton(Commands.LAST_TEN_DAYS) }
                }
            )
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

    suspend fun sendOwnContact(chatId: Long, messageId: MessageId) {
        bot.setMessageReaction(
            chatId.toChatId(),
            messageId,
            "\uD83D\uDE21"
        )
        bot.sendMessage(
            chatId.toChatId(),
            "Iltimos, o'zingizni kontaktingizni yuboring!!!",
            replyMarkup = replyKeyboard(resizeKeyboard = true) {
                row {
                    requestContactButton("Kontaktim")
                }
            }
        )
    }

    suspend fun linkContact(chatId: Long, phone: String, messageId: MessageId) {
        val user = userService.getUserByPhone(phone)
        if (user != null) {
            userService.saveUserChatId(user, chatId)
            bot.setMessageReaction(
                chatId.toChatId(),
                messageId,
                "\uD83C\uDF89"
            )
            bot.sendMessage(
                chatId.toChatId(),
                "Salom, ${user.firstname} ${user.lastname}! \uD83D\uDE0A",
                replyMarkup = replyKeyboard(resizeKeyboard = true) {
                    row {
                        simpleButton(Commands.TODAY)
                        simpleButton(Commands.ME)
                    }
                    row { simpleButton(Commands.LAST_TEN_DAYS) }
                }
            )
        } else {
            bot.setMessageReaction(
                chatId.toChatId(),
                messageId,
                "\uD83E\uDD14"
            )
            bot.sendMessage(
                chatId.toChatId(),
                "Siz shu kvartirada turasizmi o'zi?  \n" +
                        "Boshqa raqamda urinib ko'ring",
                MarkdownParseMode,
            )
        }
    }

    suspend fun getTodaysDuty(chatId: Long) {

    }
}