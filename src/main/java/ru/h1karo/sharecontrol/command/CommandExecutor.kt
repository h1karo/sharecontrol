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
        val inputArguments = listOf(bukkitCommand.name, *arguments)
        return this.findCompletes(inputArguments).toMutableList()
    }

    private fun findCompletes(input: List<String>): List<String> {
        val joined = input.joinToString(" ")
        val commands = this.commands.filter { it.getFullName().startsWith(joined, true) }
        val names = commands.map(CommandInterface::getFullName)
        val prefix = input.dropLast(1).joinToString(" ")
        return names.map { it.removePrefix(prefix).trim() }
    }

    override fun onCommand(sender: CommandSender, bukkitCommand: BukkitCommand, alias: String, arguments: Array<out String>): Boolean {
        val command: CommandInterface
        val output = this.outputFactory.build(sender)
        val inputArguments = listOf(bukkitCommand.name, *arguments)

        return try {
            command = this.getCommand(inputArguments)

            val input = this.inputFactory.build(command, inputArguments)
            command.run(input, output)
        } catch (e: CommandNotFoundException) {
            output.write("commands._not-found")
            true
        } catch (e: CommandArgumentException) {
            output.write("commands._syntax", setOf(e.command.getSyntax()))
            true
        }
    }

    @Throws(CommandNotFoundException::class)
    private fun getCommand(input: List<String>): CommandInterface {
        val joined = input.joinToString(" ")
        val commands = this.commands.filter { it.getFullName().equals(joined, true) }

        if (commands.isEmpty() && input.size > 1) {
            return this.getCommand(input.dropLast(1))
        }

        if (commands.size == 1) {
            return commands.first()
        }

        throw CommandNotFoundException(input)
    }
}
