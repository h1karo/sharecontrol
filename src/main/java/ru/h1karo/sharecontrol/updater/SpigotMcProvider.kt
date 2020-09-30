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

import com.google.gson.JsonObject
import com.google.inject.Inject
import com.google.inject.name.Named
import ru.h1karo.sharecontrol.module.PluginModule
import ru.h1karo.sharecontrol.updater.exception.UnexpectedValueException
import java.text.MessageFormat


class SpigotMcProvider @Inject constructor(@Named(PluginModule.VERSION) version: String) : HttpProvider(version) {
    override fun getUrl(): String = MessageFormat.format(URL_PATTERN, PLUGIN_ID)

    override fun getVersionFromJson(jsonObject: JsonObject): Version {
        if (!jsonObject.has("id") || !jsonObject.has("name")) {
            throw UnexpectedValueException("Response doesn't contain `id` or `name` parameter.")
        }

        val versionId = jsonObject["id"].asDouble
        val name = jsonObject["name"].asString
        val link = MessageFormat.format(DOWNLOAD_LINK_PATTERN, PLUGIN_ID, versionId.toInt())

        return Version(name.removePrefix("v"), link)
    }

    companion object {
        private const val URL_PATTERN = "https://api.spiget.org/v2/resources/{0}/versions/latest"
        private const val DOWNLOAD_LINK_PATTERN = "https://api.spiget.org/v2/resources/{0}/versions/{1}/download"
        private const val PLUGIN_ID = 9225
    }
}