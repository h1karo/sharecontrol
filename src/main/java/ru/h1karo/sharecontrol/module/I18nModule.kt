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
import ru.h1karo.sharecontrol.i18n.FallbackTranslator
import ru.h1karo.sharecontrol.i18n.FallbackTranslatorInterface
import ru.h1karo.sharecontrol.i18n.Locale
import ru.h1karo.sharecontrol.i18n.MutableTranslatorInterface
import ru.h1karo.sharecontrol.i18n.Translator
import ru.h1karo.sharecontrol.i18n.TranslatorInterface
import ru.h1karo.sharecontrol.i18n.loader.DelegatingLoader
import ru.h1karo.sharecontrol.i18n.loader.Loader
import java.io.File
import ru.h1karo.sharecontrol.configuration.plugin.Locale as LocaleParameter

class I18nModule : AbstractModule() {
    override fun configure() {
        this.bind(Loader::class.java).to(DelegatingLoader::class.java)
        this.bind(TranslatorInterface::class.java).to(FallbackTranslatorInterface::class.java)
        this.bind(FallbackTranslatorInterface::class.java).to(FallbackTranslator::class.java)
        this.bind(MutableTranslatorInterface::class.java).to(FallbackTranslator::class.java)
    }

    @Provides
    fun getFallbackTranslator(translator: Translator): FallbackTranslator {
        return FallbackTranslator(translator)
    }

    @Provides
    fun getLocale(container: ParameterContainer): Locale {
        return container.get(LocaleParameter) as Locale
    }

    @Provides
    @Named(MESSAGES_DIRECTORY)
    fun getMessagesDirectory(@Named(PluginModule.DIRECTORY) pluginDirectory: File): File {
        return File(pluginDirectory, MESSAGES_DIRECTORY_NAME)
    }

    companion object {
        const val MESSAGES_DIRECTORY = "messagesDirectory"

        private const val MESSAGES_DIRECTORY_NAME = "messages"
    }
}
