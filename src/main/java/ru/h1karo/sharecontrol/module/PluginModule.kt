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
import com.google.inject.name.Named
import ru.h1karo.sharecontrol.ResourceManagerInterface
import ru.h1karo.sharecontrol.ShareControl
import java.io.File

class PluginModule(private val plugin: ShareControl) : AbstractModule() {
    override fun configure() {
        this.bind(ShareControl::class.java).toInstance(this.plugin)
        this.bind(ResourceManagerInterface::class.java).toInstance(this.plugin)
    }

    @Provides
    @Named("name")
    fun getPluginName(): String {
        return plugin.name
    }

    @Provides
    @Named("version")
    fun getPluginVersion(): String {
        return plugin.description.version
    }

    @Provides
    @Named("directory")
    fun getPluginDirectory(): File {
        return plugin.dataFolder
    }

    @Provides
    @Named("serverVersion")
    fun getServerVersion(): String {
        return plugin.server.version
    }

    @Provides
    @Named("bukkitVersion")
    fun getBukkitVersion(): String {
        return plugin.server.bukkitVersion
    }
}