package ru.romanow.inst.services.common.config

import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent
import org.springframework.context.ApplicationListener
import org.springframework.core.env.PropertiesPropertySource
import org.springframework.core.io.ClassPathResource
import java.io.IOException
import java.util.*

class ApplicationPropertyListener : ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    private val logger = LoggerFactory.getLogger(ApplicationPropertyListener::class.java)

    private val sources = arrayOf("application-common.properties")

    override fun onApplicationEvent(event: ApplicationEnvironmentPreparedEvent) {
        val environment = event.environment
        val props = Properties()
        for (source in sources) {
            val classPathResource = ClassPathResource(source)
            try {
                classPathResource.inputStream.use { stream ->
                    props.load(stream)
                    environment
                        .propertySources
                        .addLast(PropertiesPropertySource("props-${source}", props))
                }
            } catch (exception: IOException) {
                logger.error("", exception)
            }
        }
    }
}
