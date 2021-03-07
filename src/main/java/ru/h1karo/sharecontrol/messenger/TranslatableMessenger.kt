/*
 * This file is a part of ShareControl.
 *
 * ShareControl is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ShareControl is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ShareControl. If not, see <https://www.gnu.org/licenses/>.
 *
 * @copyright Copyright (c) 2020 ShareControl
 * @author Oleg Kozlov <h1karo@outlook.com>
 * @license GNU General Public License v3.0
 * @link https://github.com/h1karo/sharecontrol
 */

package ru.h1karo.sharecontrol.messenger

import com.google.inject.Inject
import ru.h1karo.sharecontrol.i18n.TranslatorInterface
import ru.h1karo.sharecontrol.i18n.exception.I18nException
import java.text.MessageFormat

class TranslatableMessenger @Inject constructor(
    private val messenger: Messenger,
    private val translator: TranslatorInterface
) : Messenger {
    override fun send(recipient: Any, message: String, parameters: Collection<Any>) {
        try {
            val translated = this.translate(message, parameters)
            this.messenger.send(recipient, translated)
        } catch (e: I18nException) {
            this.messenger.send(recipient, message, parameters)
        }
    }

    private fun translate(message: String, parameters: Collection<Any>): String {
        return try {
            this.translator.trans(message, parameters)
        } catch (e: I18nException) {
            val keys = this.findTranslationKeys(message)
            if (keys.isEmpty()) {
                throw e
            }

            val replacePairs = keys.map { key -> Pair(MessageFormat.format(KEY_FORMAT, key), this.translator.trans(key)) }
            replacePairs.fold(message, { translated, pair -> translated.replace(pair.first, pair.second) })
        }
    }

    private fun findTranslationKeys(message: String): Collection<String> {
        return KEY_PATTERN
            .findAll(message)
            .map { it.groupValues[1] }
            .toSet()
    }

    companion object {
        private const val KEY_FORMAT = "\$'{'{0}'}'"
        private val KEY_PATTERN = Regex("\\\$\\{([a-z]+(\\.[a-z]+)?)}")
    }
}
