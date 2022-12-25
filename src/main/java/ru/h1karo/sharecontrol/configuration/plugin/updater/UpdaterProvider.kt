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

package ru.h1karo.sharecontrol.configuration.plugin.updater

import ru.h1karo.sharecontrol.configuration.entry.VerifiableParameter
import ru.h1karo.sharecontrol.updater.Provider
import java.lang.IllegalArgumentException

object UpdaterProvider : VerifiableParameter<String> {
    override fun getPath(): String = "general.updater.provider"
    override fun getDescription(): List<String> = listOf(
        "The plugin version provider type.",
        "Available: spigotmc, github.",
        "Default: spigotmc"
    )
    override fun getDefault(): Provider = Provider.SpigotMC
    override fun isHidden(): Boolean = true
    override fun fromString(value: String?): Provider {
        return if (value === null) {
            this.getDefault()
        } else {
            Provider.fromValue(value)
        }
    }

    override fun verify(value: String?): Boolean {
        if (value === null) {
            return true
        }

        return try {
            Provider.fromValue(value)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    override fun accepts(): Set<String> {
        return Provider.values().map { it.getValue() }.toSet()
    }
}
