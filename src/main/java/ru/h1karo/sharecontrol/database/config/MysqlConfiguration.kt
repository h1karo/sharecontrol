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

package ru.h1karo.sharecontrol.database.config

import com.google.inject.Inject
import com.google.inject.name.Named
import ru.h1karo.sharecontrol.database.annotation.Mysql
import ru.h1karo.sharecontrol.module.DatabaseModule

@Mysql
class MysqlConfiguration @Inject constructor(
    @Named(DatabaseModule.HOST)
    private val host: String,
    @Named(DatabaseModule.PORT)
    private val port: Int,
    @Named(DatabaseModule.USERNAME)
    private val username: String,
    @Named(DatabaseModule.PASSWORD)
    private val password: String,
    @Named(DatabaseModule.NAME)
    private val database: String
) : UserPasswordConfiguration {
    override fun getUser(): String = this.username

    override fun getPassword(): String = this.password

    override fun getDsn(): String = "mysql://${this.host}:${this.port}/${this.database}"
}
