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

package ru.h1karo.sharecontrol.command.input.argument

import java.text.MessageFormat

abstract class Argument<T>(
    val name: String,
    val isRequired: Boolean = false,
    val isArray: Boolean = false,
    val defaultValue: T? = null,
    val description: String? = null
) {
    val isOptional = !this.isRequired

    @Throws(NullPointerException::class)
    abstract fun transform(value: Any?): T

    fun serialize(): String {
        return when {
            this.isRequired -> MessageFormat.format(REQUIRED_PATTERN, this.name)
            this.isArray -> MessageFormat.format(ARRAY_PATTERN, this.name)
            this.defaultValue === null -> MessageFormat.format(OPTIONAL_PATTERN, this.name)
            else -> MessageFormat.format(DEFAULT_VALUE_PATTERN, this.name, this.defaultValue)
        }
    }

    companion object {
        const val DESCRIPTION_KEY = "commands.{0}.arguments.{1}.description"

        const val REQUIRED_PATTERN = "<{0}>"
        const val OPTIONAL_PATTERN = "[{0}]"
        const val DEFAULT_VALUE_PATTERN = "[{0}={1}]"
        const val ARRAY_PATTERN = "[{0}...]"
    }
}
