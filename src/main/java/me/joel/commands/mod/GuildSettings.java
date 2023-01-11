package me.joel.commands.mod;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;

public class GuildSettings extends ListenerAdapter {

    public static HashMap<Guild, Long> starboard_channel = new HashMap<>();
    public static HashMap<Guild, Integer> starboard_limit = new HashMap<>();
    public static HashMap<Guild, Boolean> starboard_self = new HashMap<>();

    public static HashMap<Guild, Long> confession_channel = new HashMap<>();
    public static HashMap<Guild, Long> mod_channel = new HashMap<>();
    public static HashMap<Guild, Long> join_channel = new HashMap<>();
    public static HashMap<Guild, Long> leave_channel = new HashMap<>();

    public static HashMap<Guild, Boolean> insults = new HashMap<>();
    public static HashMap<Guild, Boolean> now_playing = new HashMap<>();
    public static HashMap<Guild, Boolean> gm_gn = new HashMap<>();
}
