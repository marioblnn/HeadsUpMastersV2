package com.HeadsUpMastersV2.game;

import com.HeadsUpMastersV2.service.Utils;

public class Guest {
    String guestUUID;
    float balance;


    public Guest() {
        this.guestUUID = Utils.generateGuestID();
        this.balance = 1000.0f;
    }

    public String getGuestUUID() {
        return guestUUID;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
