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

package ru.h1karo.sharecontrol.configuration.plugin

import ru.h1karo.sharecontrol.configuration.entry.Parameter
import ru.h1karo.sharecontrol.i18n.Locale

object Locale : Parameter<String> {
    override fun getPath(): String = "general.locale"
    override fun getDescription(): List<String> = listOf(
        "The language of the plugin messages.",
        "Available out-of-the-box: en, ru.",
        "You can add your language by creating a file in the `messages` directory with the appropriate name.",
        "Default: en"
    )

    override fun getDefault(): Locale = Locale("en")
    override fun fromString(value: String?): Locale {
        return if (value === null) {
            this.getDefault()
        } else {
            Locale(value)
        }
    }
}
