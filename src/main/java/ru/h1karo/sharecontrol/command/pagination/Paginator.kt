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
 * @copyright Copyright (c) 2022 ShareControl
 * @author Oleg Kozlov <h1karo@outlook.com>
 * @license GNU General Public License v3.0
 * @link https://github.com/h1karo/sharecontrol
 */

package ru.h1karo.sharecontrol.command.pagination

import org.bukkit.util.ChatPaginator
import ru.h1karo.sharecontrol.command.exception.PageNumberOutOfRangeException

class Paginator : PaginatorInterface {
    override fun paginate(content: List<String>, page: Int, limit: Int): PaginationInterface {
        if (page <= 0 || limit <= 0) {
            throw IllegalArgumentException("The page and limit parameters must be positive non-zero integers.")
        }

        val wrappedContent = this.wrap(content)
        val pages = wrappedContent.chunked(limit)

        val pageIndex = page - 1
        val maxPage = pages.size
        if (maxPage <= pageIndex) {
            throw PageNumberOutOfRangeException(page, maxPage)
        }

        return Pagination(pages[pageIndex], page, limit, maxPage)
    }

    private fun wrap(content: List<String>): List<String> {
        return content.map { ChatPaginator.wordWrap(it, LINE_LENGTH) }.flatMap { it.toList() }
    }

    companion object {
        private const val LINE_LENGTH = ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH
    }
}
