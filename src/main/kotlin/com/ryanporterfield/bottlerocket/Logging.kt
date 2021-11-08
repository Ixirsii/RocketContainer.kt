package com.ryanporterfield.bottlerocket

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Utility interface to "inject" a [Logger] into classes which need it.
 */
interface Logging {
    val log: Logger
}

/**
 * Implementation which injects the [Logger].
 */
class LoggingImpl(override val log: Logger) : Logging {
    companion object {
        inline operator fun <reified T> invoke(): LoggingImpl {
            return LoggingImpl(LoggerFactory.getLogger(T::class.java))
        }
    }
}
