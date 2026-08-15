package com.HeadsUpMastersV2.GameService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HandMapService {
    private static final int[] primes = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41 };
    private static final Map<Integer, Integer> cactusKevMap = new HashMap<>();
    private static boolean isInitialized = false;

    private static int HIGH_CARD = 1 << 24;
    private static int PAIR = 2 << 24;
    private static int TWO_PAIRS = 3 << 24;
    private static int THREE_OF_A_KIND = 4 << 24;
    private static int STRAIGHT = 5 << 24;
    private static int FLUSH = 6 << 24;
    private static int FULL_HOUSE = 7 << 24;
    private static int FOUR_OF_A_KIND = 8 << 24;
    private static int STRAIGHT_FLUSH = 9 << 24;


    
    public HandMapService(){
        if (isInitialized) return;
        generateHashTable();
        isInitialized = true;
    }

    public static void generateHashTable(){
        for(int a = 0; a<13; a++){
            for(int b = a; b<13; b++){
                for(int c = b; c<13; c++){
                    for(int d = c; d<13; d++){
                        for (int e = d; e<13; e++){
                            int product = primes[a] * primes[b] * primes[c] * primes[d] * primes[e];
                            int hand[] = {a, b, c, d, e};
                            cactusKevMap.put(product, identifyHand(hand));
                        }
                    }
                }
            }
        }
    }

    /**
     * Function to determine what is the score of a specific Hand
     * @param handIndexes the hand eg:{0,0,0,1 1} -> full house 2 2 2 3 3
     * @param suits the suits of the cards
     * @return the score of the hand
     */
    private static int identifyHand(int[] handIndexes){
        int[] counter = new int[13];
        for(int card : handIndexes) counter[card]++;

        int quadsRank = -1;
        int tripsRank = -1;
        int pairRank = -1;
        int secondPairRank = -1;
        
        
        
        for(int i=12; i>=0; i--){
            if(counter[i] == 4) {quadsRank = i;}
            else if(counter[i] == 3) {tripsRank = i;}
            else if(counter[i] == 2){
                if(pairRank == -1){
                    pairRank = i;
                } else {
                    secondPairRank = i;
                }
            }
        }

        int[] sorted = handIndexes.clone();
        Arrays.sort(sorted);
        boolean isStraight = true;
        for(int i=0; i<4; i++){
            if(sorted[i+1] != sorted[i] + 1) isStraight = false;
        }

        /**
         * A-5 Straight
         */
        if(!isStraight && sorted[4] == 12 && sorted[0] == 0 && sorted[1] == 1 && sorted[2] == 2 && sorted[3] == 3){
            isStraight = true;
            return STRAIGHT | (3 << 20); 
        }

         /**
          * Determine Quads Score
          */
        if(quadsRank != -1){
            int kicker = 0;
            for(int i=12; i>=0; i--){
                if(counter[i] == 1) {kicker = i;}
            }
            return FOUR_OF_A_KIND | (quadsRank << 20) | (kicker << 16); 
        }

        /**
         * Determine full house
         */
        if(tripsRank != -1 && pairRank != -1){
            return FULL_HOUSE | (tripsRank << 20) | (pairRank << 16);
        }

        /**
         * Determine Straights
         */
        if(isStraight){
            return STRAIGHT | (sorted[4] << 20) ;
        }


        /**
         * Determine trips
         */
        if(tripsRank != -1 ){
            int firstKicker = -1;
            int secondKicker = -1;
            for(int i = 12 ; i>=0; i--){
                if(counter[i] == 1){
                    if(firstKicker == -1){
                        firstKicker = i;
                    } else {
                        secondKicker = i;
                    }
                }
            }
            return THREE_OF_A_KIND | (tripsRank << 20) | (firstKicker << 16) | (secondKicker << 12);
        }

        /**
         * Determine Two Pairs
         */
        if(pairRank != -1 && secondPairRank != -1){
            int kicker = 0;
            for(int i = 12; i>=0; i--){
                if(counter[i] == 1){
                    kicker = i;
                }
            }
            return TWO_PAIRS | (pairRank << 20) | (secondPairRank << 16) | (kicker << 12);
        }

        /**
         * Determine a Pair
         */
        if(pairRank != -1){
            int firstKicker =-1;
            int secondKicker = -1;
            int thirdKicker = -1;

            for(int i = 12 ; i>=0; i-- ){
                if(counter[i] == 1){
                    if(firstKicker == -1){
                        firstKicker = i;
                    }
                    else if(secondKicker == -1){
                        secondKicker = i;
                    } else{
                        thirdKicker = i;
                    }
                }
            }

            /**
             * If all fails -> high card
             */
            return PAIR | (pairRank << 20) | (firstKicker << 16) | (secondKicker << 12) | (thirdKicker << 8);
        }

        else{
            return HIGH_CARD | (sorted[4] << 20) | (sorted[3] << 16) | (sorted[2] << 12) | (sorted[1] << 8) | (sorted[0] << 4) ;
        }
    } 


    public static int getHandScore(int[] handIndexes, int[] suits){
        int product = 1;
        for(int cardIndex : handIndexes){
            product *= primes[cardIndex];
        }

        int score = cactusKevMap.get(product);

        boolean isFlush = (suits[0] & suits[1] & suits[2] & suits[3] & suits[4]) != 0;
        if(isFlush){
        if((score & 0xFF000000) == STRAIGHT){
            return (score ^ STRAIGHT ) | STRAIGHT_FLUSH;
        }

        return (score ^ HIGH_CARD) | FLUSH;
        }

        return score;

    }


    /**
     * Evaluates the score of an hand for No Limit Hold'em poker
     * @param handIndexes the indexes of the cards, card 2 has index 0, card A has index 14
     * @param suits the suits represented by Hex numbers of the cards in the exact same order 
     * @return
     */
    public static int sevenCardEvaluator(int[] handIndexes, int[] suits){
        int maxScore = 0;
        for(int i = 0; i<handIndexes.length; i++){
            for(int j = i+1; j<handIndexes.length; j++){
                int[] fiveCardsHand = new int[5];
                int[] fiveCardsSuits = new int[5];
                int index = 0;
                for(int k = 0; k<handIndexes.length; k++){
                    if(k != i && k !=j){
                        fiveCardsHand[index] = handIndexes[k];
                        fiveCardsSuits[index] = suits[k];
                        index++;
                    }
                }
                int currentScore = getHandScore(fiveCardsHand, fiveCardsSuits);
                if(currentScore > maxScore) maxScore = currentScore;
            }
        }
        return maxScore;
    }

}
