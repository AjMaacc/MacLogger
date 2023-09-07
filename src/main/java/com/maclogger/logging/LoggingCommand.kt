package com.maclogger.logging

import com.maclogger.MacLogger
import com.maclogger.MacLogger.Companion.log
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class LoggingCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is ConsoleCommandSender){
            return true
        }
        if (args.size >= 2){
            val fileName = args[0]
            val message = StringBuilder()
            for (i in 1 until args.size) {
                message.append(args[i]).append(" ")
            }
            val localDate = LocalDateTime.now(ZoneId.of("America/New_York")) //For reference
            val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a")
            log(fileName, "[" + localDate.format(formatter) +  "] " + message.toString())
            MacLogger.purchases++
            MacLogger.saveVars()
        }
        return true
    }
}