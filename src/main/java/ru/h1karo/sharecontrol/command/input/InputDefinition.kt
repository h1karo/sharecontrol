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

package ru.h1karo.sharecontrol.command.input

import ru.h1karo.sharecontrol.command.input.argument.Argument
import java.util.LinkedList

class InputDefinition {
    private val arguments: LinkedHashMap<String, Argument<*>> = linkedMapOf()

    constructor(arguments: Set<Argument<*>> = setOf()) {
        arguments.forEach { this.addArgument(it) }
    }

    fun getArguments(): Map<String, Argument<*>> = this.arguments.toMap()

    fun getValues(): LinkedList<Argument<*>> = LinkedList(this.arguments.values)

    fun getArgument(name: String): Argument<*> = this.arguments[name]
        ?: throw IllegalArgumentException("An argument with name %s does not exist".format(name))

    fun getArgument(index: Int): Argument<*> = this.getValues().getOrNull(index)
        ?: throw IllegalArgumentException("An argument with index %s does not exist".format(index))

    fun hasArgument(name: String): Boolean = this.arguments.containsKey(name)

    fun hasArgument(index: Int): Boolean = this.getValues().size > index && index > 0

    fun addArgument(vararg arguments: Argument<*>) = arguments.forEach { this.addArgument(it) }

    fun addArgument(argument: Argument<*>) {
        if (this.hasArgument(argument.name)) {
            throw IllegalArgumentException("An argument with name `%s` already exists.".format(argument.name))
        }

        if (this.hasArray()) {
            throw IllegalArgumentException("Cannot to add an argument after an array argument.")
        }

        if (this.hasOptional() && argument.isRequired) {
            throw IllegalArgumentException("Cannot to add an required argument after an optional argument.")
        }

        this.arguments[argument.name] = argument
    }

    private fun hasArray(): Boolean = this.arguments.any { it.value.isArray }

    private fun hasOptional(): Boolean = this.arguments.any { it.value.isOptional }
}
