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

package ru.h1karo.sharecontrol.updater.provider

import com.google.inject.Inject
import com.google.inject.name.Named
import ru.h1karo.sharecontrol.module.PluginModule
import ru.h1karo.sharecontrol.module.UpdaterModule
import ru.h1karo.sharecontrol.updater.Provider

class VersionProviderFactory @Inject constructor(
    @Named(PluginModule.VERSION)
    private val version: String,
    @Named(UpdaterModule.PROVIDER)
    private val type: Provider
) : VersionProviderFactoryInterface {
    override fun build(): VersionProvider {
        return when (this.type) {
            Provider.SpigotMC -> SpigotMcProvider(this.version)
            Provider.GitHub -> GithubProvider(this.version)
        }
    }
}