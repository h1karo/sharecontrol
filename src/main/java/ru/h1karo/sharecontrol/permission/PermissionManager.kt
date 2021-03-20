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

package ru.h1karo.sharecontrol.permission

import com.google.inject.Inject
import com.google.inject.Singleton
import org.bukkit.entity.Player
import org.bukkit.plugin.PluginManager
import ru.h1karo.sharecontrol.Resettable

@Singleton
class PermissionManager @Inject constructor(
    private val pluginManager: PluginManager
) : PermissionManagerInterface, Resettable {
    private val permissions: MutableCollection<Permission> = mutableSetOf()

    override fun granted(player: Player, permission: Permission): Boolean {
        if (!this.permissions.contains(permission)) {
            this.register(permission)
        }

        return player.hasPermission(permission.toBukkit())
    }

    private fun register(permission: Permission) {
        this.pluginManager.addPermission(permission.toBukkit())
        this.permissions.add(permission)
    }

    override fun clear() = this.permissions.clear()
}
