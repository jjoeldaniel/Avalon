package me.joel;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Music extends ListenerAdapter {

    String prefix = "paw";

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        // Check if from guild/bot & starts with prefix
        if (event.getMessage().isFromGuild() && !event.getAuthor().isBot() && event.getMessage().getContentRaw().startsWith(prefix)) {

            String messageSent = event.getMessage().getContentRaw().toLowerCase();
            String[]botInput = messageSent.split(" ", 4);

            // Check for 'play'
            if (botInput[1].equals("play")) {

                Guild guild = event.getGuild();
                AudioChannel channel = Objects.requireNonNull(event.getMember()).getVoiceState().getChannel();
                AudioManager audioManager = guild.getAudioManager();
                audioManager.openAudioConnection(channel);

                audioManager.setSendingHandler(new Handler());
                audioManager.openAudioConnection(channel);
                audioManager.setSelfDeafened(true);

            }

        }

    }
}
