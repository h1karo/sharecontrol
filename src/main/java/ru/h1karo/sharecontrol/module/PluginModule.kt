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

package ru.h1karo.sharecontrol.module

import com.google.inject.Provides
import com.google.inject.name.Named
import org.bukkit.plugin.Plugin
import ru.h1karo.sharecontrol.ShareControl
import ru.h1karo.sharecontrol.file.reader.DelegatingReader
import ru.h1karo.sharecontrol.file.reader.Reader
import ru.h1karo.sharecontrol.file.writer.DelegatingWriter
import ru.h1karo.sharecontrol.file.writer.Writer
import java.io.File

class PluginModule(private val plugin: ShareControl) : AbstractModule() {
    override fun configure() {
        this.bind(ShareControl::class.java).toInstance(this.plugin)
        this.bind(Plugin::class.java).to(ShareControl::class.java)
        this.bindReaders()
        this.bindWriters()
    }

    private fun bindReaders() {
        this.bindSet(Reader::class.java, setOf(DelegatingReader::class.java))
        this.bind(Reader::class.java).to(DelegatingReader::class.java)
    }

    private fun bindWriters() {
        this.bindSet(Writer::class.java, setOf(DelegatingWriter::class.java))
        this.bind(Writer::class.java).to(DelegatingWriter::class.java)
    }

    @Provides
    @Named(NAME)
    fun getPluginName(): String {
        return plugin.name
    }

    @Provides
    @Named(VERSION)
    fun getPluginVersion(): String {
        return plugin.description.version
    }

    @Provides
    @Named(DIRECTORY)
    fun getPluginDirectory(): File {
        return plugin.dataFolder
    }

    @Provides
    @Named(SERVER_VERSION)
    fun getServerVersion(): String {
        return plugin.server.version
    }

    @Provides
    @Named(BUKKIT_VERSION)
    fun getBukkitVersion(): String {
        return plugin.server.bukkitVersion
    }

    companion object {
        const val NAME = "name"
        const val VERSION = "version"
        const val DIRECTORY = "directory"
        const val SERVER_VERSION = "serverVersion"
        const val BUKKIT_VERSION = "bukkitVersion"
    }
}