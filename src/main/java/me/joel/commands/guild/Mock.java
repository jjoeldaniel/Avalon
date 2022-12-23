package me.joel.commands.guild;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import me.joel.Util;

public class Mock extends ListenerAdapter {

    @Override
    public void onMessageContextInteraction(@NotNull MessageContextInteractionEvent event) {

        if (event.getName().equals("Mock")) {
            String message = event.getInteraction().getTarget().getContentStripped();

            if (message.length() > 256) {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription("`Mock` cannot be used on messages longer than 256 characters.");

                event.replyEmbeds(builder.build()).setEphemeral(true).queue();
                return;
            }

            StringBuilder mock = new StringBuilder();

            for (int i = 0; i < message.length(); i++) {

                String character = String.valueOf(message.charAt(i));

                // capitalize letters at even index
                if (i % 2 == 0) {
                    mock.append(character.toUpperCase());
                }
                else {
                    mock.append(character);
                }

            }

            try
            {
                String url = createMock( mock.toString() );

                EmbedBuilder builder = new EmbedBuilder()
                        .setColor(Util.randColor())
                        .setImage( url );

                event.replyEmbeds(builder.build()).queue();
            }
            catch ( IOException e )
            {
                EmbedBuilder builder = new EmbedBuilder()
                        .setColor( Color.red )
                        .setDescription( "Unknown error occured, try again later!" );

                event.replyEmbeds( builder.build() ).setEphemeral( true ).queue();
            }
        }
    }

    private String createMock(String input) throws IOException
    {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("https://api.imgflip.com/caption_image");

        var arr = input.split(" ");
        final int mid = arr.length / 2;

        StringBuilder topText = new StringBuilder();
        StringBuilder bottomText = new StringBuilder();

        for (int i = 0; i < mid; i++) {
            topText.append( arr[i] ).append( " " );
        }

        for (int i = mid; i < arr.length; i++) {
            bottomText.append( arr[i] ).append( " " );
        }

        // Request parameters and other properties
        List<NameValuePair> params = new ArrayList<>(5);
        params.add(new BasicNameValuePair("template_id", "102156234"));
        params.add(new BasicNameValuePair("username", "JoelRico"));
        params.add(new BasicNameValuePair("password", "U%y#qsACd%PXuSK$4o68"));
        params.add(new BasicNameValuePair("text0", topText.toString()));
        params.add(new BasicNameValuePair("text1", bottomText.toString()));
        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        // Execute and get the response
        HttpResponse response = httpClient.execute(httppost);

        String jsonString = EntityUtils.toString(response.getEntity());
        JSONObject jsonObject = new JSONObject(jsonString);

        return jsonObject.getJSONObject( "data" ).getString( "url" );
    }
}
