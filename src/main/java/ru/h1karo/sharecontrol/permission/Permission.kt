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

package ru.h1karo.sharecontrol.permission

import org.bukkit.permissions.PermissionDefault
import org.bukkit.permissions.Permission as BukkitPermission

open class Permission(
    private val name: String,
    private val default: PermissionDefault = PermissionDefault.OP,
    private val parent: Permission? = null
) {
    fun getPath(): String = this.getPathParts().joinToString(PERMISSION_SEPARATOR)

    private fun getPathParts(): Collection<String> {
        if (this.parent != null) {
            return setOf(*this.parent.getPathParts().toTypedArray(), this.name)
        }

        return setOf(this.name)
    }

    fun toBukkit(): BukkitPermission = BukkitPermission(this.getPath(), this.default)

    override fun equals(other: Any?): Boolean {
        if (other !is Permission) {
            return false
        }

        return this.getPath() === other.getPath()
    }

    override fun hashCode(): Int = 31 * this.getPath().hashCode()

    companion object {
        private const val PERMISSION_SEPARATOR = "."
    }
}
