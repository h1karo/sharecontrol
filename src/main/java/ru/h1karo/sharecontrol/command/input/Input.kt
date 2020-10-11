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

package ru.h1karo.sharecontrol.command.input

import ru.h1karo.sharecontrol.command.exception.NotEnoughArgumentException

abstract class Input : InputInterface {
    private var arguments: MutableMap<String, Any?> = mutableMapOf()
    private var definition: InputDefinition = InputDefinition()

    override fun bind(definition: InputDefinition) {
        this.arguments = mutableMapOf()
        this.definition = definition

        this.parse()
    }

    protected abstract fun parse()

    override fun validate() {
        val missedArguments = this.definition.getArguments()
            .filter { it.value.isRequired }
            .filter { !this.arguments.containsKey(it.key) }

        if (missedArguments.isNotEmpty()) {
            throw NotEnoughArgumentException(missedArguments.keys)
        }
    }

    override fun getArguments(): Map<String, Any?> = this.arguments.toMap()

    override fun getArgument(name: String): Any? {
        if (!this.hasArgument(name)) {
            throw IllegalArgumentException("An argument with name %s does not exists.".format(name))
        }

        return when {
            this.arguments.containsKey(name) -> this.arguments[name]
            else -> this.definition.getArgument(name).defaultValue
        }
    }

    override fun setArgument(name: String, value: Any?) {
        if (!this.hasArgument(name)) {
            throw IllegalArgumentException("An argument with name %s does not exists.".format(name))
        }

        this.arguments[name] = value
    }

    override fun hasArgument(name: String): Boolean = this.definition.hasArgument(name)

    override fun getStringArgument(name: String): String? = this.getArgument(name) as String?

    override fun getBooleanArgument(name: String): Boolean? = this.getArgument(name) as Boolean?

    override fun getIntArgument(name: String): Int? = this.getArgument(name) as Int?
}
