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
 * @copyright Copyright (c) 2022 ShareControl
 * @author Oleg Kozlov <h1karo@outlook.com>
 * @license GNU General Public License v3.0
 * @link https://github.com/h1karo/sharecontrol
 */

package ru.h1karo.sharecontrol.updater

import ru.h1karo.sharecontrol.configuration.entry.ParameterValue

enum class Provider(
    private val value: String
) : ParameterValue<String> {
    SpigotMC("spigotmc"),
    GitHub("github");

    override fun getValue(): String = this.value

    companion object {
        @Throws(IllegalArgumentException::class)
        fun fromValue(value: String): Provider = values().find { it.value == value }
            ?: throw IllegalArgumentException("A version provider not found for `%s`.".format(value))
    }
}
