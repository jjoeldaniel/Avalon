package me.joel.commands.guild;

import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

public class WhoIs extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("whois")) {
            Member member = Objects.requireNonNull(event.getOption("user")).getAsMember();

            assert member != null;
            LocalDateTime joinTime = member.getTimeJoined().toLocalDateTime();
            LocalDateTime creationDate = member.getTimeCreated().toLocalDateTime();
            int numRoles = member.getRoles().size();

            StringBuilder roles = new StringBuilder();
            for (int i = 0; i < numRoles; ++i) {
                roles.append("<@&").append(member.getRoles().get(i).getId()).append("> ");
            }

            EmbedBuilder whois = new EmbedBuilder()
                    .setDescription(member.getAsMention())
                    .setAuthor(member.getUser().getName() + "#" + member.getUser().getDiscriminator(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl())
                    .setThumbnail(member.getEffectiveAvatarUrl())
                    .addField("Joined", joinTime.getMonthValue() + "/" + joinTime.getDayOfMonth() + "/" + joinTime.getYear(), true)
                    .addField("Created", creationDate.getMonthValue() + "/" + creationDate.getDayOfMonth() + "/" + creationDate.getYear(), true)
                    .addField("Roles [" + numRoles + "]", roles.toString(), false)
                    .setFooter("ID: " + member.getId())
                    .setColor(Util.randColor());

            event.replyEmbeds(whois.build()).queue();
        }
    }

    @Override
    public void onUserContextInteraction(@NotNull UserContextInteractionEvent event) {
        try {

            var invoke = event.getName();

            if (invoke.equals("Get member info")) {
                Member member = event.getTargetMember();
                assert member != null;

                LocalDateTime joinTime = member.getTimeJoined().toLocalDateTime();
                LocalDateTime creationDate = member.getTimeCreated().toLocalDateTime();

                int numRoles = member.getRoles().size();
                StringBuilder roles = new StringBuilder();
                for (int i = 0; i < numRoles; ++i) {
                    roles.append("<@&").append(member.getRoles().get(i).getId()).append("> ");
                }

                EmbedBuilder whois = new EmbedBuilder()
                        .setDescription(member.getAsMention())
                        .setAuthor(member.getUser().getName() + "#" + member.getUser().getDiscriminator(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl())
                        .setThumbnail(member.getEffectiveAvatarUrl())
                        .addField("Joined", joinTime.getMonthValue() + "/" + joinTime.getDayOfMonth() + "/" + joinTime.getYear(), true)
                        .addField("Created", creationDate.getMonthValue() + "/" + creationDate.getDayOfMonth() + "/" + creationDate.getYear(), true)
                        .addField("Roles [" + numRoles + "]", roles.toString(), false)
                        .setFooter("ID: " + member.getId())
                        .setColor(Util.randColor());

                event.replyEmbeds(whois.build()).queue();
            }

        } catch (Exception e) {
            event.replyEmbeds(Util.genericError().build()).setEphemeral(true).queue();
        }
    }
}
