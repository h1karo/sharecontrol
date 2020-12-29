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

package ru.h1karo.sharecontrol.command

import ru.h1karo.sharecontrol.command.input.InputInterface
import ru.h1karo.sharecontrol.command.input.argument.Argument
import ru.h1karo.sharecontrol.command.output.OutputInterface

interface CommandInterface : Comparable<CommandInterface> {
    fun getName(): String

    fun getFullName(): String

    fun getParent(): CommandInterface?

    fun getFirstParent(): CommandInterface?

    fun getDescription(): String

    fun getArguments(): List<Argument<*>>

    fun run(input: InputInterface, output: OutputInterface): Boolean

    fun serialize(): String

    fun getPriority(): Int = 0

    companion object {
        const val COMMAND_CHAR = "/"
    }
}
