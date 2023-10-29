package org.game.processor.app.util.dict

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Service
import java.io.FileNotFoundException

@Service
class DictFileReader(
    val resourceLoader: ResourceLoader,
    val objectMapper: ObjectMapper,
) {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun <T : Any> readDictionary(dictPath: String, type: TypeReference<T>): T {
        val stream = try {
            resourceLoader.getResource("file:$dictPath").inputStream
        } catch (err: FileNotFoundException) {
            resourceLoader.getResource("classpath:$dictPath").inputStream
        }

        val readValues = objectMapper.readValue(stream, type)
        logger.info("Dictionary with path: $dictPath loaded successfully!")

        return readValues
    }
}

inline fun <reified T : Any> DictFileReader.readDictionary(s: String): T =
    this.readDictionary(s, object : TypeReference<T>() {})