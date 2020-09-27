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

package ru.h1karo.sharecontrol.i18n.init

import com.google.common.collect.Maps
import com.google.inject.Inject
import com.google.inject.name.Named
import org.apache.commons.io.FilenameUtils
import org.reflections.Reflections
import org.reflections.scanners.ResourcesScanner
import ru.h1karo.sharecontrol.file.reader.Reader
import ru.h1karo.sharecontrol.file.writer.Writer
import ru.h1karo.sharecontrol.module.I18nModule
import ru.h1karo.sharecontrol.resource.ResourceManager
import java.io.File

class ResourceSyncer @Inject constructor(
        @Named(I18nModule.MESSAGES_DIRECTORY)
        private val directory: File,
        private val resourceManager: ResourceManager,
        private val reader: Reader,
        private val writer: Writer,
) {
    fun sync() {
        if (!this.directory.exists()) {
            this.directory.mkdirs()
        }

        this.findResources().forEach { syncResource(it) }
    }

    private fun findResources(): List<String> {
        val reflections = Reflections(ResourcesScanner())
        val resources = reflections.getResources { filename ->
            MESSAGES_FORMATS.any { format ->
                filename.endsWith(format)
            }
        }

        return resources.filter { it.startsWith(directory.name) }
    }

    private fun syncResource(path: String) {
        val resource = this.resourceManager.getResource(path)
        if (resource === null) {
            return
        }

        val name = FilenameUtils.getName(path)
        val extension = FilenameUtils.getExtension(path)

        val target = File(this.directory, name)
        if (!target.exists()) {
            target.createNewFile()
        }

        val resourceMessages = this.reader.read(resource, extension)
        val targetMessages = this.reader.read(target, target.extension).toMutableMap()

        val synced = this.syncMessages(resourceMessages, targetMessages)
        this.writer.write(target, synced, target.extension)
    }

    private fun syncMessages(original: Map<String, Any>, target: Map<String, Any>): Map<String, Any> {
        val synced = target.toMutableMap()
        val diff = Maps.difference(original, target)

        if (diff.areEqual()) {
            return target
        }

        for (value in diff.entriesOnlyOnLeft()) {
            synced[value.key] = value.value
        }

        for (value in diff.entriesOnlyOnRight()) {
            synced.remove(value.key)
        }

        return synced.toMap()
    }

    companion object {
        private val MESSAGES_FORMATS = setOf("yaml", "yml", "json")
    }
}