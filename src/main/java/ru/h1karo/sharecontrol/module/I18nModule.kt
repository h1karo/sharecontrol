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
import com.google.inject.Key
import com.google.inject.Provides
import com.google.inject.name.Named
import com.google.inject.name.Names
import ru.h1karo.sharecontrol.configuration.entry.ParameterContainer
import ru.h1karo.sharecontrol.i18n.Locale
import ru.h1karo.sharecontrol.i18n.loader.DelegatingLoader
import ru.h1karo.sharecontrol.i18n.loader.Loader
import java.io.File
import ru.h1karo.sharecontrol.configuration.plugin.Locale as LocaleParameter

class I18nModule : AbstractModule() {
    override fun configure() {
        this.bind(Loader::class.java).to(DelegatingLoader::class.java)
    }

    @Provides
    fun getLocale(injector: Injector): Locale {
        val parameterContainer = injector.getInstance(ParameterContainer::class.java)
        return parameterContainer.get(LocaleParameter) as Locale
    }

    @Provides
    @Named(MESSAGES_DIRECTORY)
    fun getMessagesDirectory(injector: Injector): File {
        val pluginDirectory = injector.getInstance(Key.get(File::class.java, Names.named(PluginModule.DIRECTORY)))
        return File(pluginDirectory, MESSAGES_DIRECTORY_NAME)
    }

    companion object {
        const val MESSAGES_DIRECTORY = "messagesDirectory"

        private const val MESSAGES_DIRECTORY_NAME = "messages"
    }
}