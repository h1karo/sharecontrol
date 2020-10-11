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
import ru.h1karo.sharecontrol.command.exception.CommandNotFoundException
import org.bukkit.command.Command as BukkitCommand

@Singleton
class CommandExecutor @Inject constructor(
    private val commands: Set<Command>
) : TabExecutor {
    override fun onTabComplete(sender: CommandSender, command: BukkitCommand, alias: String, arguments: Array<out String>): MutableList<String> {
        return mutableListOf()
    }

    override fun onCommand(sender: CommandSender, command: BukkitCommand, alias: String, arguments: Array<out String>): Boolean {
        return try {
            this.getCommand(arguments.toList()).run()
        } catch (e: CommandNotFoundException) {
            false
        }
    }

    @Throws(CommandNotFoundException::class)
    private fun getCommand(arguments: List<String>): Command {
        val joined = arguments.joinToString(" ")
        val commands: List<Command> = this.commands.filter { it.name.startsWith(joined) }

        if (commands.isEmpty() && arguments.isNotEmpty()) {
            return this.getCommand(arguments.dropLast(1))
        }

        if (commands.size == 1) {
            return commands.first()
        }

        throw CommandNotFoundException(arguments.toList())
    }
}
