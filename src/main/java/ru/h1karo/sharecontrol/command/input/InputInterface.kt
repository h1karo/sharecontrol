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

package ru.h1karo.sharecontrol.command.input

import ru.h1karo.sharecontrol.command.CommandInterface
import ru.h1karo.sharecontrol.command.exception.NotEnoughArgumentException
import java.util.LinkedList

interface InputInterface {
    fun bind(command: CommandInterface)

    @Throws(NotEnoughArgumentException::class)
    fun validate()

    fun getArguments(): Map<String, Any?>

    fun getValues(): LinkedList<Any?>

    /**
     * Get argument by name.
     */
    fun getArgument(name: String): Any?

    /**
     * Get argument by index.
     */
    fun getArgument(index: Int): Any?

    /**
     * Set argument value by argument name.
     */
    fun setArgument(name: String, value: Any?)

    /**
     * Set argument value by argument index.
     */
    fun setArgument(index: Int, value: Any?)

    /**
     * Validates that input definition has the argument by name.
     */
    fun hasArgument(name: String): Boolean

    /**
     * Validates that input definition has the argument by index.
     */
    fun hasArgument(index: Int): Boolean
}
