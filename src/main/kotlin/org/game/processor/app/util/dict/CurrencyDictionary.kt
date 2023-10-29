package org.game.processor.app.util.dict

import org.game.processor.app.config.AppConfig
import org.springframework.stereotype.Component

@Component
class CurrencyDictionary(
    val dictFileReader: DictFileReader,
    val appConfig: AppConfig
) {
    val countryByNameMap by lazy {
        dictFileReader.readDictionary<List<CurrencyEntry>>(appConfig.dictPath).associateBy { it.name }
    }

    fun findByName(countryName: String): CurrencyEntry? {
        return countryByNameMap[countryName]
    }
}