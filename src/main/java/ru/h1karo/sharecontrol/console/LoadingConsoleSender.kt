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
import org.apache.commons.lang.StringUtils
import ru.h1karo.sharecontrol.Sender
import ru.h1karo.sharecontrol.module.PluginModule

@Singleton
class LoadingConsoleSender @Inject constructor(
    private val sender: ConsoleSender,
    @Named(PluginModule.NAME) private val pluginName: String
) : Sender {
    private var state: State = State.NOT_STARTED

    fun start() {
        if (this.state == State.STARTED) {
            throw RuntimeException("You cannot start the loading twice.")
        }

        this.state = State.STARTED
        this.send(getLine(true))
    }

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

        this.sender.send(getLine())
        this.state = State.FINISHED
    }

    private fun getLine(withPluginName: Boolean = false): String {
        val line = if (withPluginName) {
            val lineLength = LINE_LENGTH - pluginName.length - 2
            val semiLine = String.format("%0" + lineLength / 2 + "d", 0)
            val parts = arrayOf(semiLine, pluginName, semiLine)
            StringUtils.join(parts, ' ')
        } else {
            String.format("%0" + LINE_LENGTH + "d", 0)
        }

        return line.replace("0", LINE_CHAR.toString())
    }

    companion object {
        private const val LINE_CHAR = '='
        private const val LINE_LENGTH = 64
    }

    enum class State {
        NOT_STARTED, STARTED, FINISHED;
    }
}
