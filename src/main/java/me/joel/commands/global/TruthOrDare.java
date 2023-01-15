package me.joel.commands.global;

import me.joel.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import org.jetbrains.annotations.NotNull;

public class TruthOrDare extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var invoke = event.getName();

        if (invoke.equals("truthordare")) {

            var sub_invoke = event.getSubcommandName();
            EmbedBuilder builder = null;

            switch (sub_invoke) {
                case ("truth") -> builder = getTruth();
                case ("dare") -> builder = getDare();
                case ("random") -> {
                    if (Util.randomWithRange(0, 100) > 50) builder = getDare();
                    else builder = getTruth();
                }
            }

            assert builder != null;
            event.replyEmbeds(builder.build())
                    .addActionRow(
                            Button.success("truth", "Truth"),
                            Button.success("dare", "Dare"),
                            Button.danger("randomTruthOrDare", "Random")
                    )
                    .queue();
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        var invoke = event.getComponentId();
        EmbedBuilder builder = null;

        if (invoke.equals("truth") || invoke.equals("dare") || invoke.equals("randomTruthOrDare")) {

            switch (invoke) {
                case ("truth") -> builder = getTruth();
                case ("dare") -> builder = getDare();
                case ("randomTruthOrDare") -> {
                    if (Util.randomWithRange(0, 100) > 50) builder = getDare();
                    else builder = getTruth();
                }
            }

            builder.setFooter("Member: " + event.getMember().getUser().getName() + "#" + event.getMember().getUser().getDiscriminator());
            event.editMessageEmbeds(builder.build())
                    .setActionRow(
                            Button.success("truth", "Truth"),
                            Button.success("dare", "Dare"),
                            Button.danger("randomTruthOrDare", "Random")
                    )
                    .queue();
        }
    }

    /**
     * Truth Generator
     * @return Random truth
     */
    private static String truth() {
        int num = Util.randomWithRange(0, 100);
        String[] truth = new String[101];

        truth[0] = "Whats the last lie you told?";
        truth[1] = "What was the most embarrassing thing youve ever done on a date?";
        truth[2] = "Have you ever accidentally hit something (or someone!) with your car?";
        truth[3] = "Name someone youve pretended to like but actually couldnt stand.";
        truth[4] = "Whats been your most physically painful experience?";
        truth[5] = "What bridges are you glad that you burned?";
        truth[6] = "If you met a genie, what would your three wishes be?";
        truth[7] = "Whats the meanest thing youve ever said to someone else?";
        truth[8] = "Who was your worst kiss ever?";
        truth[9] = "Whats one thing youd do if you knew there no consequences?";
        truth[10] = "Whats the meanest thing youve ever said about someone else?";
        truth[11] = "Who are you most jealous of?";
        truth[12] = "Would you date your high school crush today?";
        truth[13] = "Do you believe in any superstitions? If so, which ones?";
        truth[14] = "Have you ever considered cheating on a partner?";
        truth[15] = "Have you ever cheated on a partner?";
        truth[16] = "What app do you waste the most time on?";
        truth[17] = "What is the youngest age partner youd date?";
        truth[18] = "Have you ever lied about your age?";
        truth[19] = "Have you ever used a fake ID?";
        truth[20] = "Whats your favorite food?";
        truth[21] = "What is your guilty pleasure?";
        truth[22] = "What is your biggest pet peeve?";
        truth[23] = "What was your most embarrassing moment in public in front of everyone?";
        truth[24] = "Is there anything in your life that you would change?";
        truth[25] = "Do you currently have a crush on anyone?";
        truth[26] = "Do you Whats the most childish thing you still do? have a crush on anyone?";
        truth[27] = "Whats the most childish thing you still do?";
        truth[28] = "Whens the last time you dumped someone?";
        truth[29] = "Have you ever lied for a friend?";
        truth[30] = "Would you date someone shorter than you?";
        truth[31] = "If you could become invisible, whats the worst thing youd do?";
        truth[32] = "After youve dropped a piece of food, whats the longest time youve left it on the floor before eating it?";
        truth[33] = "Whats something that overwhelms you?";
        truth[34] = "What was the greatest day of your life?";
        truth[35] = "How many people have you kissed?";
        truth[36] = "Whats your most absurd deal-breaker?";
        truth[37] = "Whats the scariest thing thats ever happened to you?";
        truth[38] = "Whats the last purchase you regretted?";
        truth[39] = "Who was your first love?";
        truth[40] = "How often do you wash your sheets?";
        truth[41] = "Have you ever pretended to not get a text to get out of doing something?";
        truth[42] = "Whats your most embarrassing childhood memory?";
        truth[43] = "Have you ever been in a fight?";
        truth[44] = "Whats the worst advice youve ever given someone else?";
        truth[45] = "Whats the worst advice someone else has ever given you?";
        truth[46] = "Is there an ex with whom youd consider reconciling?";
        truth[47] = "Whats the weirdest thing you do while driving?";
        truth[48] = "Whens the last time you wanted to hit somebody?";
        truth[49] = "Whos the last person who called you?";
        truth[50] = "When was the last time you were really angry? Why?";
        truth[51] = " Whats your favorite guilty pleasure song?";
        truth[52] = "Would you ever get plastic surgery?";
        truth[53] = "Have you ever intentionally sabotaged a friend?";
        truth[54] = "Whats a skill you wish you had?";
        truth[55] = "Have you ever compromised your morals for money? How?";
        truth[56] = "Who was the last person you said, “I love you” to?";
        truth[57] = "What was your first heartbreak?";
        truth[58] = "Have you ever broken someones heart?";
        truth[59] = "When and where was your first kiss? Who was it with?";
        truth[60] = "Whens the last time you got rejected?";
        truth[61] = "When did you stop believing in Santa Claus?";
        truth[62] = "Whats the most bogus rumor youve ever heard about yourself?";
        truth[63] = "When and where was your first kiss? Who was it with?";
        truth[64] = "Do you think cheating can ever be justified? How?";
        truth[65] = "Have you ever seriously injured another person?";
        truth[66] = "Whats the scariest thing youve ever done?";
        truth[67] = "Have you ever had a paranormal experience?";
        truth[68] = "Have you ever gotten blackout drunk?";
        truth[69] = "Do you believe in aliens?";
        truth[70] = "Whats the pettiest thing youve ever done?";
        truth[71] = "Have you ever gone skinny dipping?";
        truth[72] = "Whats something youve done that you still feel guilty about?";
        truth[73] = "What is the worst date youve ever been on?";
        truth[74] = "Whats the weirdest thing youve ever collected?";
        truth[75] = "Whats the weirdest thing youve ever said to a stranger?";
        truth[76] = "Who in this server do you trust the least?";
        truth[77] = "Whats the most inappropriate time youve ever laughed?";
        truth[78] = "Whats your best pickup line?";
        truth[79] = "Whats the weirdest place youve ever given or gotten someones number?";
        truth[80] = "Whats the dumbest thing youve ever lied about?";
        truth[81] = "Have you ever said, “I love you” and not really meant it? To whom?";
        truth[82] = "Whats your least favorite memory from high school?";
        truth[83] = "Whats the worst present someone has ever given you?";
        truth[84] = "Whats the worst present youve ever given someone else?";
        truth[85] = "Whats the cruelest thing youve ever done or said to a romantic partner?";
        truth[86] = "Whats something youre embarrassed that youre good at?";
        truth[87] = "What was your most humbling moment?";
        truth[88] = "Have you ever let someone take the blame for something you did?";
        truth[89] = "Whats the most ridiculous thing you have an emotional attachment to?";
        truth[90] = "Whats something youve done that youd judge someone else for doing?";
        truth[91] = "Whats something weird you do in your sleep?";
        truth[92] = "Whos your hero?";
        truth[93] = "Whats something you know you need to do but arent looking forward to at all?";
        truth[94] = "Whats the biggest secret youve kept from your parents?";
        truth[95] = "What are you most proud of in your life?";
        truth[96] = "If you were rescuing people from a burning building and you had to leave one person behind from this server, who would it be?";
        truth[97] = "Whats the most offensive joke youve found funny?";
        truth[98] = "Whens the last time you lurked an ex on social media?";
        truth[99] = "Who would you bring with you on a deserted island?";
        truth[100] = "What was your most humbling moment?";

        return truth[num];
    }

    /**
     * Dare Generator
     * @return Random dare
     */
    private static String dare() {
        int num = Util.randomWithRange(0, 23);
        String[] dare = new String[24];

        dare[0] = "Describe the most attractive quality of every online person.";
        dare[1] = "Do 25 sit ups without stopping.";
        dare[2] = "Send a screenshot of your search history from the past two days.";
        dare[3] = "Let the person above you pick your profile picture for the next 24 hours.";
        dare[4] = "Send the 11th picture in your photo gallery.";
        dare[5] = "Let the person above you pick your discord status for the next 24 hours.";
        dare[6] = "Show everyone a screenshot of your most recent DMs.";
        dare[7] = "Say two honest things about everyone else in the group.";
        dare[8] = "Tell everyone an embarrassing story about yourself.";
        dare[9] = "Pretend to be the person above for 10 minutes.";
        dare[10] = "Send the most recent photo in your camera roll.";
        dare[11] = "Send your most played Spotify playlist.";
        dare[12] = "Seduce a member of the same gender in the group.";
        dare[13] = "Commit arson...";
        dare[14] = "Start a conversation with a random person on your discord friends list (AND SHARE IT).";
        dare[15] = "End each sentence with the word “not” until your next turn.";
        dare[16] = "Name a famous person that looks like each player.";
        dare[17] = "Lick a bar of soap.";
        dare[18] = "Go to sleep.";
        dare[19] = "Beg and plead the person above you not to leave you for the person above them.";
        dare[20] = "Pretend to be a food. Dont pretend to eat the food, pretend to be the food. Keep pretending until someone in the group guesses the food you are.";
        dare[21] = "Add someone to the server.";
        dare[22] = "Send a true and embarrassing confession in confessions before today ends.";
        dare[23] = "Do one thing the person above you tells you to do.";

        return dare[num];
    }

    /**
     * Embed Generator
     * @return Truth embed
     */
    private static EmbedBuilder getTruth() {

        // Random truth
        String truth = truth();

        return new EmbedBuilder()
                .setTitle("Truth or Dare")
                .addField("Truth: ", truth, false)
                .setColor(Util.randColor());
    }

    /**
     * Embed Generator
     * @return Dare embed
     */
    private static EmbedBuilder getDare() {

        // Random dare
        String dare = dare();

        return new EmbedBuilder()
                .setTitle("Truth or Dare")
                .addField("Dare: ", dare, false)
                .setColor(Util.randColor());
    }
    
}
