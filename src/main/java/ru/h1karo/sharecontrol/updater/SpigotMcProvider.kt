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

import com.google.gson.Gson
import com.google.inject.Inject
import com.google.inject.name.Named
import net.swiftzer.semver.SemVer
import ru.h1karo.sharecontrol.module.PluginModule
import ru.h1karo.sharecontrol.updater.exception.UnexpectedValueException
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL

class SpigotMcProvider @Inject constructor(
    @Named(PluginModule.VERSION)
    private val version: String
) : VersionProvider {
    override fun find(): Version? {
        val stream = this.createRequest()
        val version = this.readResponse(stream)

        return when {
            version.toSemVer() > SemVer.parse(this.version) -> version
            else -> null
        }
    }

    private fun createRequest(): InputStream {
        val url = URL(URL_PATTERN.format(PLUGIN_ID))
        val conn = url.openConnection()
        conn.addRequestProperty("User-Agent", USER_AGENT)
        conn.doOutput = true

        return conn.getInputStream()
    }

    private fun readResponse(stream: InputStream): Version {
        val reader = BufferedReader(InputStreamReader(stream))
        val response: String = reader.readLines().joinToString("\n")

        val version = Gson().fromJson(response, Map::class.java)
        if (!version.containsKey("id") || !version.containsKey("name")) {
            throw UnexpectedValueException("Response doesn't contain `id` or `name` parameter.")
        }

        val versionId = version["id"] as Double
        val name = version["name"] as String
        val link = DOWNLOAD_LINK_PATTERN.format(PLUGIN_ID, versionId.toInt())

        return Version(name.removePrefix("v"), link)
    }

    companion object {
        const val URL_PATTERN = "https://api.spiget.org/v2/resources/%d/versions/latest"
        const val DOWNLOAD_LINK_PATTERN = "https://api.spiget.org/v2/resources/%d/versions/%d/download"
        const val PLUGIN_ID = 9225
        const val USER_AGENT = "ShareControl Updater"
    }
}