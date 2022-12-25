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
import ru.h1karo.sharecontrol.command.input.InputInterface
import ru.h1karo.sharecontrol.command.output.OutputInterface
import ru.h1karo.sharecontrol.updater.provider.VersionProvider

class UpdateCheckCommand @Inject constructor(
    override val parent: UpdateCommand,
    private val versionProvider: VersionProvider
) : Command() {
    override val name: String = NAME

    override fun execute(input: InputInterface, output: OutputInterface): Boolean {
        val version = this.versionProvider.find()
        if (version === null) {
            output.write("update.latest")
            return true
        }

        output.write("update.new-version", setOf(version.name))
        output.write("update.download", setOf(version.link))

        return true
    }

    companion object {
        private const val NAME = "check"
    }
}
