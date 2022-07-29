package me.shimado.simpleplugin

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class SimplePlugin {

    companion object{
        var version: String = ""
    }

    constructor(version: JavaPlugin){
        SimplePlugin.version = Bukkit::class.java.packageName.split("\\.")[3];
    }

}