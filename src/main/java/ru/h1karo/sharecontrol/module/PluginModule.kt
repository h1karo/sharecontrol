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

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.multibindings.Multibinder
import com.google.inject.name.Named
import org.reflections.Reflections
import ru.h1karo.sharecontrol.ShareControl
import ru.h1karo.sharecontrol.file.reader.DelegatingReader
import ru.h1karo.sharecontrol.file.reader.Reader
import java.io.File
import java.lang.reflect.Modifier

class PluginModule(private val plugin: ShareControl) : AbstractModule() {
    override fun configure() {
        this.bind(ShareControl::class.java).toInstance(this.plugin)

        this.bindReaders()
    }

    private fun bindReaders() {
        val reflections = Reflections(ShareControl::class.java.`package`.name)
        val readers = reflections.getSubTypesOf(Reader::class.java)
        readers.removeIf { Modifier.isInterface(it.modifiers) || Modifier.isAbstract(it.modifiers) }
        readers.remove(DelegatingReader::class.java)

        val binder = Multibinder.newSetBinder(binder(), Reader::class.java)
        readers.forEach { binder.addBinding().to(it) }

        this.bind(Reader::class.java).to(DelegatingReader::class.java)
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