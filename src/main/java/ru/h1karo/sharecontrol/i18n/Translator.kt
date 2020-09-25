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
import ru.h1karo.sharecontrol.i18n.exception.MessageNotFoundException
import ru.h1karo.sharecontrol.i18n.format.MessageFormatterInterface
import ru.h1karo.sharecontrol.i18n.loader.LoaderInterface

@Singleton
class Translator @Inject constructor(
        private val locale: Locale,
        private val formatter: MessageFormatterInterface,
        private val loader: LoaderInterface
) : TranslatorInterface {
    private val catalogues: MutableMap<Locale, MessageCatalogue> = mutableMapOf()
    private val resources: MutableMap<Locale, MutableSet<Resource>> = mutableMapOf()

    override fun trans(id: String, parameters: Set<String>, locale: Locale?): String {
        val message = getMessage(id, locale)
        return this.formatter.format(message, parameters)
    }

    override fun getLocale(): Locale {
        return this.locale
    }

    fun addResource(resource: Resource) {
        this.resources.getOrPut(resource.locale, { mutableSetOf() }).add(resource)
        this.catalogues.remove(resource.locale)
    }

    private fun getMessage(id: String, locale: Locale? = null): String {
        val catalogue = this.getCatalogue(locale)
        return catalogue.messages.getOrElse(id) { throw MessageNotFoundException(id) }
    }

    private fun getCatalogue(locale: Locale? = null): MessageCatalogue {
        val targetLocale: Locale = locale ?: this.locale
        return this.catalogues.getOrElse(targetLocale) { this.loadCatalogue(targetLocale) }
    }

    private fun loadCatalogue(locale: Locale): MessageCatalogue {
        val resources = this.resources[locale]
        if (resources === null) {
            throw CatalogueNotFoundException(locale)
        }

        val catalogue = resources.fold(
                MessageCatalogue(locale),
                { acc, resource -> acc.addCatalogue(this.loader.load(resource)) }
        )
        this.catalogues[locale] = catalogue

        return catalogue
    }
}
