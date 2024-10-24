package com.abbosidev.domain.telegram

import com.abbosidev.domain.user.DutyService
import com.abbosidev.domain.user.User
import com.abbosidev.domain.user.UserService
import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.send.sendMessage
import dev.inmo.tgbotapi.extensions.api.send.setMessageReaction
import dev.inmo.tgbotapi.extensions.utils.types.buttons.replyKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.requestContactButton
import dev.inmo.tgbotapi.extensions.utils.types.buttons.simpleButton
import dev.inmo.tgbotapi.types.MessageId
import dev.inmo.tgbotapi.types.message.MarkdownParseMode
import dev.inmo.tgbotapi.types.message.textsources.mention
import dev.inmo.tgbotapi.types.toChatId
import dev.inmo.tgbotapi.utils.buildEntities
import dev.inmo.tgbotapi.utils.row
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty

@ApplicationScoped
class ViewService(
    private val bot: TelegramBot,
    private val userService: UserService,
    private val dutyService: DutyService,
    @ConfigProperty(name = "telegram.group.id", defaultValue = "0")
    private val groupId: Long,
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
        val duty = dutyService.getTodaysDuty()
        bot.sendMessage(
            chatId.toChatId(),
            "Bugun ${duty.date}  \n" +
                    "_${duty.user.firstname} ${duty.user.lastname}_ning navbatchilik kuni",
            MarkdownParseMode
        )
    }

    suspend fun getLastTenDaysDuties(chatId: Long) {
        val duties = dutyService.getLastTenDaysDuties(chatId)
        val message = duties.joinToString("\n\n") {
            "*${it.date}* -> _${it.user.firstname} ${it.user.lastname}_ning navbatchilik kuni"
        }
        bot.sendMessage(
            chatId.toChatId(),
            message,
            MarkdownParseMode
        )
    }

    suspend fun getMyDutyDate(chatId: Long) {
        val date = dutyService.getMyDutyDate(chatId)
        bot.sendMessage(
            chatId.toChatId(),
            "Sizning navbatchilik kuningiz: _${date}_",
            MarkdownParseMode
        )
    }

    suspend fun yourDutyIsToday(user: User, nextUser: User) {
        bot.sendMessage(
            user.telegramId!!.toChatId(),
            "Bugun sizning navbatchilik kuningiz, ${user.firstname} ${user.lastname}!",
        )
//        bot.sendMessage(
//            groupId.toChatId(),
//            buildEntities {
//                "Bugun " + mention(
//                    "${user.firstname} ${user.lastname}",
//                    user.telegramId!!.toChatId()
//                ) + "ning navbatchilik kuni. \n" +
//                        "Ertaga " + mention(
//                    "${nextUser.firstname} ${nextUser.lastname}",
//                    nextUser.telegramId!!.toChatId()
//                ) + "ning navbatchilik kuni"
//            }
//        )
    }

}