package com.abbosidev.domain.telegram

import com.abbosidev.infrastructure.util.removePrePlus
import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviour
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onContact
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onContentMessage
import dev.inmo.tgbotapi.extensions.utils.updates.retrieving.startGettingOfUpdatesByLongPolling
import dev.inmo.tgbotapi.types.message.abstracts.PrivateContentMessage
import dev.inmo.tgbotapi.types.message.content.TextContent
import io.quarkus.runtime.StartupEvent
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jboss.logging.Logger

@ApplicationScoped
class TelegramBotService(
    private val bot: TelegramBot,
    private val viewService: ViewService,
    private val logger: Logger,
) {

    fun startEventListening(
        @Observes event: StartupEvent,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val behaviour = bot.buildBehaviour {
                onCommand("start", initialFilter = { it is PrivateContentMessage }) {
                    viewService.startCommand(it.chat.id.chatId.long)
                }
                onContentMessage(initialFilter = { it is PrivateContentMessage }) {
                    logger.info("Received message: $it")
                    when (it) {
                        is PrivateContentMessage -> {
                            val text = (it.content as? TextContent)?.text
                            val chatId = it.chat.id.chatId.long
                            val messageId = it.messageId.long
                            if (!text.isNullOrBlank()) {
                                when (text) {
                                    Commands.TODAY -> {

                                    }

                                    Commands.LAST_TEN_DAYS -> {}
                                    Commands.ME -> {

                                    }
                                }
                            }
                        }

                        else -> {
                        }
                    }
                }
                onContact(initialFilter = { it is PrivateContentMessage }) {
                    val chatId = it.chat.id.chatId.long
                    val phone = it.content.contact.phoneNumber.removePrePlus()
                    val userId = it.content.contact.userId?.chatId?.long
                    val firstname = it.content.contact.firstName
                    val lastname = it.content.contact.lastName

                }
            }
            bot.startGettingOfUpdatesByLongPolling(
                updatesReceiver = behaviour.asUpdateReceiver,
                allowedUpdates = listOf("message"),
                exceptionsHandler = { logger.error("Error while handling telegram updates", it) },
            )
        }
    }
}
