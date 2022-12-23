package periodicallyprogramming.avalon.commands.global;

import periodicallyprogramming.avalon.Util;
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
            String targetName;
            String targetPFP;

            // DMs
            if (!event.isFromGuild()) {
                User user = event.getOption("user").getAsUser();
                targetName = user.getName() + "#" + user.getDiscriminator();
                targetPFP = user.getEffectiveAvatarUrl();

                // Embed
                EmbedBuilder avatar = new EmbedBuilder()
                        .setTitle(targetName)
                        .setColor(Util.randColor())
                        .setImage(targetPFP + "?size=256")
                        .setFooter("ID: " + user.getId());

                event.replyEmbeds(avatar.build()).queue();
                return;
            }
            // Server
            Member member = event.getOption("user").getAsMember();
            assert member != null;
            targetName = member.getEffectiveName() + "#" + member.getUser().getDiscriminator();
            targetPFP = member.getEffectiveAvatarUrl();

            // Embed
            EmbedBuilder avatar = new EmbedBuilder()
                    .setTitle(targetName)
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
            String targetName;
            String targetPFP;

            assert member != null;
            targetName = member.getEffectiveName() + "#" + member.getUser().getDiscriminator();
            targetPFP = member.getEffectiveAvatarUrl();

            // Embed
            EmbedBuilder avatar = new EmbedBuilder()
                    .setTitle(targetName)
                    .setDescription(member.getAsMention())
                    .setColor(Util.randColor())
                    .setImage(targetPFP + "?size=256")
                    .setFooter("ID: " + member.getId());

            event.replyEmbeds(avatar.build()).queue();
        }
        else if (invoke.equals("Get user avatar")) {
            Member member = event.getTargetMember();
            String targetName;
            String targetPFP;

            assert member != null;
            targetName = member.getEffectiveName() + "#" + member.getUser().getDiscriminator();
            targetPFP = member.getUser().getAvatarUrl();

            // Embed
            EmbedBuilder avatar = new EmbedBuilder()
                    .setTitle(targetName)
                    .setDescription(member.getAsMention())
                    .setColor(Util.randColor())
                    .setImage(targetPFP + "?size=256")
                    .setFooter("ID: " + member.getId());

            event.replyEmbeds(avatar.build()).queue();
        }
    }
}
