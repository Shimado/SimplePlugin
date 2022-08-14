package me.shimado.simpleplugin

import org.bukkit.Bukkit
import org.bukkit.Server
import org.bukkit.plugin.java.JavaPlugin

class SimplePlugin {

    companion object{
        @JvmStatic
        var version: String = ""
    }

    constructor(server: Server){
        SimplePlugin.version = server.javaClass.getPackage().getName().split("\\.")[3];
    }

}