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
import com.google.gson.JsonParser
import net.swiftzer.semver.SemVer
import ru.h1karo.sharecontrol.updater.exception.UnexpectedValueException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL

abstract class HttpProvider constructor(private val version: String) : VersionProvider {
    override fun find(): Version? {
        val stream = this.createRequest(this.getUrl())
        val jsonObject = this.readResponse(stream)
        val version = this.getVersionFromJson(jsonObject)

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

    private fun readResponse(stream: InputStream): JsonObject {
        val reader = InputStreamReader(stream)
        val element = JsonParser.parseReader(reader)

        if (!element.isJsonObject) {
            throw UnexpectedValueException("Expected json object, got array.")
        }

        return element.asJsonObject
    }

    protected abstract fun getVersionFromJson(jsonObject: JsonObject): Version

    companion object {
        private const val USER_AGENT = "ShareControl Updater"
    }
}