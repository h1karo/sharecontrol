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

package ru.h1karo.sharecontrol.file.writer

import com.google.inject.Inject
import com.google.inject.Singleton
import ru.h1karo.sharecontrol.file.exception.NotFoundWriterException

@Singleton
class DelegatingWriter @Inject constructor(private val writers: Set<@JvmSuppressWildcards Writer>) : Writer {
    override fun write(resource: Any, data: Map<String, Any>, format: String): Boolean {
        val writer = this.getWriter(resource, format)
        return writer.write(resource, data, format)
    }

    private fun getWriter(resource: Any, format: String): Writer {
        val writer = this.writers.find { it.supports(resource, format) }
        if (writer === null) {
            throw NotFoundWriterException(resource, format)
        }

        return writer
    }

    override fun supports(resource: Any, format: String): Boolean {
        return this.writers.any { it.supports(resource, format) }
    }
}
