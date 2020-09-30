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
import net.swiftzer.semver.SemVer
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL

abstract class HttpProvider constructor(private val version: String) : VersionProvider {
    override fun find(): Version? {
        val stream = this.createRequest(this.getUrl())
        val map = this.readResponse(stream)
        val version = this.getVersionFromJson(map)

        return when {
            version.toSemVer() > SemVer.parse(this.version) -> version
            else -> null
        }
    }

    protected abstract fun getUrl(): String

    private fun createRequest(url: String): InputStream {
        val conn = URL(url).openConnection()
        conn.addRequestProperty("User-Agent", USER_AGENT)
        conn.doOutput = true

        return conn.getInputStream()
    }

    private fun readResponse(stream: InputStream): Map<*, *> {
        val reader = BufferedReader(InputStreamReader(stream))
        val response: String = reader.readLines().joinToString("\n")

        return Gson().fromJson(response, Map::class.java)
    }

    protected abstract fun getVersionFromJson(map: Map<*, *>): Version

    companion object {
        private const val USER_AGENT = "ShareControl Updater"
    }
}
