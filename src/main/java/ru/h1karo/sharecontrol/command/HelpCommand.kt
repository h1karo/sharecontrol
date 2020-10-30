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
import com.google.inject.Provider
import ru.h1karo.sharecontrol.command.input.InputInterface
import ru.h1karo.sharecontrol.command.input.argument.Argument
import ru.h1karo.sharecontrol.command.input.argument.ListStringArgument
import ru.h1karo.sharecontrol.command.output.OutputInterface
import ru.h1karo.sharecontrol.command.style.OutputStyle
import ru.h1karo.sharecontrol.i18n.TranslatorInterface
import java.text.MessageFormat

class HelpCommand @Inject constructor(
    private val commandProviders: Collection<@JvmSuppressWildcards Provider<@JvmSuppressWildcards CommandInterface>>,
    private val translator: TranslatorInterface
) : Command(
    NAME,
    linkedSetOf(
        ListStringArgument(
            COMMAND_ARGUMENT,
            true,
            description = MessageFormat.format(Argument.DESCRIPTION_KEY, NAME, COMMAND_ARGUMENT)
        )
    )
) {
    override fun execute(input: InputInterface, output: OutputInterface): Boolean {
        val style = OutputStyle(output)
        val commandList = input.getArgument(COMMAND_ARGUMENT) as List<*>

        val joined = commandList.joinToString(" ")
        val commands = this.provideCommands().filter { it.getName().equals(joined, true) }

        style.write("help.title")
        if (commands.isEmpty()) {
            style.write("help.not-found")
            return true
        }

        if (commands.size > 1) {
            style.write("help.many.title")
            commands.forEachIndexed { index, command -> style.write("help.many.list", setOf(index, command.getName())) }
            return true
        }

        val command = commands.first()
        style.write("help.command", setOf(command.serialize()))

        val arguments = command.getArguments()
        if (arguments.isNotEmpty()) {
            style.write("help.arguments.title")
        }

        arguments.forEach {
            val nameKey = MessageFormat.format(ARGUMENT_NAME_KEY, command.getName(), it.name)
            val name = this.translator.trans(nameKey)

            val descriptionKey = MessageFormat.format(ARGUMENT_DESCRIPTION_KEY, command.getName(), it.name)
            val description = this.translator.trans(descriptionKey)

            val requirementKey = when {
                it.isRequired -> REQUIRED_ARGUMENT_KEY
                else -> OPTIONAL_ARGUMENT_KEY
            }
            val requirement = this.translator.trans(requirementKey)

            style.write("help.arguments.list", setOf(name, description, requirement))
        }

        return true
    }

    private fun provideCommands() = this.commandProviders.map { it.get() }

    companion object {
        const val NAME = "help"
        const val COMMAND_ARGUMENT = "command"
        const val REQUIRED_ARGUMENT_KEY = "help.arguments.required"
        const val OPTIONAL_ARGUMENT_KEY = "help.arguments.optional"
    }
}
