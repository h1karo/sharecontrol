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
import org.bukkit.util.ChatPaginator
import ru.h1karo.sharecontrol.command.exception.PageNumberOutOfRangeException
import ru.h1karo.sharecontrol.command.input.InputInterface
import ru.h1karo.sharecontrol.command.input.argument.Argument
import ru.h1karo.sharecontrol.command.input.argument.IntegerArgument
import ru.h1karo.sharecontrol.command.output.OutputInterface
import ru.h1karo.sharecontrol.command.style.OutputStyle
import ru.h1karo.sharecontrol.i18n.TranslatorInterface
import java.text.MessageFormat

abstract class AbstractListCommand @Inject constructor(
    private val translator: TranslatorInterface
) : Command() {
    override val name: String = NAME

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

            output.write("list.header", listOf(page, pagination.getLastPageNumber()))

            pagination.getItems().forEach { output.write(it) }

            if (pagination.hasNext()) {
                val nextPage = page + 1
                val nextPageCommand = this.getCommand(mapOf(PAGE_ARGUMENT to nextPage))
                output.write("list.footer", listOf(nextPageCommand))
            }
        } catch (e: PageNumberOutOfRangeException) {
            output.write("list.header", listOf(e.page, e.maxPage))
            output.write("list.empty")
        }

        return true
    }

    private fun getListItem(command: CommandInterface): String {
        val description = this.translator.trans(command.getDescription())

        return this.translator.trans("list.format", listOf(command.getSyntax(), description))
    }

    protected abstract fun provideCommands(): List<CommandInterface>

    companion object {
        const val NAME = "list"
        const val PAGE_ARGUMENT = "page"
        const val LIMIT = ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT
    }
}
