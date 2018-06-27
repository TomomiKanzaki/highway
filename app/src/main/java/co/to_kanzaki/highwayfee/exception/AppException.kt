package co.to_kanzaki.highwayfee.exception

open class AppException(open val t: Throwable, open val innerMessage: String?, open val userMessage: String?): Exception() {
}