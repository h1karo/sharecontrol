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

package ru.h1karo.sharecontrol.init

import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.Singleton
import ru.h1karo.sharecontrol.console.BlockStyle
import ru.h1karo.sharecontrol.i18n.Locale
import ru.h1karo.sharecontrol.i18n.MutableTranslatorInterface
import ru.h1karo.sharecontrol.i18n.init.ResourceFinder
import ru.h1karo.sharecontrol.i18n.init.ResourceSyncer

@Singleton
class I18nInitializer @Inject constructor(
    console: BlockStyle,
    private val syncer: ResourceSyncer,
    private val finder: ResourceFinder,
    private val translator: MutableTranslatorInterface,
    private val localeProvider: Provider<Locale>
) : AbstractInitializer(console) {
    override fun initialize() {
        this.syncer.sync()
        this.translator.clear()

        this.finder.find().forEach {
            this.translator.addResource(it)
            this.initLocale(it.locale)
        }

        val locale = this.localeProvider.get()
        this.translator.setLocale(locale)
        this.initLocale(locale)

        this.info("Locale detected: §7%s§8 (§9%s§8)".format(locale.name, locale.abbr))
        this.success("Internationalization component loaded.")
    }

    private fun initLocale(locale: Locale) {
        locale.name = this.translator.trans("name", emptySet(), locale)
    }

    override fun terminate() {
        this.translator.clear()
    }
}
