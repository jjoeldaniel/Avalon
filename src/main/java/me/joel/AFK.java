package me.joel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AFK extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        if (event.getName().equals("afk")) {

            try {

                Member member = event.getMember();
                assert member != null;

                // Return from AFK
                if (member.getEffectiveName().startsWith("(AFK)")) {
                    System.out.println("AFK Member returned");
                    String user = Objects.requireNonNull(event.getMember()).getEffectiveName();
                    StringBuilder username = new StringBuilder()
                            .append(user)
                            .delete(0, 5);
                    member.modifyNickname(username.toString()).queue();

                    EmbedBuilder builder = new EmbedBuilder()
                            .setDescription("Welcome back, " + event.getMember().getAsMention() + "!")
                            .setColor(Util.randColor());

                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                    return;
                }
                if (!member.getEffectiveName().startsWith("(AFK)")) {
                    String newName = "(AFK) " + member.getEffectiveName();

                    EmbedBuilder builder = new EmbedBuilder()
                            .setDescription("Your AFK status has been set, " + event.getMember().getAsMention() + "!")
                            .setColor(Util.randColor());

                    member.modifyNickname(newName).queue();
                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                }


            } catch (Exception ignore) {}

        }
    }
}
