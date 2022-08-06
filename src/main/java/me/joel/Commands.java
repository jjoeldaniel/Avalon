package me.joel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class Commands extends ListenerAdapter {

    final String inviteLink = "https://discord.com/api/oauth2/authorize?client_id=971239438892019743&permissions=8&scope=applications.commands%20bot";

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {

        EmbedBuilder builder = new EmbedBuilder()
                .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                .setTitle("Thank you for inviting Avalon to " + event.getGuild().getName() + "!")
                .setColor(Util.randColor())
                .setDescription("Make sure to use /help to get the full commands list!")
                .addBlankField(false)
                .addField("Need to contact us?", "Add joel#0005 on Discord for questions!", false)
                .addField("Want to invite Avalon to another server?", "Click on my profile and click \" Add to Server\" to invite Avalon!", false);

        Objects.requireNonNull(event.getGuild().getSystemChannel()).sendMessageEmbeds(builder.build()).setActionRow(
                        Button.link(inviteLink, "Invite"))
                .queue();
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        var invoke = event.getComponentId();

        switch (invoke) {

            case ("truth") -> {
                EmbedBuilder builder = TruthOrDare.getTruth();

                event.replyEmbeds(builder.build())
                        .addActionRow(
                                Button.success("truth", "Truth"),
                                Button.success("dare", "Dare"),
                                Button.danger("randomTruthOrDare", "Random")
                        )
                        .queue();
            }
            case ("dare") -> {
                EmbedBuilder builder = TruthOrDare.getDare();

                event.replyEmbeds(builder.build())
                        .addActionRow(
                                Button.success("truth", "Truth"),
                                Button.success("dare", "Dare"),
                                Button.danger("randomTruthOrDare", "Random")
                        )
                        .queue();
            }
            case ("randomtruthordare") -> {
                if (Util.randomWithRange(0, 100) > 50) {
                    EmbedBuilder builder = TruthOrDare.getDare();

                    event.replyEmbeds(builder.build())
                            .addActionRow(
                                    Button.success("truth", "Truth"),
                                    Button.success("dare", "Dare"),
                                    Button.danger("randomtruthordare", "Random")
                            )
                            .queue();
                }
                else {
                    EmbedBuilder builder = TruthOrDare.getTruth();

                    event.replyEmbeds(builder.build())
                            .addActionRow(
                                    Button.success("truth", "Truth"),
                                    Button.success("dare", "Dare"),
                                    Button.danger("randomtruthordare", "Random")
                            )
                            .queue();
                }
            }

        }
    }

}
