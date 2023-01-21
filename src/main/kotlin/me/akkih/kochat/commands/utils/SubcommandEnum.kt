package me.akkih.kochat.commands.utils

enum class SubcommandEnum(
    val command: String,
    val commandUsage: String,
    val permission: String
) {

    HELP("Help", "/kochat help", "kochat.commands.help"),
    RELOAD("Reload", "/kochat reload", "kochat.commands.reload"),

}