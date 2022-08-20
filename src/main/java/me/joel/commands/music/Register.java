package me.joel.commands.music;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Register extends ListenerAdapter {

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {

        List<CommandData> musicCommandData = new ArrayList<>();

        musicCommandData.add(Commands.slash("play", "Requests a song").addOption(OptionType.STRING, "song", "Accepts Spotify, YouTube, or Apple Music", true));
        musicCommandData.add(Commands.slash("pause", "Pause playback"));
        musicCommandData.add(Commands.slash("resume", "Resume playback"));
        musicCommandData.add(Commands.slash("clear", "Clears queue"));
        musicCommandData.add(Commands.slash("queue", "Displays music queue"));
        musicCommandData.add(Commands.slash("playing", "Displays currently playing song"));
        musicCommandData.add(Commands.slash("loop", "Loops currently playing song"));
        musicCommandData.add(Commands.slash("shuffle", "Shuffles music queue"));
        musicCommandData.add(Commands.slash("volume", "Sets bot volume").addOption(OptionType.STRING, "num", "Sets volume (between 1 and 100)", true));
        musicCommandData.add(Commands.slash("skip", "Skips song").addOption(OptionType.INTEGER, "target", "Removes selected song from queue", false).addOption(OptionType.INTEGER, "num", "Removes \"x\" number of songs", false));
        musicCommandData.add(Commands.slash("seek", "Sets song position").addOption(OptionType.INTEGER, "seconds", "Sets seconds", false).addOption(OptionType.INTEGER, "minutes", "Sets minutes", false).addOption(OptionType.INTEGER, "hours", "Sets hours", false));

        event.getJDA().addEventListener(new Activity(), new Clear(), new Loop(), new Pause(), new Play(), new Playing(), new Queue(), new Resume(), new Seek(), new Shuffle(), new Skip(), new Util(), new Volume());
        event.getGuild().updateCommands().addCommands(musicCommandData).complete();
    }
}