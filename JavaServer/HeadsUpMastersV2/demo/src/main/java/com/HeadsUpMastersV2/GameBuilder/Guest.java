package com.HeadsUpMastersV2.GameBuilder;

import com.HeadsUpMastersV2.GameService.Utils;

public class Guest {
    String guestID;
    float balance;


    public Guest() {
        this.guestID = Utils.generateGuestID();
        this.balance = 1000.0f;
    }

    public String getGuestID() {
        return guestID;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
