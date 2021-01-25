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

import ru.h1karo.sharecontrol.command.exception.ArgumentTransformationException

class IntegerArgument(
    name: String,
    isRequired: Boolean = false,
    defaultValue: Int? = null,
    description: String? = null
) : Argument<Int>(name, isRequired, false, defaultValue, description) {
    override fun transform(value: Any?): Int {
        if (value === null) {
            throw NullPointerException()
        }

        if (value is Int) {
            return value
        }

        if (value is String) {
            return value.toInt()
        }

        if (value is Double) {
            return value.toInt()
        }

        if (value is Float) {
            return value.toInt()
        }

        throw ArgumentTransformationException(this.name, value)
    }
}
