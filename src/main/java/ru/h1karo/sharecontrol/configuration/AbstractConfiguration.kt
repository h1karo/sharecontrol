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

package ru.h1karo.sharecontrol.configuration

import org.bukkit.configuration.file.YamlConfiguration
import ru.h1karo.sharecontrol.configuration.parameter.ParameterInterface
import ru.h1karo.sharecontrol.configuration.parameter.ParameterValueInterface
import java.io.File
import java.io.IOException

abstract class AbstractConfiguration(folder: File, path: String) {
    private var file: File = File(folder, path)
    private var config: YamlConfiguration = YamlConfiguration.loadConfiguration(file)

    abstract fun initialize()

    fun <T> get(parameter: ParameterInterface<T>): ParameterValueInterface<T> {
        val value = this.config.get(parameter.getPath())
        if (value === null) {
            return parameter.getDefault();
        }

        return parameter.fromString(value as String?)
    }

    fun get(path: String, default: Any? = null): Any? {
        return this.config.get(path, default)
    }

    fun getString(path: String, default: String? = null): String? {
        return this.config.getString(path, default)
    }

    inline fun <reified T : Enum<T>> getEnum(path: String, default: T): T? {
        val value: String? = this.getString(path)
        if (value === null) {
            return null;
        }

        return try {
            enumValueOf<T>(value)
        } catch (exception: IllegalArgumentException) {
            default
        }
    }

    @Throws(IOException::class)
    fun save() = config.save(file)
}