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

package ru.h1karo.sharecontrol.file.reader

import com.google.inject.Inject
import com.google.inject.Singleton
import ru.h1karo.sharecontrol.file.exception.NotFoundReaderException

@Singleton
class Reader @Inject constructor(private val readers: Set<ReaderInterface>) : ReaderInterface {
    override fun read(resource: Any, format: String): Map<String, Any> {
        val reader = this.getReader(resource, format)
        return reader.read(resource, format)
    }

    private fun getReader(resource: Any, format: String): ReaderInterface {
        val reader = this.readers.find { it.supports(resource, format) }
        if (reader === null) {
            throw NotFoundReaderException(resource, format)
        }

        return reader
    }

    override fun supports(resource: Any, format: String): Boolean {
        return this.readers.any { it.supports(resource, format) }
    }
}