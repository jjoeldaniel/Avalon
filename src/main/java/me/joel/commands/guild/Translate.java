package me.joel.commands.guild;

import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Translate extends ListenerAdapter {

    @Override
    public void onMessageContextInteraction(@NotNull MessageContextInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("Translate message")) {

            // Attempts translation, ignores and returns null on exception
            String text = event.getTarget().getContentRaw();
            String translation = null;
            try {
                translation = translate(text);
            } catch (IOException ignore) {}

            EmbedBuilder builder = new EmbedBuilder();

            // On Exception
            if (translation == null) {
                builder = Util.genericError();
            }

            // On Success
            else {
                builder.setTitle("Translated Text").setDescription("\"" + translation + "\"").setColor(Util.randColor());
            }

            event.replyEmbeds(builder.build()).setEphemeral(false).queue();
        }
    }

    // Google Translate API
    private static String translate(String text) throws IOException {
        String urlStr = "https://script.google.com/macros/s/AKfycbzCWLTSgDZTKaSyE3sgcb2KTcFDNMvpL1nH1gRXFhMDV3by792WUjxviYk3lPxic7Wf3Q/exec " +
                "?q=" + URLEncoder.encode(text, StandardCharsets.UTF_8) +
                "&target=" + "en" +
                "&source=" + "";
        URL url = new URL(urlStr);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
