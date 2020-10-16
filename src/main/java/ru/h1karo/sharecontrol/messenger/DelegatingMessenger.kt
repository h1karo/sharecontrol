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

package ru.h1karo.sharecontrol.messenger

import com.google.inject.Inject
import ru.h1karo.sharecontrol.messenger.format.MessageFormatter
import ru.h1karo.sharecontrol.messenger.transport.Transport

class DelegatingMessenger @Inject constructor(
    private val transports: Set<@JvmSuppressWildcards Transport>,
    private val formatter: MessageFormatter
) : Messenger {
    override fun send(recipient: Any, message: String, parameters: Set<Any>) {
        val formatted = this.formatter.format(message, parameters)
        this.getTransport(recipient).send(recipient, formatted)
    }

    private fun getTransport(recipient: Any): Transport {
        return this.transports.first { it.supports(recipient) }
    }
}
