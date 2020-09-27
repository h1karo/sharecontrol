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
import ru.h1karo.sharecontrol.console.ConsoleStyle

class ChainInitializer @Inject constructor(
    console: ConsoleStyle,
    private val initializers: Set<@JvmSuppressWildcards Initializer>
) : AbstractInitializer(console) {
    override fun initialize() {
        try {
            val initializers = this.initializers.sorted()

            this.start()
            initializers.forEach { it.initialize() }
        } catch (e: Exception) {
            this.handleException(e)
        } finally {
            this.end()
        }
    }

    override fun terminate() {
        try {
            val initializers = this.initializers.sorted().reversed()

            this.start()
            initializers.forEach { it.terminate() }
        } catch (e: Exception) {
            this.handleException(e)
        } finally {
            this.end()
        }
    }

    private fun handleException(e: Exception) {
        this.error("Exception caught: " + e.message)
        this.error("More information can be found into logs.")
        this.error("Please provide all logs to developer to fix the bug.")

        // @TODO logging
        e.printStackTrace()
    }
}
