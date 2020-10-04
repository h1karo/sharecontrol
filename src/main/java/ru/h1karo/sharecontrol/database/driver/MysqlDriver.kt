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

package ru.h1karo.sharecontrol.database.driver

import ru.h1karo.sharecontrol.database.annotation.Mysql
import ru.h1karo.sharecontrol.database.config.Configuration
import ru.h1karo.sharecontrol.database.config.UserPasswordConfiguration
import ru.h1karo.sharecontrol.database.exception.DriverException
import java.sql.Connection
import java.sql.DriverManager

@Mysql
class MysqlDriver : Driver {
    override fun validateDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver")
        } catch (e: Exception) {
            throw DriverException("JDBC driver for MySQL not found.", e)
        }
    }

    override fun connect(config: Configuration): Connection {
        if (config !is UserPasswordConfiguration) {
            throw DriverException("MySQL requires username and password to connect, but provided configuration have not.")
        }

        try {
            return DriverManager.getConnection("jdbc:${config.getDsn()}", config.getUser(), config.getPassword())
        } catch (e: Exception) {
            throw DriverException("Error on connect to MySQL.", e)
        }
    }
}
