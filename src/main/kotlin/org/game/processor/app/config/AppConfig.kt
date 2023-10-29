package org.game.processor.app.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig(
    @Value("\${app.secret}") val secret: String,
    @Value("\${encryption.enabled.flag}") val encryptionFlag: Boolean,
    @Value("\${database.base_url}") val sourceUrl: String,
    @Value("\${app.dictionary.path}") val dictPath: String
)
