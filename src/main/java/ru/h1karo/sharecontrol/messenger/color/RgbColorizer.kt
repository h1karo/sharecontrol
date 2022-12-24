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

package ru.h1karo.sharecontrol.messenger.color

import com.google.inject.Inject
import ru.h1karo.sharecontrol.messenger.color.ColorizerInterface.Companion.COLOR_CHAR
import java.util.regex.Pattern

class RgbColorizer @Inject constructor(
    private val colorizer: ColorizerInterface
) : ColorizerInterface {
    override fun colorize(message: String): String {
        var target = message
        val matcher = HEX_PATTERN.matcher(message)
        while (matcher.find()) {
            val hexCode = matcher.group()
            val hexColor = hexCode.removePrefix(COLOR_CHAR.toString())
            val chatColor = this.createChatColor(hexColor)

            target = target.replace(hexCode, chatColor)
        }

        return this.colorizer.colorize(target)
    }

    private fun createChatColor(hexColor: String): String {
        val colorChars = hexColor.replace("#", "x").toCharArray()

        return colorChars.joinToString("") { String(charArrayOf(COLOR_CHAR, it)) }
    }

    companion object {
        private val HEX_PATTERN = Pattern.compile("&#[0-9a-fA-F]{6}")
    }
}
