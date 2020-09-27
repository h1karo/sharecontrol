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

package ru.h1karo.sharecontrol.yaml

import ru.h1karo.sharecontrol.configuration.entry.DescribedEntry
import ru.h1karo.sharecontrol.configuration.entry.Entry

class YamlCommenter {
    fun include(yaml: String, entries: Set<Entry>): String {
        val comments: HashMap<String, String> = this.prepareComments(entries)
        if (comments.isEmpty()) {
            return yaml
        }

        return this.doInclude(yaml.lines(), comments).joinToString(System.lineSeparator())
    }

    private fun prepareComments(entries: Set<Entry>): HashMap<String, String> {
        val comments: HashMap<String, String> = HashMap()

        entries.forEach { entry ->
            if (entry !is DescribedEntry) {
                return@forEach
            }

            val count = entry.getPath().count { path -> '.' == path }
            val leadingSpaces = "  ".repeat(count)
            val comment = entry.getDescription()
                .joinToString(System.lineSeparator()) {
                    when {
                        it.isNotEmpty() -> "$leadingSpaces# $it"
                        else -> it
                    }
                }

            comments[entry.getPath()] = comment
        }

        return comments
    }

    private fun doInclude(content: List<String>, comments: Map<String, String>): List<String> {
        val newContent = mutableListOf<String>()
        val currentPath = mutableListOf<String>()
        content.forEach { line ->
            if (this.isNode(line)) {
                val node = line.substring(0, line.indexOf(':')).trim()
                val whitespaceCount = line.indexOf(node)

                while (currentPath.size != whitespaceCount / 2 && currentPath.size > 0) {
                    currentPath.removeAt(currentPath.size - 1)
                }

                currentPath.add(node)

                val path = currentPath.joinToString(".")
                val comment = comments[path]

                if (comment !== null) {
                    newContent.add(comment)
                }
            }

            newContent.add(line)
        }
        return newContent
    }

    private fun isNode(line: String): Boolean {
        if (line.startsWith('#')) {
            return false
        }

        return line.contains(": ") || line.length > 1 && line[line.length - 1] == ':'
    }
}