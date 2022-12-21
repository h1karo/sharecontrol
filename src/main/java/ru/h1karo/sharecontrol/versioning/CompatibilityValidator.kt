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

package ru.h1karo.sharecontrol.versioning

import com.google.inject.Inject
import com.google.inject.name.Named
import net.swiftzer.semver.SemVer
import ru.h1karo.sharecontrol.module.PluginModule

class CompatibilityValidator @Inject constructor(
    @Named(PluginModule.BUKKIT_VERSION) private val bukkitVersion: String
) {
    fun validate(version: String): Boolean {
        val coreVersion = SemVer.parse(this.bukkitVersion)
        val (start, end) = this.getSupportInterval(version)
        return coreVersion in start..end
    }

    @Suppress("UNUSED_PARAMETER")
    private fun getSupportInterval(version: String): List<SemVer> {
        // todo remove hard code with version support provider
        return listOf(
            SemVer(1, 15),
            SemVer(1, 15, 2)
        )
    }
}
