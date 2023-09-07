package com.maclogger

import com.maclogger.logging.LoggingCommand
import com.maclogger.logging.PlayerPointsLogging
import org.black_ixx.playerpoints.PlayerPoints
import org.black_ixx.playerpoints.PlayerPointsAPI
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.logging.Logger

class MacLogger : JavaPlugin() {
    override fun onEnable() {
        getCommand("mlog")?.setExecutor(LoggingCommand())
        Bukkit.getServer().pluginManager.registerEvents(PlayerPointsLogging(), this)
        setApi()
        setVars()
        logger.info("Hola")
    }

    override fun onDisable() {
        saveVars()
        logger.info("adiós")
    }

    companion object {
        var ppAPI: PlayerPointsAPI? = null
        var purchases = 0
        var ppUpdates = 0
        fun logger(): Logger {
            return Bukkit.getLogger()
        }

        fun setApi() {
            if (Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")) {
                this.ppAPI = PlayerPoints.getInstance().api
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

        fun setVars() {
            val directoryPath = Bukkit.getServer().pluginManager.getPlugin("MacLogger")?.dataFolder
            val countFileName = "data.yml"

            val directory = File("$directoryPath")
            if (!directory.exists()) {
                directory.mkdirs()
            }
            val countFile = File(directory, countFileName)

            if (!countFile.exists()) {
                // Create the YAML file with an initial "count" value
                val data = mapOf("gsCount" to 0, "ppUpdates" to 0)
                val yamlOptions = DumperOptions()
                yamlOptions.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
                val yaml = Yaml(yamlOptions)
                val yamlString = yaml.dump(data)
                FileWriter(countFile).use { writer ->
                    writer.write(yamlString)
                }
            } else {
                val yaml = Yaml()
                val yamlData = countFile.reader().use { reader -> yaml.load<Map<String, Int>>(reader) }
                purchases = yamlData["gsCount"] ?: 0
                ppUpdates = yamlData["ppUpdates"] ?: 0
                return
            }
            purchases = 0
            ppUpdates = 0
        }
        fun saveVars(): Boolean {
            val directoryPath = Bukkit.getServer().pluginManager.getPlugin("MacLogger")?.dataFolder
            val yaml = Yaml(DumperOptions().apply { defaultFlowStyle = DumperOptions.FlowStyle.BLOCK })
            val filePath = Paths.get(directoryPath.toString(), "data.yml")

            try {
                // Read the existing YAML content
                val content = Files.readString(filePath)

                // Parse the YAML content into a map
                val data = (yaml.load<Map<String, Any>>(content) ?: mutableMapOf()).toMutableMap()

                // Update the "count" variable with the new value
                data["gsCount"] = purchases
                data["ppUpdates"] = ppUpdates

                // Dump the updated data to YAML format
                val updatedYamlContent = yaml.dump(data)

                // Write the updated content back to the file
                Files.write(filePath, updatedYamlContent.toByteArray(), StandardOpenOption.CREATE)

                return true
            } catch (e: IOException) {
                println("Error updating YAML file: ${e.message}")
                return false
            }
        }
    }
}