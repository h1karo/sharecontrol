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

import ru.h1karo.sharecontrol.command.exception.CommandArgumentException
import ru.h1karo.sharecontrol.command.input.InputDefinition
import ru.h1karo.sharecontrol.command.input.InputInterface
import ru.h1karo.sharecontrol.command.input.argument.Argument
import ru.h1karo.sharecontrol.command.output.OutputInterface
import java.text.MessageFormat

abstract class Command : CommandInterface {
    override val definition = InputDefinition()

    override val parent: CommandInterface? = null

    override val priority: Int = 0

    override fun getFirstParent(): CommandInterface? {
        var parent = this.parent
        while (parent?.parent != null) {
            parent = parent.parent
        }

        return parent
    }

    override fun getDescription(): String = MessageFormat.format(DESCRIPTION_KEY, this.name)

    override fun getArguments(): List<Argument<*>> = this.definition.getValues()

    @Throws(CommandArgumentException::class)
    override fun run(input: InputInterface, output: OutputInterface): Boolean {
        input.bind(this)
        input.validate()

        return this.execute(input, output)
    }

    protected abstract fun execute(input: InputInterface, output: OutputInterface): Boolean

    override fun getSyntax(): String {
        val command = setOf(this.getFullName())
            .plus(this.getArguments().map { it.serialize() })
            .joinToString(" ")
        return CommandInterface.COMMAND_CHAR + command
    }

    override fun getFullName(): String =
        setOf(this.getPrefix(), this.name)
            .joinToString(" ")
            .trim(' ')

    private fun getPrefix(): String {
        var parent = this.parent
        val parts = LinkedHashSet<String>()
        while (parent !== null) {
            parts.add(parent.name)
            parent = parent.parent
        }

        return parts.reversed().joinToString(" ")
    }

    final override fun compareTo(other: CommandInterface): Int {
        return other.priority - this.priority
    }

    companion object {
        const val DESCRIPTION_KEY = "commands.{0}.description"
        const val ARGUMENT_DESCRIPTION_KEY = "commands.{0}.arguments.{1}"
    }
}
