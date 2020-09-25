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

package ru.h1karo.sharecontrol.i18n.init

import com.google.inject.Inject
import com.google.inject.name.Named
import ru.h1karo.sharecontrol.i18n.Locale
import ru.h1karo.sharecontrol.i18n.Resource
import ru.h1karo.sharecontrol.module.I18nModule
import java.io.File

class ResourceFinder @Inject constructor(
        @Named(I18nModule.MESSAGES_DIRECTORY)
        private val directory: File
) {
    fun find(): Set<Resource> {
        val directories = this.directory.listFiles()

        if (directories === null) {
            return emptySet()
        }

        return directories.flatMap { findLocaleResources(it) }.toSet()
    }

    private fun findLocaleResources(it: File): Set<Resource> {
        val locale = Locale(it.name)
        val files = it.listFiles()

        if (files === null) {
            return emptySet()
        }

        return files.map { Resource(locale, it.absolutePath, it.extension) }.toSet()
    }
}