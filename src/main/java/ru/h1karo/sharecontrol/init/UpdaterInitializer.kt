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
import ru.h1karo.sharecontrol.module.UpdaterModule
import ru.h1karo.sharecontrol.updater.VersionProvider

class UpdaterInitializer @Inject constructor(
    console: BlockStyle,
    @Named(UpdaterModule.UPDATER_ENABLED)
    private val isUpdaterEnabled: Boolean,
    private val versionProvider: VersionProvider
) : AbstractInitializer(console) {
    override fun initialize() {
        if (!this.isUpdaterEnabled) {
            return
        }

        val version = this.versionProvider.find()
        if (version === null) {
            this.success("You are using the latest version of the plugin.")
            return
        }

        this.send("&7A new version of the plugin has been found: &9{0}&7.", setOf(version.name))
        this.send(" &7You can download the update at this link:")
        this.send(" &9{0}&7.", setOf(version.link))
    }

    override fun terminate() {}

    override fun getPriority(): Int = 50
}
