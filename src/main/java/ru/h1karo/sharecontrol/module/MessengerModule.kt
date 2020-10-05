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

package ru.h1karo.sharecontrol.module

import com.google.inject.Injector
import com.google.inject.Provides
import ru.h1karo.sharecontrol.i18n.TranslatorInterface
import ru.h1karo.sharecontrol.messenger.ColoredMessenger
import ru.h1karo.sharecontrol.messenger.DelegatingMessenger
import ru.h1karo.sharecontrol.messenger.Messenger
import ru.h1karo.sharecontrol.messenger.TranslatableMessenger
import ru.h1karo.sharecontrol.messenger.format.IcuMessageFormatter
import ru.h1karo.sharecontrol.messenger.format.MessageFormatter
import ru.h1karo.sharecontrol.messenger.transport.Transport

class MessengerModule : AbstractModule() {
    override fun configure() {
        this.bindSet(Transport::class.java)
        this.bind(MessageFormatter::class.java).to(IcuMessageFormatter::class.java)
        this.bind(Messenger::class.java).to(TranslatableMessenger::class.java)
    }

    @Provides
    fun getTranslatableMessenger(injector: Injector): TranslatableMessenger {
        return TranslatableMessenger(
            injector.getInstance(ColoredMessenger::class.java),
            injector.getInstance(TranslatorInterface::class.java)
        )
    }

    @Provides
    fun getColoredMessenger(injector: Injector): ColoredMessenger {
        return ColoredMessenger(
            injector.getInstance(DelegatingMessenger::class.java),
            injector.getInstance(MessageFormatter::class.java)
        )
    }
}
