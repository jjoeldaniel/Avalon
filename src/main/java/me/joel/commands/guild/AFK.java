package me.joel.commands.guild;

import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AFK extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("afk")) {

            try {
                Member member = event.getMember();
                assert member != null;

                // Check for admin/owner
                if (member.getPermissions().contains(Permission.ADMINISTRATOR) || member.isOwner()) {

                    EmbedBuilder builder = new EmbedBuilder()
                            .setDescription("Owner/Admins cannot use /afk!")
                            .setColor(Util.randColor());

                    event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                    return;
                }

                // Return from AFK
                if (member.getEffectiveName().startsWith("(AFK)")) {
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

            }
            // Exception Catch
            catch (Exception exception) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setDescription("Unknown error occurred, try again later!")
                        .setFooter("Make sure Avalons role is set as high as possible in the role hierarchy if this error continues to occur!")
                        .setColor(Util.randColor());

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
            }
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if (!event.getAuthor().isBot() && event.isFromGuild()) {

            // AFK Member
            Member member;

            // Get member, return if null;
            try {
                member = event.getMessage().getMentions().getMembers().get(0);
            }
            catch (Exception e) { return; }

            // AFK Mention
            if (member.getEffectiveName().contains("(AFK)")) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setDescription("Mentioned member is AFK, " + Objects.requireNonNull(event.getMember()).getAsMention() + "!")
                        .setColor(Util.randColor());

                event.getChannel().sendMessageEmbeds(builder.build()).queue();
            }
        }
    }
}
