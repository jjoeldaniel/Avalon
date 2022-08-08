package me.joel.commands.global;

import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class Help extends ListenerAdapter {

    final String inviteLink = "https://discord.com/api/oauth2/authorize?client_id=971239438892019743&permissions=8&scope=applications.commands%20bot";

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("help")) {
            EmbedBuilder builder = help(1);

            event.replyEmbeds(builder.build()).setEphemeral(true)
                    .addActionRow(
                            Button.success("helpGeneral", "General").asDisabled(),
                            Button.success("helpMod", "Moderation"),
                            Button.success("helpMusic", "Music"),
                            Button.link(inviteLink, "Invite"))
                    .queue();
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        var invoke = event.getComponentId();

        switch (invoke) {

            case ("helpGeneral") -> {
                EmbedBuilder builder = help(1);

                event.editMessageEmbeds(builder.build())
                        .setActionRow(
                                Button.success("helpGeneral", "General").asDisabled(),
                                Button.success("helpMod", "Moderation"),
                                Button.success("helpMusic", "Music"),
                                Button.link(inviteLink, "Invite"))
                        .queue();
            }
            case ("helpMod") -> {
                EmbedBuilder builder = help(2);

                event.editMessageEmbeds(builder.build())
                        .setActionRow(
                                Button.success("helpGeneral", "General"),
                                Button.success("helpMod", "Moderation").asDisabled(),
                                Button.success("helpMusic", "Music"),
                                Button.link(inviteLink, "Invite"))
                        .queue();
            }
            case ("helpMusic") -> {
                EmbedBuilder builder = help(3);

                event.editMessageEmbeds(builder.build())
                        .setActionRow(
                                Button.success("helpGeneral", "General"),
                                Button.success("helpMod", "Moderation"),
                                Button.success("helpMusic", "Music").asDisabled(),
                                Button.link(inviteLink, "Invite"))
                        .queue();
            }
        }
    }

    /**
     * Help Embed
     * @param setting 1 = General, 2 = Mod, 3 = Music
     * @return Help embed
     */
    public EmbedBuilder help(int setting) {

        EmbedBuilder builder = null;

        switch (setting) {
            case 1 -> builder = new EmbedBuilder()
                    .setColor(Util.randColor())
                    .setTitle("Avalon Commands")
                    .setThumbnail("https://cdn.discordapp.com/avatars/971239438892019743/a40528ce063fc40a62d86d09bb1aa087.png?size=256")
                    .addField("General Commands", """
                            `/help` : Lists commands
                            `/ping` : Pings bot
                            `/coinflip` Flips a coin
                            `/truth` : Requests truth
                            `/dare` : Requests dare
                            `/avatar` : Retrieves target profile picture
                            `/whois` : Provides user information
                            `/afk` : Sets AFK status
                            `/confess` : Sends anonymous confession
                            `/8ball` : Asks the magic 8ball a question""", false);
            case 2 -> builder = new EmbedBuilder()
                    .setColor(Util.randColor())
                    .setTitle("Avalon Commands")
                    .setThumbnail("https://cdn.discordapp.com/avatars/971239438892019743/a40528ce063fc40a62d86d09bb1aa087.png?size=256")
                    .addField("Moderation Commands", """
                            `/purge` : Purges messages (up to 100)
                            `/broadcast` : Sends message as Avalon""", false);
            case 3 -> builder = new EmbedBuilder()
                    .setColor(Util.randColor())
                    .setTitle("Avalon Commands")
                    .setThumbnail("https://cdn.discordapp.com/avatars/971239438892019743/a40528ce063fc40a62d86d09bb1aa087.png?size=256")
                    .addField("Music Commands", """
                            `/play` : Plays YouTube, Spotify, Apple Music
                            `/pause` : Pauses playback
                            `/resume` : Resumes playback
                            `/clear` : Clears queue
                            `/queue` : Displays song queue
                            `/playing` : Displays currently playing song
                            `/join` : Requests for bot to join VC
                            `/leave` : Requests for bot to leave VC
                            `/volume` : Sets volume
                            `/loop` : Loops the currently playing song
                            `/shuffle` : Shuffles music queue
                            `/skip` : Skips song""", false);
        }

        return builder;
    }
}
