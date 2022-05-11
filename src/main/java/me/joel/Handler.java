package me.joel;

import net.dv8tion.jda.api.audio.AudioSendHandler;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

public class Handler implements AudioSendHandler {
    @Override
    public boolean canProvide() {
        return false;
    }

    @Nullable
    @Override
    public ByteBuffer provide20MsAudio() {
        return null;
    }
}
