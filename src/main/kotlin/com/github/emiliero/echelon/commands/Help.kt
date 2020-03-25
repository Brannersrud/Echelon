package com.github.emiliero.echelon.commands

import discord4j.core.DiscordClient
import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.MessageChannel
import discord4j.core.`object`.entity.User
import discord4j.core.event.domain.message.MessageCreateEvent

fun printHelp(client: DiscordClient) {
    client.eventDispatcher.on(MessageCreateEvent::class.java)
        .map { obj: MessageCreateEvent -> obj.message }
        .filter { message: Message ->
            message.author.map { user: User -> !user.isBot}.orElse(false)
        }
        .filter { message: Message ->
            message.content.orElse("").contains(Commands.Help.commandString, ignoreCase = true)
        }.flatMap { m : Message -> m.channel }
        .flatMap<Message> { channel: MessageChannel -> channel.createMessage(printCommands())}
        .subscribe()
}

private fun printCommands() : String {
    var result : String = "> **COMMAND LIST** \n"

    for(enum in Commands.values()) {
        result += "> $enum \n"
        result += "> ```" + enum.commandString + " ```\n"
    }

    return result
}