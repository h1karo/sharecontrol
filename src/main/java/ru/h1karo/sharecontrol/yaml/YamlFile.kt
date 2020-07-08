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

package ru.h1karo.sharecontrol.yaml

import org.bukkit.configuration.file.YamlConfiguration
import ru.h1karo.sharecontrol.configuration.entry.EntryInterface
import ru.h1karo.sharecontrol.configuration.entry.ParameterInterface
import ru.h1karo.sharecontrol.configuration.entry.ParameterValueInterface
import java.io.File
import java.io.FileWriter

abstract class YamlFile(folder: File, path: String) {
    private val file: File = File(folder, path)
    private lateinit var config: YamlConfiguration
    private val commenter: YamlCommenter = YamlCommenter()

    fun initialize(): YamlFile {
        this.config = YamlConfiguration.loadConfiguration(file)

        val headerSet = this.getHeader()
        if (headerSet !== null) {
            val header = headerSet.joinToString(System.lineSeparator())
            this.config.options().header(header + System.lineSeparator())
        }

        this.getEntries().forEach { this.initializeEntry(it) }
        this.save()
        return this
    }

    private fun initializeEntry(entry: EntryInterface) {
        val has = this.config.contains(entry.getPath())

        if (!has && entry is ParameterInterface<*>) {
            this.config.set(entry.getPath(), entry.getDefault().getValue())
        }
    }

    abstract fun getEntries(): Set<EntryInterface>

    open fun getHeader(): List<String>? = null

    fun <T> get(parameter: ParameterInterface<T>): ParameterValueInterface<T> {
        val value = this.config.get(parameter.getPath())
        if (value === null) {
            return parameter.getDefault()
        }

        return parameter.fromString(value as String?)
    }

    private fun save() {
        val content = commenter.include(this.config.saveToString(), this.getEntries())
        FileWriter(this.file).use { it.write(content) }
    }
}