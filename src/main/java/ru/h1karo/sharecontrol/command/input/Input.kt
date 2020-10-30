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

import ru.h1karo.sharecontrol.command.exception.InvalidArgumentException
import ru.h1karo.sharecontrol.command.exception.NotEnoughArgumentException
import java.util.LinkedList

abstract class Input : InputInterface {
    protected var definition: InputDefinition = InputDefinition()
    private var arguments: MutableMap<String, Any?> = mutableMapOf()

    override fun bind(definition: InputDefinition) {
        this.arguments = mutableMapOf()
        this.definition = definition

        this.parse()
    }

    protected abstract fun parse()

    override fun validate() {
        val missedArguments = this.definition.getArguments()
            .filter { it.value.isRequired() }
            .filter { !this.arguments.containsKey(it.key) }

        if (missedArguments.isNotEmpty()) {
            throw NotEnoughArgumentException(missedArguments.keys)
        }
    }

    override fun getArguments(): Map<String, Any?> = this.arguments.toMap()

    override fun getValues(): LinkedList<Any?> = LinkedList(this.arguments.values)

    override fun getArgument(name: String): Any? {
        if (!this.hasArgument(name)) {
            throw IllegalArgumentException("An argument with name %s does not exists.".format(name))
        }

        return when {
            this.arguments.containsKey(name) -> this.arguments[name]
            else -> this.definition.getArgument(name).defaultValue
        }
    }

    override fun getArgument(index: Int): Any? {
        if (!this.hasArgument(index)) {
            throw IllegalArgumentException("An argument with index %s does not exists.".format(index))
        }

        return this.getValues().getOrElse(index) { this.definition.getArgument(index).defaultValue }
    }

    override fun setArgument(name: String, value: Any?) {
        if (!this.hasArgument(name)) {
            throw IllegalArgumentException("An argument with name %s does not exists.".format(name))
        }
        try {
            val definition = this.definition.getArgument(name)
            this.arguments[name] = definition.transform(value)
        } catch (e: NullPointerException) {
            throw InvalidArgumentException(name)
        } catch (e: NumberFormatException) {
            throw InvalidArgumentException(name)
        }
    }

    override fun setArgument(index: Int, value: Any?) {
        val definition = this.definition.getArgument(index)
        this.setArgument(definition.name, value)
    }

    override fun hasArgument(name: String): Boolean = this.definition.hasArgument(name)

    override fun hasArgument(index: Int): Boolean = this.definition.hasArgument(index)
}
