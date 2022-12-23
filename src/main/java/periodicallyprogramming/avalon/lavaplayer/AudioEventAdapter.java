package periodicallyprogramming.avalon.lavaplayer;

public class AudioEventAdapter extends com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter {

    // Loop
    private static boolean loop = false;
    public static void setLoop(boolean value) {
        loop = value;
    }
    public static boolean isLooping() {
        return loop;
    }

    // Shuffle
    private static boolean shuffle = false;
    public static void setShuffle(boolean value) {
        shuffle = value;
    }
    public static boolean isShuffling() {
        return shuffle;
    }
}
