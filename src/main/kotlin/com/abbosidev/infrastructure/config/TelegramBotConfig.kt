package com.abbosidev.infrastructure.config

import dev.inmo.tgbotapi.bot.TelegramBot
import dev.inmo.tgbotapi.extensions.api.telegramBot
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import org.eclipse.microprofile.config.inject.ConfigProperty

@ApplicationScoped
class TelegramBotConfig(
    @ConfigProperty(name = "telegram.bot.token")
    private val token: String,
) {
    @Produces
    fun bot(): TelegramBot = telegramBot(token)
}
