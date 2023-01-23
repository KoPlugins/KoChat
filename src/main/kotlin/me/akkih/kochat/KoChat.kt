package me.akkih.kochat

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import me.akkih.kochat.commands.BaseCommand
import me.akkih.kochat.config.enums.ConfigMessage
import me.akkih.kochat.listeners.MessageListener
import me.akkih.kochat.listeners.PlayerListener
import me.akkih.kochat.utils.ChatUtil
import me.akkih.kochat.utils.JsonUtil
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

internal var isPluginUpdated = true
lateinit var main: KoChat

fun CommandSender.sendConfigMessage(msg: ConfigMessage) = sendMessage(ChatUtil.format(msg.get()))
fun CommandSender.sendConfigMessage(msg: ConfigMessage, vararg placeholders: Pair<String, String>) {
    var mess = msg.get()
    for ((key, value) in placeholders) mess = mess.replace(key, value)
    sendMessage(ChatUtil.format(mess))
}

class KoChat : JavaPlugin() {

    override fun onEnable() {
        logger.info("Starting KoChat...")

        main = this

        createResources()
        BaseCommand()
        checkIsPluginUpdated()
        registerListeners()

        logger.info("KoChat started successfully!")
    }

    private fun createResources() {
        if (!dataFolder.exists()) dataFolder.mkdir()
        saveDefaultConfig()

        saveResource("messages${File.separator}en_us.yml", false)
        saveResource("messages${File.separator}pt_br.yml", false)
    }

    private fun checkIsPluginUpdated() {
        Bukkit.getScheduler().runTaskAsynchronously(this, Runnable {
            val mapper = ObjectMapper()

            val root: JsonNode = try {
                mapper.readValue(JsonUtil.getDataJSON(), JsonNode::class.java)
            } catch (error: JsonProcessingException) {
                throw RuntimeException(error)
            }

            if (root.get("latestVersion").toString() != description.version) {
                logger.severe("You are running an outdated version of KoChat, please update to the latest version!")
                isPluginUpdated = false
            }
        })
    }

    private fun registerListeners() {
        logger.info("Registering listeners...")
        val manager = Bukkit.getPluginManager()

        manager.registerEvents(MessageListener(), this)
        manager.registerEvents(PlayerListener(), this)
        logger.info("Listeners registered successfully!")
    }

}