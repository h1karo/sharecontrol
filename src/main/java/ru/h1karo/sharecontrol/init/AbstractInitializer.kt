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

package ru.h1karo.sharecontrol.init

import org.bukkit.Bukkit
import ru.h1karo.sharecontrol.console.BlockStyle

abstract class AbstractInitializer(protected val console: BlockStyle) : Initializer {
    private val recipient = Bukkit.getConsoleSender()

    protected fun send(message: String, parameters: Set<String> = emptySet()) =
        this.console.send(recipient, message, parameters)

    protected fun success(message: String, parameters: Set<String> = emptySet()) =
        this.console.success(recipient, message, parameters)

    protected fun error(message: String, parameters: Set<String> = emptySet()) =
        this.console.error(recipient, message, parameters)

    protected fun warning(message: String, parameters: Set<String> = emptySet()) =
        this.console.warning(recipient, message, parameters)

    protected fun start() = this.console.sendTitledLine(recipient)
    protected fun end() = this.console.sendLine(recipient)

    override fun getPriority(): Int = 1

    final override fun compareTo(other: Initializer): Int {
        return other.getPriority() - this.getPriority()
    }
}
