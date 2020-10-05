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

package ru.h1karo.sharecontrol.database

import ru.h1karo.sharecontrol.configuration.entry.ParameterValue
import ru.h1karo.sharecontrol.database.annotation.Mysql
import ru.h1karo.sharecontrol.database.annotation.Sqlite

enum class DatabaseType(private val value: String, private val annotation: Class<out Annotation>) :
    ParameterValue<String> {
    MySQL("mysql", Mysql::class.java),
    SQLite("sqlite", Sqlite::class.java);

    override fun getValue(): String = this.value
    fun getAnnotation(): Class<out Annotation> = this.annotation

    companion object {
        @Throws(IllegalArgumentException::class)
        fun fromValue(value: String): DatabaseType = values().find { it.value == value }
            ?: throw IllegalArgumentException("The database type not found for `%s`.".format(value))
    }
}
