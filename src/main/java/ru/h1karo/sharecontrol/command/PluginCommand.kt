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
import com.google.inject.name.Named
import ru.h1karo.sharecontrol.command.exception.CommandArgumentException
import ru.h1karo.sharecontrol.command.exception.CommandNotFoundException
import ru.h1karo.sharecontrol.command.input.InputInterface
import ru.h1karo.sharecontrol.command.output.OutputInterface
import ru.h1karo.sharecontrol.module.PluginModule

class PluginCommand @Inject constructor(
    @Named(PluginModule.NAME) pluginName: String,
    private val listCommandProvider: Provider<ListCommand>
) : RootCommand() {
    override val name: String = pluginName.toLowerCase()

    override val priority: Int = 1000

    override fun execute(input: InputInterface, output: OutputInterface): Boolean {
        return try {
            val command = this.listCommandProvider.get()
            command.run(input, output)
        } catch (e: CommandArgumentException) {
            throw CommandNotFoundException(emptyList())
        }
    }
}
