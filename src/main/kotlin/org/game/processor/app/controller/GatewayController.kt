package org.game.processor.app.controller

import org.game.processor.app.exception.InvalidOperation
import org.game.processor.app.exception.UnauthorizedException
import org.game.processor.app.model.response.OperationResponse
import org.game.processor.app.model.request.Request
import org.game.processor.app.model.response.BalanceResponse
import org.game.processor.app.service.GatewayService
import org.game.processor.app.service.TokenValidationService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/open-api-games/v1/games-processor")
class GatewayController(
        private val gatewayService: GatewayService,
        private val tokenValidationService: TokenValidationService,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/balance")
    fun balance(@RequestBody request: Request): BalanceResponse{
        if (tokenValidationService.isTokenValid(request)) {
            throw UnauthorizedException(request.token)
        }
        return gatewayService.balance(request) ?: throw InvalidOperation(
            "Obtained null for /balance and token: ${request.token}")
    }

    @PostMapping("/debit")
    fun debit(@RequestBody request: Request): OperationResponse {
        if (tokenValidationService.isTokenValid(request)) {
            throw UnauthorizedException(request.token)
        }
        return gatewayService.debit(request)
    }

    @PostMapping("/credit")
    fun credit(@RequestBody request: Request): OperationResponse {
        if (tokenValidationService.isTokenValid(request)) {
            throw UnauthorizedException(request.token)
        }
        return gatewayService.credit(request)
    }

    @ExceptionHandler(UnauthorizedException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleUnauthorizedException(ex: UnauthorizedException): ResponseEntity<Any?> {
        logger.error("Unauthorized request for token: {}", ex.message)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null)
    }

    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    fun handleInvalidOperationException(ex: InvalidOperation): ResponseEntity<Any?> {
        logger.error("Invalid operation with error: {}", ex.message)
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(null)
    }
}