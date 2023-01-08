package me.joel.commands.global;

import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class Help extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("help")) {
            EmbedBuilder builder = help(1);

            event.replyEmbeds(builder.build()).setEphemeral(true)
                    .addActionRow(
                            Button.success("helpGeneral", "General").asDisabled(),
                            Button.success("helpReminder", "Reminders"),
                            Button.success("helpMod", "Moderation"),
                            Button.success("helpMusic", "Music"),
                            Button.success("helpConfig", "Config"))
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
                                Button.success("helpReminder", "Reminders"),
                                Button.success("helpMod", "Moderation"),
                                Button.success("helpMusic", "Music"),
                                Button.success("helpConfig", "Config"))
                        .queue();
            }
            case ("helpReminder") -> {
                EmbedBuilder builder = help(5);

                event.editMessageEmbeds(builder.build())
                        .setActionRow(
                                Button.success("helpGeneral", "General"),
                                Button.success("helpReminder", "Reminders").asDisabled(),
                                Button.success("helpMod", "Moderation"),
                                Button.success("helpMusic", "Music"),
                                Button.success("helpConfig", "Config"))
                        .queue();
            }
            case ("helpMod") -> {
                EmbedBuilder builder = help(2);

                event.editMessageEmbeds(builder.build())
                        .setActionRow(
                                Button.success("helpGeneral", "General"),
                                Button.success("helpReminder", "Reminders"),
                                Button.success("helpMod", "Moderation").asDisabled(),
                                Button.success("helpMusic", "Music"),
                                Button.success("helpConfig", "Config"))
                        .queue();
            }
            case ("helpMusic") -> {
                EmbedBuilder builder = help(3);

                event.editMessageEmbeds(builder.build())
                        .setActionRow(
                                Button.success("helpGeneral", "General"),
                                Button.success("helpReminder", "Reminders"),
                                Button.success("helpMod", "Moderation"),
                                Button.success("helpMusic", "Music").asDisabled(),
                                Button.success("helpConfig", "Config"))
                        .queue();
            }
            case ("helpConfig") -> {
                EmbedBuilder builder = help(4);

                event.editMessageEmbeds(builder.build())
                        .setActionRow(
                                Button.success("helpGeneral", "General"),
                                Button.success("helpReminder", "Reminders"),
                                Button.success("helpMod", "Moderation"),
                                Button.success("helpMusic", "Music"),
                                Button.success("helpConfig", "Config").asDisabled())
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
                .addField("General Commands", """
                        `/help`  Lists commands
                        `/ping`  Pings bot
                        `/coinflip` Flips a coin
                        `/truth` Requests truth
                        `/dare`   Requests dare
                        `/avatar` Retrieves target profile picture
                        `/whois`  Provides user information
                        `/confess` Sends anonymous confession
                        `/report confession` Report a confession by its confession number
                        `/8ball`   Asks the magic 8ball a question""", false);
            case 2 -> builder = new EmbedBuilder()
                .setColor(Util.randColor())
                .setTitle("Avalon Commands")
                .addField("Moderation Commands", """
                        `/purge`  Purges messages (up to 100)
                        `/poll` Submits poll to be voted on
                        `/broadcast`  Sends message as Avalon""", false);
            case 3 -> builder = new EmbedBuilder()
                .setColor(Util.randColor())
                .setTitle("Avalon Commands")
                .addField("Music Commands", """
                        `/play`  Plays YouTube, Spotify, Apple Music
                        `/pause` Pauses playback
                        `/resume` Resumes playback
                        `/clear`  Clears queue
                        `/queue`  Displays song queue
                        `/playing` Displays currently playing song
                        `/join`  Requests for bot to join VC
                        `/leave` Requests for bot to leave VC
                        `/volume`  Sets volume
                        `/loop`  Loops the currently playing song
                        `/shuffle` Shuffles music queue
                        `/skip`  Skips song
                        `/seek`  Seeks song position""", false);
            case 4 -> builder = new EmbedBuilder()
                .setColor(Util.randColor())
                .setTitle("Avalon Commands")
                .addField("Server Configuration", """
                        `/config` Configure server settings
                        `/star` Configure starboard settings
                        `/toggle`  Toggles Avalon features""", false);
            case 5 -> builder = new EmbedBuilder()
                .setColor(Util.randColor())
                .setTitle("Avalon Commands")
                .addField("Reminder Commands", """
                        `/reminder new`  Add a new reminder
                        `/reminder list`  Lists all stored reminders
                        `/reminder reset`  Resets all stored reminders
                        `/reminder delete`  Delete a stored reminder
                        `/reminder toggle`  Toggles notifications""", false);
        }

        return builder;
    }
}
