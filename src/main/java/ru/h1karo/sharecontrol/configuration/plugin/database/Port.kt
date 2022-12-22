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

package ru.h1karo.sharecontrol.configuration.plugin.database

import ru.h1karo.sharecontrol.configuration.entry.IntegerValue
import ru.h1karo.sharecontrol.configuration.entry.VerifiableParameter

object Port : VerifiableParameter<Int> {
    private const val MIN_PORT = 0
    private const val MAX_PORT = 65535

    override fun getPath(): String = "general.database.port"
    override fun getDescription(): List<String> = listOf(
        "The database port.",
        "It can be in the range from 0 to 65565.",
        "Default MySQL port: 3306.",
        "Only for MySQL."
    )
    override fun getDefault(): IntegerValue = IntegerValue(3306)
    override fun fromString(value: String?): IntegerValue {
        return if (value === null) {
            getDefault()
        } else {
            IntegerValue(value.toInt())
        }
    }

    override fun verify(value: String?): Boolean {
        if (value === null) {
            return true
        }

        return try {
            val port = value.toInt()
            port in MIN_PORT..MAX_PORT
        } catch (e: NumberFormatException) {
            false
        }
    }

    override fun accepts(): Set<String> = setOf("$MIN_PORT-$MAX_PORT")
}
