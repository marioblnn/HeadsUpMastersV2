package com.example.demo.GameBuilderTest;

import java.util.List;

import org.junit.jupiter.api.Test;
import com.HeadsUpMastersV2.GameBuilder.Card;
import com.HeadsUpMastersV2.GameBuilder.GuestPlayer;

public class GuestPlayerTest {
    
    @Test
    public void testGuestPlayerCreation() {
        String guestID = "Guest1234";
        Double stack = 1000.0;
        Integer seat = 1;

        GuestPlayer guestPlayer = new GuestPlayer(guestID, stack, seat);

        assert guestPlayer.getGuestID().equals(guestID) : "Guest ID should match the provided value";
        assert guestPlayer.getStack().equals(stack) : "Stack should match the provided value";
        assert guestPlayer.getSeat().equals(seat) : "Seat should match the provided value";
        assert guestPlayer.getHand().isEmpty() : "Hand should be initialized as empty";
        assert guestPlayer.getBet().equals(0.0) : "Initial bet should be 0.0";
        Card card1 = new Card("A", "Hearts", 12, 41, 0x1);
        Card card2 = new Card("K", "Diamonds", 13, 42, 0x2);

        guestPlayer.setHand(List.of(card1, card2));

        System.out.println("Guest Player Details:");
        System.out.println("Guest ID: " + guestPlayer.getGuestID());
        System.out.println("Stack: " + guestPlayer.getStack());
        System.out.println("Seat: " + guestPlayer.getSeat());
        System.out.println("Hand: " + guestPlayer.getHand());
        System.out.println("Bet: " + guestPlayer.getBet());
    }

    

}
