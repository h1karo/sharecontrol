# ShareControl
This is the official repository of ShareControl Minecraft server plugin. The repository contains only 3.0+ the plugin versions.

![Workflow Status](https://img.shields.io/github/workflow/status/h1karo/sharecontrol/Build%20package?style=for-the-badge)
![License](https://img.shields.io/github/license/h1karo/sharecontrol?style=for-the-badge)
![Release](https://img.shields.io/github/v/release/h1karo/sharecontrol?style=for-the-badge)

[![RuBukkit.org](https://img.shields.io/static/v1?label=RuBukkit&message=ShareControl&color=blue&style=for-the-badge)](http://rubukkit.org/threads/admn-sec-mech-sharecontrol-v2-6-4-kontrol-tvorcheskogo-rezhima-1-7-1-11.106125/)
[![BukkitDev](https://img.shields.io/static/v1?label=BukkitDev&message=ShareControl&color=blue&style=for-the-badge)](https://dev.bukkit.org/projects/sharecontrol)
[![SpigotMC](https://img.shields.io/static/v1?label=SpigotMC&message=ShareControl&color=orange&style=for-the-badge)](https://www.spigotmc.org/resources/sharecontrol.9225/)

## Requirements

1. Java 17+
2. Compatible core (see [below](#server-cores-support-table))

### Server cores support table

| Core name   | Support versions |
|:------------|:----------------:|
| Bukkit      |   1.19-1.19.3    |
| Spigot      |   1.19-1.19.3    |
| PaperSpigot |   1.19-1.19.3    |

## Installation

1. Download the executable plugin file (ShareControl.jar).   
   You can download the latest version directly from [GitHub](https://github.com/h1karo/sharecontrol/releases/latest/download/ShareControl.jar).
2. Move downloaded file into plugins directory of your server (`/plugins/` by default).
3. Start up (or fully restart) server.
4. Configure the plugin via `/plugins/ShareControl/config.yaml` file (see section [Configuration](#configuration)).
5. Run `/sc reload` in console or game.

## Compiling

We use [Maven](https://maven.apache.org/) to compile the plugin. Maven provides all required dependencies.

Install them and run `mvn clean package`. In the `target` directory, you will get the compiled executable jar-file `ShareControl.jar`.