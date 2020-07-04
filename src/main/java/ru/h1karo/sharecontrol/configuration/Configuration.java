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

package ru.h1karo.sharecontrol.configuration;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

abstract class Configuration {
    private final File file;
    private final YamlConfiguration config;

    Configuration(File folder, String path) {
        this.file = new File(folder, path);
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    abstract public void init();

    protected Object get(String path, Object def) {
        return this.config.get(path, def);
    }

    <T extends Enum<T>> T getEnum(String path, T def) {
        String value = this.config.getString(path);
        if (value != null) {
            return T.valueOf(def.getDeclaringClass(), value);
        } else return null;
    }

    public void save() throws IOException {
        this.config.save(this.file);
    }
}