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

package ru.h1karo.sharecontrol.module

import ru.h1karo.sharecontrol.command.CommandExecutor
import ru.h1karo.sharecontrol.command.CommandInterface
import ru.h1karo.sharecontrol.command.input.factory.InputFactoryInterface
import ru.h1karo.sharecontrol.command.input.factory.ListInputFactory
import ru.h1karo.sharecontrol.command.output.factory.MessengerOutputFactory
import ru.h1karo.sharecontrol.command.output.factory.OutputFactoryInterface
import org.bukkit.command.CommandExecutor as BukkitCommandExecutor
import org.bukkit.command.TabCompleter as BukkitTabCompleter

class CommandModule : AbstractModule() {
    override fun configure() {
        this.bind(BukkitCommandExecutor::class.java).to(CommandExecutor::class.java)
        this.bind(BukkitTabCompleter::class.java).to(CommandExecutor::class.java)
        this.bindSet(CommandInterface::class.java)
        this.bind(InputFactoryInterface::class.java).to(ListInputFactory::class.java)
        this.bind(OutputFactoryInterface::class.java).to(MessengerOutputFactory::class.java)
    }
}
