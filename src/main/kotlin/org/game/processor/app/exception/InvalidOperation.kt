package org.game.processor.app.exception

class InvalidOperation(message: String, cause: Throwable? = null) :
    RuntimeException(message, cause)