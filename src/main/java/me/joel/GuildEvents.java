package me.joel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildDeafenEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GuildEvents extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {

        if (event.getMember().getId().equals(event.getJDA().getSelfUser().getId())) {
            Member member = event.getGuild().getSelfMember();
            member.deafen(true).queue();
        }
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        final String inviteLink = "https://discord.com/api/oauth2/authorize?client_id=971239438892019743&permissions=8&scope=applications.commands%20bot";

        EmbedBuilder builder = new EmbedBuilder()
                .setThumbnail(event.getJDA().getSelfUser().getAvatarUrl())
                .setTitle("Thank you for inviting Avalon to " + event.getGuild().getName() + "!")
                .setColor(Util.randColor())
                .setDescription("Make sure to use /help to get the full commands list!")
                .addBlankField(false)
                .addField("Need to contact us?", "Add joel#0005 on Discord for questions!", false)
                .addField("Want to invite Avalon to another server?", "Click on my profile and click \" Add to Server\" to invite Avalon!", false);

        Objects.requireNonNull(event.getGuild().getSystemChannel()).sendMessageEmbeds(builder.build()).setActionRow(
                        Button.link(inviteLink, "Invite"))
                .queue();
    }

    @Override
    public void onGuildVoiceGuildDeafen(@NotNull GuildVoiceGuildDeafenEvent event) {

        if (event.getMember().getId().equals(event.getJDA().getSelfUser().getId())) {
            Member member = event.getGuild().getSelfMember();
            if (Objects.requireNonNull(member.getVoiceState()).inAudioChannel()) {
                member.deafen(true).queue();
            }
        }
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {

        // paw patrol and cat club welcome messages
        if (event.getGuild().getId().equals("645471751316307998") || event.getGuild().getId().equals("971225319153479790")) {

            Member member = event.getMember();
            EmbedBuilder memberJoin = new EmbedBuilder()
                    .setColor(Util.randColor())
                    .setTitle("A new member has joined!")
                    .setDescription
                            (
                                    "Welcome " + member.getAsMention() + " to " + event.getGuild().getName() +
                                            "! There are now " + event.getGuild().getMemberCount() + " members in " + event.getGuild().getName() + "."
                            )
                    .setThumbnail(member.getEffectiveAvatarUrl())
                    .setFooter("User: " + member.getUser().getName() +"#" + member.getUser().getDiscriminator() + " ID: " + member.getId());

            // find welcome channel
            try {

                int channelNum = Objects.requireNonNull(event.getGuild()).getTextChannels().size();
                for (int i = 0; i < channelNum; ++i) {

                    if (event.getGuild().getTextChannels().get(i).getName().contains("welcome")) {

                        event.getGuild().getTextChannels().get(i).sendMessageEmbeds(memberJoin.build()).queue();
                        return;
                    }
                }
            }
            // If no welcomeChannel found
            catch (Exception ignore) {}
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        // Checks if user
        if (event.isFromType(ChannelType.TEXT)) {

            if (!event.getAuthor().isBot() && event.isFromGuild() && !event.getChannel().asTextChannel().isNSFW()) {

                // Grabs user input
                String messageSent = event.getMessage().getContentRaw().toLowerCase();

                // Goodnight
                if (messageSent.contains("goodnight") || messageSent.contains("good night") || messageSent.equalsIgnoreCase("gn") && Util.randomWithRange(0, 100) > 50) {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setDescription("goodnight sweetie!");
                    event.getMessage().replyEmbeds(builder.build()).queue();
                }
                // Good morning
                if (messageSent.contains("goodmorning") || messageSent.contains("good morning") || messageSent.equalsIgnoreCase("gm") && Util.randomWithRange(0, 100) > 50) {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setColor(Util.randColor())
                            .setDescription("good morning sweetie!");
                    event.getMessage().replyEmbeds(builder.build()).queue();
                }
                // Insult
                if (isInsult(messageSent) && Util.randomWithRange(0, 100) > 75) {
                    event.getMessage().replyEmbeds(randomInsult().build()).queue();
                }

            }
        }
    }

    /**
     * @return True if message contains insult
     */
    boolean isInsult(String message) {
            return message.contains("fuck") || (message.contains("cunt")) || (message.contains("prick") || (message.contains("slut")) || (message.contains("asshole")) || (message.contains("bastard")) || (message.contains("twat")) || (message.contains("bitch")) || (message.contains("dick")));
        }
    EmbedBuilder randomInsult() {
        Random rand = new Random();

        List<String> insultList = new ArrayList<>();
        insultList.add("No you");
        insultList.add("Fuck you");
        insultList.add("Your mom");
        insultList.add("Stfu");
        insultList.add("Bruh");
        insultList.add("Dickhead");
        insultList.add("Asshole");
        insultList.add("Idiot");
        insultList.add("You can do better");
        insultList.add("Stfu inbred");
        insultList.add("Bitch pls");
        insultList.add("Shut your mouth");
        insultList.add("You disgust me");
        insultList.add("Fuck off");
        insultList.add("Dumbfuck");
        insultList.add("Dumbass");
        insultList.add("You're dumb");
        insultList.add("Fuck off midget");
        insultList.add("I'll fucking roundhouse kick you in the teeth, dumbfuck");
        insultList.add("Shut the fuck up, literally no one is paying attention");
        insultList.add("Minorly whore");

        int num = rand.nextInt(insultList.size());

        return new EmbedBuilder()
                .setColor(Util.randColor())
                .setDescription(insultList.get(num));
    }

}
