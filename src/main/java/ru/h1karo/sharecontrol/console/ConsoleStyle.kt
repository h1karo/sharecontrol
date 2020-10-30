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
import ru.h1karo.sharecontrol.messenger.Messenger

@Singleton
open class ConsoleStyle @Inject constructor(private val messenger: Messenger) : Messenger {
    fun success(recipient: Any, message: String, parameters: Collection<Any> = emptySet()) =
        this.send(recipient, "§2✓§8 $message", parameters)

    fun error(recipient: Any, message: String, parameters: Collection<Any> = emptySet()) =
        this.send(recipient, "§4✗§c $message", parameters)

    fun warning(recipient: Any, message: String, parameters: Collection<Any> = emptySet()) =
        this.send(recipient, "§6!§e $message", parameters)

    override fun send(recipient: Any, message: String, parameters: Collection<Any>) =
        this.messenger.send(recipient, message, parameters)
}
