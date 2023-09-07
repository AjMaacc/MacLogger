package com.maclogger.logging

import com.maclogger.MacLogger.Companion.log
import com.maclogger.MacLogger.Companion.logger
import com.maclogger.MacLogger.Companion.ppAPI
import org.black_ixx.playerpoints.event.PlayerPointsChangeEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class PlayerPointsLogging : Listener {
    @EventHandler
    fun playerPointsChange(e : PlayerPointsChangeEvent){
        val uuid = e.playerId
        val player = Bukkit.getOfflinePlayer((uuid))
        val name = player.name
        val change = e.change
        val currentPoints = "%,d".format(ppAPI?.look(uuid))
        val localDate = LocalDateTime.now(ZoneId.of("America/New_York")) //For reference
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a")
        val time = localDate.format(formatter)
        val diff = "%,d".format(ppAPI?.look(uuid)?.plus(change))
        if (change >= 0){
            log("PlayerPoints", "[$time] [+] $name | $currentPoints points -> $diff points")
            logger().info("Logged PlayerPoints change!")
        }
        else {
            log("PlayerPoints", "[$time] [-] $name | $currentPoints points -> $diff points")
            logger().info("Logged PlayerPoints change!")
        }
    }
}