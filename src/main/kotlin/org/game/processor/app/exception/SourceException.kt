package org.game.processor.app.exception

class SourceException(val callerFunction: String = "", val id: Long = 10, message: String ="", cause: Throwable? = null) :
        RuntimeException(message, cause)