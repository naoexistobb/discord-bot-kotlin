
import discord4j.core.DiscordClient
import discord4j.core.event.domain.message.MessageCreateEvent
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.mono

fun main(args: Array<String>) {
    val token = args[0]
    val client = DiscordClient.create(token)
    client.withGateway {
        mono {
            it.on(MessageCreateEvent::class.java)
                .asFlow()
                .collect {
                    val message = it.message
                    if(message.content == "!ping"){
                        val channel = message.channel.awaitSingle()
                        channel.createMessage("Pong!").awaitSingle()
                    }
                }
        }
    }.block()
}
