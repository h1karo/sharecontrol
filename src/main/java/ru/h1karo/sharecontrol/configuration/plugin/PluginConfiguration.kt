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

package ru.h1karo.sharecontrol.configuration.plugin

import com.google.inject.Inject
import com.google.inject.name.Named
import ru.h1karo.sharecontrol.configuration.AbstractConfiguration
import ru.h1karo.sharecontrol.configuration.entry.EntryInterface
import java.io.File

class PluginConfiguration @Inject constructor(
        @Named("directory") folder: File
) : AbstractConfiguration(folder, "config.yaml") {
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

    override fun getEntries(): Set<EntryInterface> = setOf(
            Locale,
            Database,
            Updater
    )
}