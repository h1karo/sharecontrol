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

package ru.h1karo.sharecontrol.module

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.name.Named
import ru.h1karo.sharecontrol.configuration.ParameterContainer
import ru.h1karo.sharecontrol.configuration.plugin.updater.UpdaterEnabled
import ru.h1karo.sharecontrol.configuration.plugin.updater.UpdaterProvider
import ru.h1karo.sharecontrol.updater.Provider
import ru.h1karo.sharecontrol.updater.provider.VersionProvider
import ru.h1karo.sharecontrol.updater.provider.VersionProviderFactory
import ru.h1karo.sharecontrol.updater.provider.VersionProviderFactoryInterface

class UpdaterModule : AbstractModule() {
    override fun configure() {
        this.bind(VersionProviderFactoryInterface::class.java).to(VersionProviderFactory::class.java)
    }

    @Provides
    fun getVersionProvider(factory: VersionProviderFactoryInterface): VersionProvider {
        return factory.build()
    }

    @Provides
    @Named(UPDATER_ENABLED)
    fun isUpdaterEnabled(container: ParameterContainer): Boolean {
        return container.get(UpdaterEnabled).getValue()
    }

    @Provides
    @Named(PROVIDER)
    fun getUpdaterProvider(container: ParameterContainer): Provider {
        return container.get(UpdaterProvider) as Provider
    }

    companion object {
        const val UPDATER_ENABLED = "updaterEnabled"
        const val PROVIDER = "updaterProvider"
    }
}
