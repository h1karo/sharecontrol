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

package ru.h1karo.sharecontrol.listener.updater

import com.google.inject.Inject
import com.google.inject.name.Named
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent
import ru.h1karo.sharecontrol.listener.OnDemandListener
import ru.h1karo.sharecontrol.messenger.Messenger
import ru.h1karo.sharecontrol.module.UpdaterModule
import ru.h1karo.sharecontrol.updater.VersionProvider

class UpdaterListener @Inject constructor(
    @Named(UpdaterModule.UPDATER_ENABLED)
    private val isUpdaterEnabled: Boolean,
    private val versionProvider: VersionProvider,
    private val messenger: Messenger,
) : OnDemandListener {
    override fun isEnabled(): Boolean = this.isUpdaterEnabled

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        // @todo permission check
        val version = this.versionProvider.find() ?: return

        this.messenger.send(event.player) {
            it.send("update.new-version", setOf(version.name))
            it.send("update.download", setOf(version.link))
        }
    }
}
