package me.joel.commands.global;

import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Avatar extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("avatar")) {
            String targetPFP;

            // DMs
            if (!event.isFromGuild()) {
                User user = event.getOption("user").getAsUser();
                targetPFP = user.getEffectiveAvatarUrl();

                // Embed
                EmbedBuilder avatar = new EmbedBuilder()
                        .setColor(Util.randColor())
                        .setImage(targetPFP + "?size=256")
                        .setFooter("ID: " + user.getId());

                event.replyEmbeds(avatar.build()).queue();
                return;
            }
            // Server
            Member member = event.getOption("user").getAsMember();
            targetPFP = member.getEffectiveAvatarUrl();

            // Embed
            EmbedBuilder avatar = new EmbedBuilder()
                    .setColor(Util.randColor())
                    .setImage(targetPFP + "?size=256")
                    .setFooter("ID: " + member.getId());

            event.replyEmbeds(avatar.build()).queue();
        }
    }

    @Override
    public void onUserContextInteraction(@NotNull UserContextInteractionEvent event) {
        var invoke = event.getName();

        if (invoke.equals("Get member avatar")) {
            Member member = event.getTargetMember();
            String targetPFP;

            targetPFP = member.getEffectiveAvatarUrl();

            // Embed
            EmbedBuilder avatar = new EmbedBuilder()
                    .setDescription(member.getAsMention())
                    .setColor(Util.randColor())
                    .setImage(targetPFP + "?size=256")
                    .setFooter("ID: " + member.getId());

            event.replyEmbeds(avatar.build()).queue();
        }
        else if (invoke.equals("Get user avatar")) {
            Member member = event.getTargetMember();
            String targetPFP;

            targetPFP = member.getUser().getAvatarUrl();

            // Embed
            EmbedBuilder avatar = new EmbedBuilder()
                    .setDescription(member.getAsMention())
                    .setColor(Util.randColor())
                    .setImage(targetPFP + "?size=256")
                    .setFooter("ID: " + member.getId());

            event.replyEmbeds(avatar.build()).queue();
        }
    }
}
