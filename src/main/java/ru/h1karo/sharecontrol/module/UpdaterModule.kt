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
import com.google.inject.Injector
import com.google.inject.Provides
import com.google.inject.name.Named
import ru.h1karo.sharecontrol.configuration.entry.ParameterContainer
import ru.h1karo.sharecontrol.configuration.plugin.Updater
import ru.h1karo.sharecontrol.updater.SpigotMcProvider
import ru.h1karo.sharecontrol.updater.VersionProvider

class UpdaterModule : AbstractModule() {
    override fun configure() {
        this.bind(VersionProvider::class.java).to(SpigotMcProvider::class.java)
    }

    @Provides
    @Named(UPDATER_ENABLED)
    fun isUpdaterEnabled(injector: Injector): Boolean {
        val parameterContainer = injector.getInstance(ParameterContainer::class.java)
        return parameterContainer.get(Updater).getValue()
    }

    companion object {
        const val UPDATER_ENABLED = "updater-enabled"
    }
}
