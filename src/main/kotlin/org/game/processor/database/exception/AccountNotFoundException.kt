package org.game.processor.database.exception

class AccountNotFoundException(message: String, cause: Throwable? = null) :
        RuntimeException(message, cause)
