package com.HeadsUpMastersV2.GameService;

public class Utils {
    
    public static String generateGuestID(){
        StringBuilder id = new StringBuilder();
        String chars = "0123456789";
        for (int i = 0; i < 4 ; i++){
            id.append(chars.charAt((int)(Math.random() * chars.length())));
        }
        return "Guest-" + id.toString();
    }


}
