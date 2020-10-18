package ru.romanow.inst.services.common.utils

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.spi.StackTraceElementProxy
import ch.qos.logback.classic.spi.ThrowableProxyUtil
import ch.qos.logback.classic.util.LevelToSyslogSeverity
import ch.qos.logback.core.CoreConstants
import ch.qos.logback.core.LayoutBase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.slf4j.MDC
import java.util.*
import kotlin.collections.HashMap

class GraylogLayout : LayoutBase<ILoggingEvent>() {
    private val gson: Gson = GsonBuilder().create()
    private val hostName: String = NetworkUtils.hostName
    var applicationName: String? = null

    override fun doLayout(event: ILoggingEvent): String {
        val map: MutableMap<String, Any?> = HashMap()
        map["version"] = "1.1"
        map["level"] = LevelToSyslogSeverity.convert(event)
        map["level_name"] = event.level.levelStr
        map["host"] = hostName
        map["application_name"] = applicationName
        val message: String
        val detailedMessage: String
        val throwableProxy = event.throwableProxy
        if (throwableProxy != null) {
            message = throwableProxy.message
            detailedMessage = buildStackTrace(throwableProxy.stackTraceElementProxyArray)
        } else {
            message = event.formattedMessage
            detailedMessage = event.formattedMessage
        }
        map["message"] = message
        map["full_message"] = detailedMessage
        map["timestamp"] = event.timeStamp
        map["_thread_name"] = event.threadName
        map["_logger_name"] = event.loggerName
        Optional.ofNullable(MDC.getCopyOfContextMap()).ifPresent { m: Map<String, String?>? -> map.putAll(m!!) }
        val stackTrace = event.callerData
        if (stackTrace != null && stackTrace.isNotEmpty()) {
            val stackElement = stackTrace[0]
            map["_class_name"] = stackElement.className
            map["_file_name"] = stackElement.fileName
            map["line_number"] = stackElement.lineNumber
        }
        return "${gson.toJson(map)}\n"
    }

    private fun buildStackTrace(traceElements: Array<StackTraceElementProxy>): String {
        val builder = StringBuilder()
        for (step in traceElements) {
            builder.append(CoreConstants.TAB).append(step.toString())
            ThrowableProxyUtil.subjoinPackagingData(builder, step)
            builder.append(CoreConstants.LINE_SEPARATOR)
        }
        return builder.toString()
    }
}
