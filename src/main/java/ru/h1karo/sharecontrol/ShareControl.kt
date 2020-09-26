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

package ru.h1karo.sharecontrol

import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Singleton
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import ru.h1karo.sharecontrol.module.I18nModule
import ru.h1karo.sharecontrol.module.InitializationModule
import ru.h1karo.sharecontrol.module.PluginModule

@Singleton
class ShareControl : JavaPlugin(), Listener {
    private val injector: Injector = Guice.createInjector(PluginModule(this), InitializationModule(), I18nModule())
    private val initializer: Initializer = injector.getInstance(ChainInitializer::class.java)

    override fun onEnable() = this.initializer.initialize()
    override fun onDisable() = this.initializer.terminate()
}