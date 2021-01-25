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
import org.bukkit.util.ChatPaginator
import ru.h1karo.sharecontrol.command.exception.PageNumberOutOfRangeException
import ru.h1karo.sharecontrol.command.input.InputInterface
import ru.h1karo.sharecontrol.command.input.argument.Argument
import ru.h1karo.sharecontrol.command.input.argument.IntegerArgument
import ru.h1karo.sharecontrol.command.output.OutputInterface
import ru.h1karo.sharecontrol.command.style.OutputStyle
import ru.h1karo.sharecontrol.i18n.TranslatorInterface
import java.text.MessageFormat

class ListCommand @Inject constructor(
    private val commandProviders: Collection<@JvmSuppressWildcards Provider<@JvmSuppressWildcards CommandInterface>>,
    private val translator: TranslatorInterface,
    override val parent: ShareControlCommand
) : Command() {
    override val name: String = NAME

    override val priority: Int = 900

    init {
        this.definition.addArgument(
            IntegerArgument(
                PAGE_ARGUMENT,
                defaultValue = 1,
                description = MessageFormat.format(Argument.DESCRIPTION_KEY, NAME, PAGE_ARGUMENT)
            )
        )
    }

    override fun execute(input: InputInterface, output: OutputInterface): Boolean {
        val style = OutputStyle(output)
        val page = input.getArgument(PAGE_ARGUMENT) as Int
        val commands = this.provideCommands()
        val items = commands.map { this.getListItem(it) }

        try {
            val paginator = style.createPaginator()
            val pagination = paginator.paginate(items.toList(), page, LIMIT - 1)

            output.write("list.title", listOf(page, pagination.getLastPageNumber()))
            pagination.getItems().forEach { output.write(it) }
        } catch (e: PageNumberOutOfRangeException) {
            output.write("list.title", listOf(e.page, e.maxPage))
            output.write("list.empty")
        }

        return true
    }

    private fun getListItem(command: CommandInterface): String {
        val description = this.translator.trans(command.getDescription())

        return this.translator.trans("list.format", listOf(command.getSyntax(), description))
    }

    private fun provideCommands() = this.commandProviders
        .map { it.get() }
        .sorted()
        .filter { it is ShareControlCommand || it.getFirstParent() is ShareControlCommand }

    companion object {
        const val NAME = "list"
        const val PAGE_ARGUMENT = "page"
        const val LIMIT = ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT
    }
}
