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

package ru.h1karo.sharecontrol.i18n

import com.google.inject.Inject
import ru.h1karo.sharecontrol.InitializerInterface
import ru.h1karo.sharecontrol.console.LoadingConsoleSender
import ru.h1karo.sharecontrol.i18n.init.ResourceFinder
import ru.h1karo.sharecontrol.i18n.init.ResourceSyncer

class I18nInitializer @Inject constructor(
        private val sender: LoadingConsoleSender,
        private val syncer: ResourceSyncer,
        private val finder: ResourceFinder,
        private val translator: Translator
) : InitializerInterface {
    override fun initialize() {
        val locale = this.translator.getLocale()
        this.sender.send("&7Locale detected: " + locale.abbr)
        this.sender.send("&7Loading messages...")

        this.syncer.sync()

        this.translator.clear()
        this.finder.find().forEach { this.translator.addResource(it) }

        this.sender.send("&7Messages loading complete.")
    }

    override fun terminate() {
        this.translator.clear()
    }
}