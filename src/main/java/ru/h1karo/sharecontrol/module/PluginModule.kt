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

package ru.h1karo.sharecontrol.module

import com.google.inject.Provides
import com.google.inject.name.Named
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin
import ru.h1karo.sharecontrol.Resettable
import ru.h1karo.sharecontrol.ShareControl
import ru.h1karo.sharecontrol.file.reader.DelegatingReader
import ru.h1karo.sharecontrol.file.reader.Reader
import ru.h1karo.sharecontrol.file.writer.DelegatingWriter
import ru.h1karo.sharecontrol.file.writer.Writer
import ru.h1karo.sharecontrol.permission.PermissionManager
import ru.h1karo.sharecontrol.permission.PermissionManagerInterface
import java.io.File

class PluginModule(private val plugin: ShareControl) : AbstractModule() {
    override fun configure() {
        this.bind(ShareControl::class.java).toInstance(this.plugin)
        this.bind(JavaPlugin::class.java).to(ShareControl::class.java)
        this.bind(Plugin::class.java).to(ShareControl::class.java)
        this.bind(PermissionManagerInterface::class.java).to(PermissionManager::class.java)
        this.bindSet(Resettable::class.java)
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
        return this.plugin.name
    }

    @Provides
    @Named(VERSION)
    fun getPluginVersion(): String {
        return this.plugin.description.version
    }

    @Provides
    @Named(DIRECTORY)
    fun getPluginDirectory(): File {
        return this.plugin.dataFolder
    }

    @Provides
    @Named(DATA_DIRECTORY)
    fun getDataDirectory(@Named(DIRECTORY) pluginDirectory: File): File {
        val directory = File(pluginDirectory, DATA_DIRECTORY_NAME)

        if (!directory.exists()) {
            directory.mkdirs()
        }

        return directory
    }

    @Provides
    @Named(SERVER_VERSION)
    fun getServerVersion(): String {
        return this.plugin.server.version
    }

    @Provides
    @Named(BUKKIT_VERSION)
    fun getBukkitVersion(): String {
        return this.plugin.server.bukkitVersion
    }

    @Provides
    fun getPluginManager(): PluginManager {
        return this.plugin.server.pluginManager
    }

    companion object {
        const val NAME = "name"
        const val VERSION = "version"
        const val DIRECTORY = "directory"
        const val DATA_DIRECTORY = "dataDirectory"
        const val SERVER_VERSION = "serverVersion"
        const val BUKKIT_VERSION = "bukkitVersion"

        private const val DATA_DIRECTORY_NAME = "data"
    }
}
