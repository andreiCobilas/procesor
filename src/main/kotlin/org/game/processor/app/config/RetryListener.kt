package org.game.processor.app.config

import org.slf4j.LoggerFactory
import org.springframework.retry.RetryCallback
import org.springframework.retry.RetryContext
import org.springframework.retry.listener.RetryListenerSupport
import org.springframework.stereotype.Component

@Component
internal class RetryListener : RetryListenerSupport() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun <T, E : Throwable?> onError(context: RetryContext, callback: RetryCallback<T, E>?, throwable: Throwable?) {
        logger.warn("An error occurred during request, retrying {}", context.retryCount)
        super.onError(context, callback, throwable)
    }
}