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
import com.google.inject.name.Named
import org.apache.commons.lang.StringUtils
import ru.h1karo.sharecontrol.SenderInterface

class LoadingBlock @Inject constructor(
        private val sender: ConsoleSender,
        @Named("pluginName") private val pluginName: String
) : SenderInterface {
    fun start() {
        this.send(getLine(true))
    }

    override fun send(message: String): LoadingBlock {
        this.sender.send(message)
        return this
    }

    fun end() {
        this.sender.send(getLine())
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
}