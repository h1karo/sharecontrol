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

import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.Provides
import com.google.inject.name.Named
import com.google.inject.name.Names
import ru.h1karo.sharecontrol.configuration.ParameterContainer
import ru.h1karo.sharecontrol.configuration.plugin.database.Host
import ru.h1karo.sharecontrol.configuration.plugin.database.Name
import ru.h1karo.sharecontrol.configuration.plugin.database.Password
import ru.h1karo.sharecontrol.configuration.plugin.database.Path
import ru.h1karo.sharecontrol.configuration.plugin.database.Port
import ru.h1karo.sharecontrol.configuration.plugin.database.Username
import ru.h1karo.sharecontrol.database.DatabaseType
import ru.h1karo.sharecontrol.database.config.Configuration
import ru.h1karo.sharecontrol.database.driver.Driver
import java.io.File
import ru.h1karo.sharecontrol.configuration.plugin.database.Type as DatabaseTypeParameter

class DatabaseModule : AbstractModule() {
    override fun configure() {
        this.bindByAnnotation(Configuration::class.java)
        this.bindByAnnotation(Driver::class.java)
    }

    @Provides
    fun getType(injector: Injector): DatabaseType {
        val container = injector.getInstance(ParameterContainer::class.java)
        return container.get(DatabaseTypeParameter) as DatabaseType
    }

    @Provides
    fun getConfiguration(injector: Injector): Configuration {
        val type = injector.getInstance(DatabaseType::class.java)
        return injector.getInstance(Key.get(Configuration::class.java, type.getAnnotation()))
    }

    @Provides
    fun getDriver(injector: Injector): Driver {
        val type = injector.getInstance(DatabaseType::class.java)
        return injector.getInstance(Key.get(Driver::class.java, type.getAnnotation()))
    }

    @Provides
    @Named(PATH)
    fun getDatabasePath(injector: Injector): String {
        val container = injector.getInstance(ParameterContainer::class.java)
        val path = container.get(Path).getValue()

        val directory = injector.getInstance(Key.get(File::class.java, Names.named(PluginModule.DATA_DIRECTORY)))
        val file = File(directory, path)

        val parent = file.parentFile
        if (!parent.exists()) {
            parent.mkdirs()
        }

        return file.absolutePath
    }

    @Provides
    @Named(HOST)
    fun getDatabaseHost(injector: Injector): String {
        val container = injector.getInstance(ParameterContainer::class.java)
        return container.get(Host).getValue()
    }

    @Provides
    @Named(PORT)
    fun getDatabasePort(injector: Injector): Int {
        val container = injector.getInstance(ParameterContainer::class.java)
        return container.get(Port).getValue()
    }

    @Provides
    @Named(NAME)
    fun getDatabaseName(injector: Injector): String {
        val container = injector.getInstance(ParameterContainer::class.java)
        return container.get(Name).getValue()
    }

    @Provides
    @Named(USERNAME)
    fun getDatabaseUsername(injector: Injector): String {
        val container = injector.getInstance(ParameterContainer::class.java)
        return container.get(Username).getValue()
    }

    @Provides
    @Named(PASSWORD)
    fun getDatabasePassword(injector: Injector): String {
        val container = injector.getInstance(ParameterContainer::class.java)
        return container.get(Password).getValue()
    }

    companion object {
        const val PATH = "databasePath"
        const val HOST = "databaseHost"
        const val PORT = "databasePort"
        const val NAME = "databaseName"
        const val USERNAME = "databaseUsername"
        const val PASSWORD = "databasePassword"
    }
}
