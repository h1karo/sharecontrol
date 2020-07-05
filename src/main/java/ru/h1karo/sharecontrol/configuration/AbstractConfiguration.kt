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

    fun initialize(): AbstractConfiguration {
        val headerSet = this.getHeader()
        if (headerSet !== null) {
            val header = headerSet.joinToString(System.lineSeparator())
            this.config.options().header(header)
        }

        this.getParameters().forEach { this.initializeParameter(it) }
        this.save()
        return this
    }

    private fun <T> initializeParameter(parameter: ParameterInterface<T>) {
        if (this.config.contains(parameter.getPath())) {
            return
        }

        this.config.set(parameter.getPath(), parameter.getDefault().getValue())
    }

    abstract fun getParameters(): Set<ParameterInterface<*>>

    open fun getHeader(): List<String>? = null

    fun <T> get(parameter: ParameterInterface<T>): ParameterValueInterface<T> {
        val value = this.config.get(parameter.getPath())
        if (value === null) {
            return parameter.getDefault()
        }

        return parameter.fromString(value as String?)
    }

    @Throws(IOException::class)
    fun save() = config.save(file)
}