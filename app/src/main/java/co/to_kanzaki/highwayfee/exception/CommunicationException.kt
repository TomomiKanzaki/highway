package co.to_kanzaki.highwayfee.exception

class CommunicationException(override val t: Throwable, override val innerMessage: String?, override val userMessage: String?)
    : AppException(t, innerMessage, userMessage) {
}