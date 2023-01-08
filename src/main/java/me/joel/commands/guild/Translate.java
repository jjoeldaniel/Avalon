package me.joel.commands.guild;

import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.Color;

import org.jetbrains.annotations.NotNull;

import com.google.cloud.translate.Translation;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;

public class Translate extends ListenerAdapter {

    @Override
    public void onMessageContextInteraction(@NotNull MessageContextInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("Translate message")) {

            String text = event.getTarget().getContentDisplay();
            event.deferReply().queue();

            try {
                EmbedBuilder builder = translate(text);
                event.getHook().sendMessageEmbeds(builder.build()).queue();
            } 
            catch (Exception e) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Color.RED)
                        .setDescription("An error occurred while translating the message. Please try again later.");
                event.getHook().sendMessageEmbeds(builder.build()).queue();
            }
        }
    }

    /**
     * Translates messages using Google API (limit of 16,000 characters daily)
     * @param text Message pre-translation
     * @return Translated text
     */
    private static EmbedBuilder translate(String text) {

        var translate = TranslateOptions.getDefaultInstance().getService();
        
        String originLanguage = translate.detect(text).getLanguage();

        if (originLanguage.equals("en")) {
            EmbedBuilder builder = new EmbedBuilder()
                    .setColor(Util.randColor())
                    .setDescription("The message is already in English!");

            return builder;
        }

        String targetLanguage = "en";
        
        Translation translation = translate.translate(
            text,
            TranslateOption.sourceLanguage(originLanguage),
            TranslateOption.targetLanguage("en")
        );

        String translatedText = translation.getTranslatedText();

        EmbedBuilder builder = new EmbedBuilder()
                .setColor(Util.randColor())
                .setTitle("Translation")
                .addField("Original [" + originLanguage + "]", text, false)
                .addField("Translation [" + targetLanguage + "]", translatedText, false)
                .setFooter("Powered by Google Translate");

        return builder;

    }
}
