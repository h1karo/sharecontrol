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

package ru.h1karo.sharecontrol.i18n

import com.google.inject.Inject
import com.google.inject.Singleton
import ru.h1karo.sharecontrol.i18n.exception.CatalogueNotFoundException
import ru.h1karo.sharecontrol.i18n.exception.I18nException
import ru.h1karo.sharecontrol.i18n.exception.MessageNotFoundException
import ru.h1karo.sharecontrol.configuration.plugin.Locale as LocaleParameter

@Singleton
class FallbackTranslator @Inject constructor(
    private val translator: MutableTranslatorInterface
) : FallbackTranslatorInterface, MutableTranslatorInterface {
    override fun trans(id: String, parameters: Collection<Any>, locale: Locale?): String {
        return try {
            this.translator.trans(id, parameters, locale)
        } catch (e: I18nException) {
            if (e !is CatalogueNotFoundException && e !is MessageNotFoundException) {
                throw e
            }

            this.translator.trans(id, parameters, this.getFallbackLocale())
        }
    }

    override fun addResource(resource: Resource) = this.translator.addResource(resource)

    override fun getLocale(): Locale = this.translator.getLocale()

    override fun setLocale(locale: Locale) = this.translator.setLocale(locale)

    override fun getFallbackLocale(): Locale = LocaleParameter.getDefault()

    override fun clear() = this.translator.clear()
}
