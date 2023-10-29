package org.game.processor.app.service

import org.game.processor.app.config.AppConfig
import org.game.processor.app.model.request.Request

import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory
import org.springframework.util.DigestUtils

@Service
class TokenValidationService(private val appConfig: AppConfig) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun isTokenValid(request: Request): Boolean{
        val encryptRequestBodyWithSecret = encryptRequestBodyWithSecret(request.data.toString())
        return   (appConfig.encryptionFlag && request.token != encryptRequestBodyWithSecret)
    }

    private fun encryptRequestBodyWithSecret(input: String): String {
        try {
            return DigestUtils.md5DigestAsHex((input + appConfig.secret).toByteArray())
        } catch (err: Exception) {
            logger.error(
                    "Failed to encrypt input {}, with error message: {}",
                    input,
                    err.message
            )
            throw err
        }
    }
}