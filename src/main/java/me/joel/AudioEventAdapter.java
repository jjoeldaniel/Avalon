package me.joel;

public class AudioEventAdapter extends com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter {

    private static boolean isLoop = false;

    public static void setLoop(boolean value) {
        isLoop = value;
    }

    public static boolean isLooping() {
        return isLoop;
    }

}
