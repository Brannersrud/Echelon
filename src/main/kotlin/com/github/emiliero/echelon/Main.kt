package com.github.emiliero.echelon

import com.github.emiliero.echelon.authorization.login
import com.github.emiliero.echelon.commands.printHelp
import com.github.emiliero.echelon.commands.queueActions
import discord4j.core.DiscordClient
import discord4j.core.DiscordClientBuilder


fun main(args: Array<String>) {
    val client: DiscordClient = DiscordClientBuilder(System.getenv("TOKEN_KEY")).build()

    login(client)
    printHelp(client) //Help command
    queueActions(client) //Queue commands
    client.login().block()





}

