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

package ru.h1karo.sharecontrol.configuration.plugin

import ru.h1karo.sharecontrol.configuration.parameter.ParameterInterface
import ru.h1karo.sharecontrol.configuration.parameter.ParameterValueInterface

object Locale : ParameterInterface<String> {
    override fun getPath(): String = "general.locale"
    override fun getDescription(): Set<String> = setOf("The language of plugin messages.")
    override fun getDefault(): Type = Type.ENGLISH
    override fun fromString(value: String?): Type {
        return if (value === null) {
            this.getDefault();
        } else {
            Type.valueOf(value)
        }
    }

    enum class Type(private val value: String) : ParameterValueInterface<String> {
        ENGLISH("en"),
        RUSSIAN("ru");

        override fun getValue(): String = value
    }
}