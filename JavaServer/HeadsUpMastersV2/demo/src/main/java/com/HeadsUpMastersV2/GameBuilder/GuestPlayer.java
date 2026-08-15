package com.HeadsUpMastersV2.GameBuilder;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class GuestPlayer {
    private String guestID;
    private Double stack;
    private List<Card> hand;
    private Double bet;
    private Integer seat;
    private int handScore;

    public GuestPlayer(String guestID, Double stack, Integer seat) {
        this.guestID = guestID;
        this.stack = stack;
        this.hand = new ArrayList<>();
        this.bet = 0.0;
        this.seat = seat;
        this.handScore = 0;
    }


    public void clearHand() {
        this.hand.clear();
    }

    public void bet(Double amount) {
        if (amount <= this.stack) {
            this.bet += amount;
            this.stack -= amount;
        } else {
            throw new IllegalArgumentException("Bet amount exceeds available stack.");
        }
    }


    /**
     * Void funtion to give a card to a player.<br>
     * <br>
     * Maily used in {@link Game}<br>
     * <br>
     * @param card The param will be an {@link Card} object
     */
    public void giveCard(Card card){
        int cardCount = this.hand.size();
        if(cardCount < 2){
            this.hand.add(cardCount, card);
        }
        else{
            System.out.println("Player has 2 cards already");
        }
    }



}
