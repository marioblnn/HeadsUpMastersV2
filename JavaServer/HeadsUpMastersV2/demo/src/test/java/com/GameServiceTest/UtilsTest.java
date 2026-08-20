package com.GameServiceTest;

import org.junit.jupiter.api.Test;

public class UtilsTest {
    
    @Test
    public void testGenerateGuestID() {
        String guestID = com.HeadsUpMastersV2.service.Utils.generateGuestID();
        assert guestID.startsWith("Guest") : "Guest ID should start with 'Guest'";
        assert guestID.length() == 10 : "Guest ID should be 10 characters long";
        assert guestID.substring(6).matches("\\d{4}") : "Guest ID should have 4 digits after 'GUEST'";
        System.out.println("Generated Guest ID: " + guestID);
    }


}
