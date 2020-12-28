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

import com.google.inject.Inject
import com.google.inject.Singleton
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import ru.h1karo.sharecontrol.command.exception.CommandArgumentException
import ru.h1karo.sharecontrol.command.exception.CommandNotFoundException
import ru.h1karo.sharecontrol.command.input.factory.InputFactoryInterface
import ru.h1karo.sharecontrol.command.output.factory.OutputFactoryInterface
import org.bukkit.command.Command as BukkitCommand

@Singleton
class CommandExecutor @Inject constructor(
    private val commands: Set<@JvmSuppressWildcards CommandInterface>,
    private val inputFactory: InputFactoryInterface,
    private val outputFactory: OutputFactoryInterface
) : TabExecutor {
    override fun onTabComplete(sender: CommandSender, bukkitCommand: BukkitCommand, alias: String, arguments: Array<out String>): MutableList<String> {
        return this.findCompletes(arguments.toList()).toMutableList()
    }

    private fun findCompletes(arguments: List<String>): List<String> {
        val joined = arguments.joinToString(" ")
        val commands = this.commands.filter { it.getName().startsWith(joined) }
        val names = commands.map { it.getName() }
        val prefix = arguments.dropLast(1).joinToString(" ")
        return names.map { it.removePrefix(prefix).trim() }
    }

    override fun onCommand(sender: CommandSender, bukkitCommand: BukkitCommand, alias: String, arguments: Array<out String>): Boolean {
        val command: CommandInterface
        val output = this.outputFactory.build(sender)

        try {
            command = this.getCommand(arguments.toList())
        } catch (e: CommandNotFoundException) {
            output.write("commands._not-found")
            return true
        }

        val input = this.inputFactory.build(command, arguments.toList())

        return try {
            command.run(input, output)
        } catch (e: CommandArgumentException) {
            output.write("commands._syntax", setOf(command.serialize()))
            true
        }
    }

    @Throws(CommandNotFoundException::class)
    private fun getCommand(arguments: List<String>): CommandInterface {
        if (arguments.isEmpty()) {
            return this.commands.find { it.getName() == "list" }!!
        }

        val joined = arguments.joinToString(" ")
        val commands = this.commands.filter { it.getName().equals(joined, true) }

        if (commands.isEmpty() && arguments.size > 1) {
            return this.getCommand(arguments.dropLast(1))
        }

        if (commands.size == 1) {
            return commands.first()
        }

        throw CommandNotFoundException(arguments.toList())
    }
}
