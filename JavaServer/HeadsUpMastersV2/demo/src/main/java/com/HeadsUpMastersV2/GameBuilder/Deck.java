package com.HeadsUpMastersV2.GameBuilder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class Deck {
    /**
    * Builds a standard 52-card poker deck and shuffles it securely.<br>
    *<br>
    * Uses a {@link SecureRandom} seed to ensure a cryptographically secure shuffle.
    *
    * @return a shuffled list of 52 playing cards
    */
    public static List<Card> createDeck() {
        SecureRandom random = new SecureRandom();
        String[] suits = { "spade", "heart", "club", "diamond" };
        String[] symbols = {
            "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "J", "Q", "K", "A"
        };
        int[] primes = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41 };
        int[] strength = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        int[] hexSuits = {0x8000, 0x4000, 0x2000, 0x1000};
        List<Card> cards = new ArrayList<>();

        for (int j = 0; j<suits.length; j++) {
            for (int i = 0; i < symbols.length; i++) {
                cards.add(new Card(symbols[i], suits[j], strength[i] ,primes[i], hexSuits[j]));
            }
        }

        Collections.shuffle(cards, random);
        return cards;
    }
}
