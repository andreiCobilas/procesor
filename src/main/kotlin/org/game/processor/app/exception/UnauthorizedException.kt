package org.game.processor.app.exception

class UnauthorizedException(message: String, cause: Throwable? = null) :
        RuntimeException(message, cause)
