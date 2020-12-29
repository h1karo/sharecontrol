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

package ru.h1karo.sharecontrol.command.input.factory

import ru.h1karo.sharecontrol.command.CommandInterface
import ru.h1karo.sharecontrol.command.input.InputInterface
import ru.h1karo.sharecontrol.command.input.ListInput
import java.util.LinkedList

class ListInputFactory : InputFactoryInterface {
    override fun build(command: CommandInterface, arguments: Collection<String>): InputInterface {
        return ListInput(this.getParameters(arguments, command))
    }

    private fun getParameters(arguments: Collection<String>, command: CommandInterface): LinkedList<String> {
        if (arguments.isEmpty()) {
            return LinkedList()
        }

        val string = arguments.joinToString(" ")

        if (string == command.getFullName()) {
            return LinkedList()
        }

        val parameters = string.removePrefix(command.getFullName()).trim()
        return LinkedList(parameters.split(" "))
    }
}
