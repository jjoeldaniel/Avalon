package me.joel.commands.guild;

import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Mock extends ListenerAdapter {

    @Override
    public void onMessageContextInteraction(@NotNull MessageContextInteractionEvent event) {

        if (event.getName().equals("Mock")) {
            String message = event.getInteraction().getTarget().getContentRaw().strip().toLowerCase();

            if (message.length() > 256) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("`Mock` cannot be used on messages longer than 256 characters.");

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            StringBuilder mock = new StringBuilder("");

            for (int i = 0; i < message.length(); i++) {

                String character = String.valueOf(message.charAt(i));

                // capitalize letters at even index
                if (i % 2 == 0) {
                    mock.append(character.toUpperCase());
                }
                else {
                    mock.append(character);
                }

            }

            EmbedBuilder builder = new EmbedBuilder()
                    .setColor(Util.randColor())
                    .setThumbnail("https://i.imgur.com/GADKV9C.png?width=128px")
                    .addField("Mock", mock.toString(), false);

            event.replyEmbeds(builder.build()).queue();
        }
    }
}
