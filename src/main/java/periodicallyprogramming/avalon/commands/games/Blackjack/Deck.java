package periodicallyprogramming.avalon.commands.games.Blackjack;

import periodicallyprogramming.avalon.Util;

import java.util.HashMap;

public class Deck {

    static HashMap<String, Integer> deck = new HashMap<>();
    static HashMap<Integer, String> reverse = new HashMap<>();

    // Holds cards and their values
    static void deckValues() {
        deck.put("Ace", 11);
        deck.put("King", 10);
        deck.put("Queen", 10);
        deck.put("Jack", 10);
        deck.put("2", 2);
        deck.put("3", 3);
        deck.put("4", 4);
        deck.put("5", 5);
        deck.put("6", 6);
        deck.put("7", 7);
        deck.put("8", 8);
        deck.put("9", 9);
        deck.put("10", 10);
    }

    static void reverseValues() {
        reverse.put(2, "2");
        reverse.put(3, "3");
        reverse.put(4, "4");
        reverse.put(5, "5");
        reverse.put(6, "6");
        reverse.put(7, "7");
        reverse.put(8, "8");
        reverse.put(9, "9");
        reverse.put(10, "Random");
        reverse.put(11, "Ace");
    }

    static String randomCard() {
        deckValues();
        reverseValues();
        int num = Util.randomWithRange(2, 11);

        String card = reverse.get(num);

        // if "random", get card
        if (card.equalsIgnoreCase("random")) {
            int r = Util.randomWithRange(0, 3);

            switch (r) {
                case 0 -> card = "King";
                case 1 -> card = "Jack";
                case 2 -> card = "Queen";
                case 3 -> card = "10";
            }
        }

        return card;
    }
}
