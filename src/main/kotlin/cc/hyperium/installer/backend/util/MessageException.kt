package cc.hyperium.installer.backend.util

/**
 * Hacky way to make ui show a message from backend
 * because i cba to learn how to do that
 * properly
 */
class MessageException(message: String) : Exception(message) {
    override fun toString(): String {
        return message ?: error("Message is null? how")
    }
}