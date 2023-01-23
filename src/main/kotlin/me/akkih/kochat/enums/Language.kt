package me.akkih.kochat.enums

enum class Language {

    EN_US,
    PT_BR;

    companion object {
        private val languages = Language.values().map { it.toString() }

        fun exists(language: String): Boolean {
            return languages.contains(language)
        }
    }

}