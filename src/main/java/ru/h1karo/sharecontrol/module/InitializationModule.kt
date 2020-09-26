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

package ru.h1karo.sharecontrol.module

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import org.reflections.Reflections
import ru.h1karo.sharecontrol.ChainInitializer
import ru.h1karo.sharecontrol.Initializer
import ru.h1karo.sharecontrol.ShareControl
import java.lang.reflect.Modifier

class InitializationModule : AbstractModule() {
    override fun configure() {
        val reflections = Reflections(ShareControl::class.java.`package`.name)
        val initializers = reflections.getSubTypesOf(Initializer::class.java)
        initializers.removeIf { Modifier.isInterface(it.modifiers) || Modifier.isAbstract(it.modifiers) }
        initializers.remove(ChainInitializer::class.java)

        val initializerBinder = Multibinder.newSetBinder(binder(), Initializer::class.java)
        initializers.forEach { initializerBinder.addBinding().to(it) }
    }
}