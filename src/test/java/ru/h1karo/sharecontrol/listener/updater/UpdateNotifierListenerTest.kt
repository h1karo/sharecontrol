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

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyCollection
import org.mockito.Mockito.anyString
import ru.h1karo.sharecontrol.messenger.Messenger
import ru.h1karo.sharecontrol.messenger.StatefulMessenger
import ru.h1karo.sharecontrol.permission.PermissionManagerInterface
import ru.h1karo.sharecontrol.updater.Version
import ru.h1karo.sharecontrol.updater.VersionProvider

internal class UpdateNotifierListenerTest {
    @Test
    @DisplayName("No messages when no permission")
    fun noPermission() {
        val manager = this.createPermissionManager(false)
        val provider = this.createProvider()
        val messenger = mock<Messenger>()

        this.dispatch(provider, manager, messenger)

        verify(provider, times(0)).find()
        verify(messenger, times(0)).send(any(), anyString(), anyCollection())
    }

    @Test
    @DisplayName("No messages when no version")
    fun noVersion() {
        val manager = this.createPermissionManager(true)
        val provider = this.createProvider()
        val messenger = mock<Messenger>()

        this.dispatch(provider, manager, messenger)

        verify(provider, times(1)).find()
        verify(messenger, times(0)).send(any(), anyString(), anyCollection())
    }

    @Test
    @DisplayName("Messages when version available")
    fun onVersionFound() {
        val manager = this.createPermissionManager(true)
        val provider = this.createProvider(Version("3.0.0", "some-link"))
        val messenger = mock<Messenger>()

        this.dispatch(provider, manager, messenger)

        verify(provider, times(1)).find()
        verify(messenger, atLeastOnce()).send(any(), any<(StatefulMessenger) -> Unit>())
    }

    private fun dispatch(provider: VersionProvider, manager: PermissionManagerInterface, messenger: Messenger) {
        val listener = UpdateNotifierListener(true, provider, manager, messenger)

        val player = mock<Player>()
        val event = PlayerJoinEvent(player, "joined")

        listener.onPlayerJoin(event)
    }

    private fun createPermissionManager(granted: Boolean): PermissionManagerInterface {
        val manager = mock<PermissionManagerInterface>()
        `when`(manager.granted(any(), any())).thenReturn(granted)

        return manager
    }

    private fun createProvider(version: Version? = null): VersionProvider {
        val provider = mock<VersionProvider>()
        `when`(provider.find()).thenReturn(version)

        return provider
    }
}
