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
        val messagesDirectory = resourceManager.getResource(directory.name)
        if (messagesDirectory === null) {
            return
        }

        val files = messagesDirectory.listFiles()
        if (files === null) {
            return
        }

        for (it in files) {
            val target = File(this.directory, it.name)
            if (!target.exists()) {
                target.createNewFile()
            }

            this.syncMessages(it, target)
        }
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
}