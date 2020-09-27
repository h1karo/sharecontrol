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

package ru.h1karo.sharecontrol.init

import com.google.inject.Inject
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import ru.h1karo.sharecontrol.console.LoadingConsoleSender
import java.lang.Exception

class ChainInitializer @Inject constructor(
        private val initializers: Set<@JvmSuppressWildcards Initializer>,
        private val sender: LoadingConsoleSender,
        private val plugin: Plugin
) : AbstractInitializer() {
    override fun initialize() {
        try {
            val initializers = this.initializers.sorted()

            this.sender.start()
            initializers.forEach { it.initialize() }
        } catch (e: Exception) {
            this.handleException(e)
        } finally {
            this.sender.end()
        }
    }

    override fun terminate() {
        try {
            val initializers = this.initializers.sorted().reversed()

            this.sender.start()
            initializers.forEach { it.terminate() }
        } catch (e: Exception) {
            this.handleException(e)
        } finally {
            this.sender.end()
        }
    }

    private fun handleException(e: Exception) {
        this.sender.send("&cException caught: " + e.message)
        this.sender.send("&cMore information can be found into logs.")
        this.sender.send("&cPlease provide all logs to developer to fix the bug.")

        // @TODO logging
        e.printStackTrace()
    }
}