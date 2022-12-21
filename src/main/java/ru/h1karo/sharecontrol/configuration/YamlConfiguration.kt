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

package ru.h1karo.sharecontrol.configuration

import ru.h1karo.sharecontrol.configuration.entry.Entry
import ru.h1karo.sharecontrol.configuration.entry.Parameter
import ru.h1karo.sharecontrol.configuration.entry.ParameterValue
import ru.h1karo.sharecontrol.configuration.entry.VerifiableParameter
import ru.h1karo.sharecontrol.configuration.exception.InvalidValueException
import ru.h1karo.sharecontrol.yaml.YamlCommenter
import java.io.File
import java.io.FileWriter
import org.bukkit.configuration.file.YamlConfiguration as BukkitYamlConfiguration

abstract class YamlConfiguration(folder: File, path: String) {
    private val file = File(folder, path)
    private val config = BukkitYamlConfiguration()
    private val commenter = YamlCommenter()

    fun initialize(): YamlConfiguration {
        val parent = this.file.parentFile
        if (!parent.exists() && !parent.mkdirs()) {
            throw FileSystemException(parent, reason = "Unable to create directory")
        }

        if (!this.file.exists() && !this.file.createNewFile()) {
            throw FileSystemException(this.file, reason = "Unable to create a config file")
        }

        this.config.load(file)

        val header = this.getHeader()
        if (header !== null) {
            this.config.options().setHeader(header)
        }

        this.getEntries().forEach { this.initializeEntry(it) }
        this.save()

        return this
    }

    private fun initializeEntry(entry: Entry) {
        val hasEntry = this.config.contains(entry.getPath())

        if (!hasEntry && entry is Parameter<*>) {
            this.config.set(entry.getPath(), entry.getDefault().getValue())
        }

        if (hasEntry && entry is VerifiableParameter<*>) {
            val value = this.config.getString(entry.getPath())
            if (!entry.verify(value)) {
                throw InvalidValueException(entry, value)
            }
        }
    }

    abstract fun getEntries(): Set<Entry>

    open fun getHeader(): List<String>? = null

    fun <T> get(parameter: Parameter<T>): ParameterValue<T> {
        val value = this.config.get(parameter.getPath())
        if (value === null) {
            return parameter.getDefault()
        }

        return parameter.fromString(value.toString())
    }

    private fun save() {
        val content = commenter.include(this.config.saveToString(), this.getEntries())
        FileWriter(this.file).use { it.write(content) }
    }
}
