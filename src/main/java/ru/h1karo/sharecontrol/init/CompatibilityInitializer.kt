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
import com.google.inject.name.Named
import ru.h1karo.sharecontrol.console.LoadingConsoleSender
import ru.h1karo.sharecontrol.module.PluginModule
import ru.h1karo.sharecontrol.versioning.CompatibilityValidator

class CompatibilityInitializer @Inject constructor(
    @Named(PluginModule.VERSION) private val version: String,
    private val validator: CompatibilityValidator,
    private val sender: LoadingConsoleSender
) : AbstractInitializer() {
    override fun initialize() {
        if (!validator.validate(version)) {
            sender.send("&cThe server kernel version may not be compatible with the plugin.")
            sender.send("&cThis means that the author of the plugin does not support this version of the kernel at the moment and therefore does not provide a guarantee for the plugins to work with your kernel.")
            sender.send("&cUse the plugin at your own risk.")
        }
    }

    override fun terminate() {}

    override fun getPriority(): Int = 200
}
