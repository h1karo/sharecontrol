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

import com.google.inject.Inject
import com.google.inject.Singleton
import com.google.inject.name.Named
import ru.h1karo.sharecontrol.configuration.entry.Entry
import ru.h1karo.sharecontrol.configuration.plugin.ChatPrefix
import ru.h1karo.sharecontrol.configuration.plugin.Locale
import ru.h1karo.sharecontrol.configuration.plugin.database.Host
import ru.h1karo.sharecontrol.configuration.plugin.database.Name
import ru.h1karo.sharecontrol.configuration.plugin.database.Password
import ru.h1karo.sharecontrol.configuration.plugin.database.Path
import ru.h1karo.sharecontrol.configuration.plugin.database.Port
import ru.h1karo.sharecontrol.configuration.plugin.database.Type
import ru.h1karo.sharecontrol.configuration.plugin.database.Username
import ru.h1karo.sharecontrol.configuration.plugin.updater.UpdaterEnabled
import ru.h1karo.sharecontrol.configuration.plugin.updater.UpdaterProvider
import ru.h1karo.sharecontrol.module.PluginModule
import java.io.File

@Singleton
class PluginConfiguration @Inject constructor(
    @Named(PluginModule.DIRECTORY) folder: File
) : YamlConfiguration(folder, "config.yaml") {
    override fun getHeader(): List<String> = listOf(
        "+-----------------------------------------------+ #",
        "+       ShareControl's configuration file       + #",
        "+-----------------------------------------------+ #",
        "The author of the plugin: h1karo",
        "The repository: https://github.com/h1karo/sharecontrol",
        "BukkitDev: https://dev.bukkit.org/projects/sharecontrol",
        "SpigotMC: https://www.spigotmc.org/resources/sharecontrol.9225/",
        "RuBukkit: http://rubukkit.org/threads/admn-sec-mech-sharecontrol-v2-6-4-kontrol-tvorcheskogo-rezhima-1-7-1-11.106125/",
        "Thanks for using my plugin!"
    )

    override fun getEntries(): Set<Entry> =
        setOf(Locale, ChatPrefix).plus(UPDATER_ENTRIES).plus(DATABASE_ENTRIES)

    companion object {
        private val DATABASE_ENTRIES = setOf(Type, Path, Host, Port, Name, Username, Password)
        private val UPDATER_ENTRIES = setOf(UpdaterEnabled, UpdaterProvider)
    }
}
