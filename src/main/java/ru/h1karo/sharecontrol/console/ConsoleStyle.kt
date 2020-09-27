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

package ru.h1karo.sharecontrol.console

import com.google.inject.Inject
import com.google.inject.Singleton
import com.google.inject.name.Named
import ru.h1karo.sharecontrol.messenger.Messenger
import ru.h1karo.sharecontrol.module.PluginModule

@Singleton
class ConsoleStyle @Inject constructor(
    private val messenger: Messenger,
    @Named(PluginModule.NAME)
    private val pluginName: String,
    @Named(PluginModule.VERSION)
    private val pluginVersion: String
) : Messenger {
    fun success(recipient: Any, message: String) = this.send(recipient, "&2✓&8 $message")

    fun error(recipient: Any, message: String) = this.send(recipient, "&4✗&c $message")

    fun warning(recipient: Any, message: String) = this.send(recipient, "&6!&e $message")

    fun sendLine(recipient: Any) = this.send(recipient, this.getLine())

    fun sendTitledLine(recipient: Any) = this.send(recipient, this.getTitledLine())

    override fun send(recipient: Any, message: String) = this.messenger.send(recipient, message)

    private fun getPluginTitle(): String = listOf(this.pluginName, this.pluginVersion).joinToString(" ")

    private fun getLine(): String {
        return "&8" + LINE_CHAR.repeat(LINE_LENGTH)
    }

    private fun getTitledLine(): String {
        val title = getPluginTitle()
        val lineLength = LINE_LENGTH - title.length - 2
        val semiLine = LINE_CHAR.repeat(lineLength / 2)
        return "&8$semiLine &9$title &8$semiLine"
    }

    companion object {
        private const val LINE_CHAR = "-"
        private const val LINE_LENGTH = 64
    }
}
