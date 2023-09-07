package com.maclogger

import com.maclogger.logging.LoggingCommand
import com.maclogger.logging.PlayerPointsLogging
import org.black_ixx.playerpoints.PlayerPoints
import org.black_ixx.playerpoints.PlayerPointsAPI
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.logging.Logger

class MacLogger : JavaPlugin() {
    override fun onEnable() {
        getCommand("mlog")?.setExecutor(LoggingCommand())
        Bukkit.getServer().pluginManager.registerEvents(PlayerPointsLogging(), this)
        setApi()
    }

    override fun onDisable() {
        logger.info("adiós")
    }

    companion object {
        var ppAPI: PlayerPointsAPI? = null
        fun logger(): Logger {
            return Bukkit.getLogger()
        }

        fun setApi(){
            if (Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")) {
                this.ppAPI = PlayerPoints.getInstance().api;
                logger().info("Hooked into PlayerPoints!")
            }
        }
        fun log(fileName: String, message: String) {
            val path = Bukkit.getServer().pluginManager.getPlugin("MacLogger")?.dataFolder
            val directoryPath = File("$path/logs")
            val filePath = File(directoryPath, "$fileName.txt")

            try {
                if (!directoryPath.exists()) {
                    directoryPath.mkdirs()
                }
                val writer = FileWriter(filePath, true)
                writer.write(message + System.lineSeparator())
                writer.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}