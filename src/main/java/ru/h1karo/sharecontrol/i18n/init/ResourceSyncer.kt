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

        val messageDirectory = this.resourceManager.getResource(this.directory.name)
        val filenames = this.findResources()
        for (filename in filenames) {
            val original = File(messageDirectory?.parent, filename)

            val target = File(this.directory, original.name)
            if (!target.exists()) {
                target.createNewFile()
            }

            this.syncMessages(original, target)
        }
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

    private fun syncMessages(original: File, target: File) {
        val originalMessages = this.reader.read(original, original.extension)
        val targetMessages = this.reader.read(target, target.extension).toMutableMap()
        val diff = Maps.difference(originalMessages, targetMessages)

        if (diff.areEqual()) {
            return
        }

        for (value in diff.entriesOnlyOnLeft()) {
            targetMessages[value.key] = value.value
        }

        for (value in diff.entriesOnlyOnRight()) {
            targetMessages.remove(value.key)
        }

        this.writer.write(target, targetMessages, target.extension)
    }

    companion object {
        private val MESSAGES_FORMATS = setOf("yaml", "yml", "json")
    }
}