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

import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito
import ru.h1karo.sharecontrol.command.input.factory.InputFactoryInterface
import ru.h1karo.sharecontrol.command.output.factory.OutputFactoryInterface
import kotlin.test.assertEquals

internal class CommandExecutorTest {
    @ParameterizedTest
    @MethodSource("provideData")
    fun onTabComplete(commands: Set<CommandInterface>, arguments: Array<String>, expected: List<String>) {
        val executor = this.createExecutor(commands)
        val sender = Mockito.mock(CommandSender::class.java)
        val command = this.createBukkitCommand()

        val actual = executor.onTabComplete(sender, command, ALIAS, arguments)
        assertEquals(actual, expected)
    }

    private fun createExecutor(commands: Set<CommandInterface>): CommandExecutor {
        val inputFactory = Mockito.mock(InputFactoryInterface::class.java)
        val outputFactory = Mockito.mock(OutputFactoryInterface::class.java)

        return CommandExecutor(commands, inputFactory, outputFactory)
    }

    private fun createBukkitCommand(): BukkitCommand {
        val command = Mockito.mock(BukkitCommand::class.java)
        Mockito.`when`(command.name).thenReturn(COMMAND_NAME)

        return command
    }

    companion object {
        private const val COMMAND_NAME = "sharecontrol"
        private const val ALIAS = "sc"

        @JvmStatic
        fun provideData(): Collection<Arguments> {
            val commands = setOf(
                this.createCommand(setOf(COMMAND_NAME)),
                this.createCommand(setOf(COMMAND_NAME, "help")),
                this.createCommand(setOf(COMMAND_NAME, "list")),
                this.createCommand(setOf(COMMAND_NAME, "update", "check")),
            )

            return setOf(
                Arguments.of(commands, arrayOf("lis"), listOf("list")),
                Arguments.of(commands, arrayOf("h"), listOf("help")),
                Arguments.of(commands, arrayOf("update", "c"), listOf("check")),
            )
        }

        private fun createCommand(path: Set<String>): CommandInterface {
            val command = Mockito.mock(CommandInterface::class.java)
            Mockito.`when`(command.name).thenReturn(path.last())
            Mockito.`when`(command.getFullPath()).thenReturn(path)
            Mockito.`when`(command.getFullName()).thenReturn(path.joinToString(" "))

            return command
        }
    }
}
