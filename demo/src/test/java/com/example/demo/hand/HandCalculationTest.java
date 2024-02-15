package com.example.demo.hand;

import com.example.demo.deck.Card;
import com.example.demo.deck.CardValue;
import com.example.demo.deck.Suit;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HandCalculationTest {
    @Test
    void checkPairAndMore() {

    }

    @Test
    void checkHighCardsTrue() {
        ArrayList<String> playingCards = new ArrayList<>();
        playingCards.add("14,h");
        playingCards.add("11,d");
        HandCalculation handCalculator = new HandCalculation(playingCards);
        boolean result = handCalculator.checkHighCards();
        assertEquals(true, result);
    }

    @Test
    void checkHighCardsFalse() {
        ArrayList<String> playingCards = new ArrayList<>();
        playingCards.add("2,h");
        playingCards.add("9,d");
        HandCalculation handCalculator = new HandCalculation(playingCards);
        boolean result = handCalculator.checkHighCards();
        assertEquals(false, result);
    }

    @Test
    void checkSuit() {
    }

    public static void main(String[] args) {
        ArrayList<String> playingCards = new ArrayList<>();
        playingCards.add("2,h");
        playingCards.add("9,d");
        HandCalculation handCalculator = new HandCalculation(playingCards);
        handCalculator.checkSuit();
    }


    @Test
    void checkStraight() {
        //any 5 cards in a row lead to straight
        ArrayList<String> playingCards = new ArrayList<>();
        playingCards.add("5,h");
        playingCards.add("4,d");
        playingCards.add("6,h");
        playingCards.add("7,d");
        playingCards.add("8,h");
        playingCards.add("14,d");
        playingCards.add("9,h");
        HandCalculation handCalculator = new HandCalculation(playingCards);
        int straight = handCalculator.checkStraight();
        assertEquals(5, straight);
    }

    @Test
    void help() {

    }
}