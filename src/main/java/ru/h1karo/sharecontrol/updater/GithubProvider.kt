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

package ru.h1karo.sharecontrol.updater

import com.google.inject.Inject
import com.google.inject.name.Named
import ru.h1karo.sharecontrol.module.PluginModule
import java.text.MessageFormat

class GithubProvider @Inject constructor(@Named(PluginModule.VERSION) version: String) : HttpProvider(version) {
    override fun getUrl(): String = MessageFormat.format(URL_PATTERN, OWNER, REPOSITORY)

    override fun getVersionFromJson(map: Map<*, *>): Version {
        val name = map["tag_name"] as String
        val assets = map["assets"] as List<*>
        val asset = assets.first() as Map<*, *>
        val link = asset["browser_download_url"] as String

        return Version(name.removePrefix("v"), link)
    }

    companion object {
        const val URL_PATTERN = "https://api.github.com/repos/{0}/{1}/releases/latest"
        const val OWNER = "h1karo"
        const val REPOSITORY = "sharecontrol"
    }
}
