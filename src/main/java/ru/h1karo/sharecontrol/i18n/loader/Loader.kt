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

package ru.h1karo.sharecontrol.i18n.loader

import com.google.inject.Inject
import com.google.inject.Singleton
import ru.h1karo.sharecontrol.i18n.Locale
import ru.h1karo.sharecontrol.i18n.MessageCatalogue
import ru.h1karo.sharecontrol.i18n.exception.NotFoundLoaderException

@Singleton
class Loader @Inject constructor(private val loaders: Set<LoaderInterface>) : LoaderInterface {
    override fun load(resource: Any, locale: Locale): MessageCatalogue {
        val loader = this.loaders.find { it.supports(resource) }
        if (loader === null) {
            throw NotFoundLoaderException(resource)
        }

        return loader.load(resource, locale)
    }

    override fun supports(resource: Any): Boolean {
        return this.loaders.any { it.supports(resource) }
    }
}