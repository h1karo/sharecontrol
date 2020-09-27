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
import ru.h1karo.sharecontrol.Sender
import ru.h1karo.sharecontrol.module.PluginModule

@Singleton
class LoadingConsoleSender @Inject constructor(
    private val sender: ConsoleSender,
    @Named(PluginModule.NAME)
    private val pluginName: String,
    @Named(PluginModule.VERSION)
    private val pluginVersion: String
) : Sender {
    private var state: State = State.NOT_STARTED

    fun start() {
        if (this.state == State.STARTED) {
            throw RuntimeException("You cannot start the loading twice.")
        }

        this.state = State.STARTED
        this.send(this.getTitledLine())
    }

    fun success(message: String): LoadingConsoleSender = this.send("&2✓&7 $message")

    override fun send(message: String): LoadingConsoleSender {
        if (this.state != State.STARTED) {
            throw RuntimeException("You cannot send messages when the loading has not started.")
        }

        this.sender.send(message)
        return this
    }

    fun end() {
        if (this.state == State.FINISHED) {
            throw RuntimeException("You cannot finish the loading twice.")
        }

        this.sender.send(this.getLine())
        this.state = State.FINISHED
    }

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

    enum class State {
        NOT_STARTED, STARTED, FINISHED;
    }
}
