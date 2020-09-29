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
import ru.h1karo.sharecontrol.console.BlockStyle
import ru.h1karo.sharecontrol.module.PluginModule
import ru.h1karo.sharecontrol.versioning.CompatibilityValidator

class CompatibilityInitializer @Inject constructor(
    console: BlockStyle,
    @Named(PluginModule.VERSION) private val version: String,
    private val validator: CompatibilityValidator
) : AbstractInitializer(console) {
    override fun initialize() {
        if (validator.validate(version)) {
            return
        }

        this.warning("The server version may not be compatible with the plugin.")
        this.warning("This means that the author of the plugin does not support")
        this.warning(" this version of the kernel at the moment and therefore")
        this.warning(" does not provide a guarantee for the plugins to work with")
        this.warning(" your kernel.")
        this.warning("Use the plugin at your own risk.")
    }

    override fun terminate() {}

    override fun getPriority(): Int = 200
}
