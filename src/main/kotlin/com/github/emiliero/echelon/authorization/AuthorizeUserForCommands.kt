package com.github.emiliero.echelon.authorization

import com.github.emiliero.echelon.queue.StudentAssistant

fun isUserAuthorizedForStudentAssistantCommands(username: String, discriminator: String): Boolean {
    for (enum in StudentAssistant.values()) {
        if (enum.studentAssistantUsername == username && enum.toString() == discriminator) {
            return true
        }
    }
    return false
}