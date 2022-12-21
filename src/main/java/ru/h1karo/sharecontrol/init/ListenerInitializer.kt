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

package ru.h1karo.sharecontrol.init

import com.google.inject.Inject
import org.bukkit.event.HandlerList
import org.bukkit.plugin.Plugin
import ru.h1karo.sharecontrol.console.BlockStyle
import ru.h1karo.sharecontrol.listener.Listener
import ru.h1karo.sharecontrol.listener.OnDemandListener

class ListenerInitializer @Inject constructor(
    console: BlockStyle,
    private val plugin: Plugin,
    private val listeners: Set<Listener>,
) : AbstractInitializer(console) {
    override fun initialize() {
        val pluginManager = this.plugin.server.pluginManager
        this.getListeners().forEach { pluginManager.registerEvents(it, plugin) }
    }

    override fun terminate() {
        this.getListeners().forEach { HandlerList.unregisterAll(it) }
    }

    private fun getListeners(): Collection<Listener> {
        return this.listeners.filter {
            return@filter when (it) {
                is OnDemandListener -> it.isEnabled()
                else -> true
            }
        }
    }
}
