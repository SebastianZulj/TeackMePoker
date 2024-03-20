package com.example.demo.hand;

import com.example.demo.deck.Card;
import com.example.demo.deck.CardValue;
import com.example.demo.deck.Suit;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains all test cases related to the HandCalculation class.
 * That encompasses TF15-24 as well as some extra checking high cards and straight.
 * @author Tiffany Dizdar, HT24
 */
class   HandCalculationTest {

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
    void checkStraight() {
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
    void TF15() {
        ArrayList<Card> cardsList = new ArrayList<>();
        Card card1 = new Card(Suit.HEARTS, CardValue.TWO, null);
        Card card2 = new Card(Suit.SPADES, CardValue.FOUR, null);
        cardsList.add(card1);
        cardsList.add(card2);
        Hand hand = new Hand(cardsList);
        String info = hand.showInfo();
        assertEquals("Helper: Ingenting, tyvärr..." +
                "\nAdvice: Denna hand kanske inte är den bästa att spela på..." +
                "\npwrBar: 1" +
                "\ntoHighlight: []", info);
    }

    @Test
    void TF16() {
        ArrayList<Card> cardsList = new ArrayList<>();
        Card card1 = new Card(Suit.HEARTS, CardValue.ACE, null);
        Card card2 = new Card(Suit.SPADES, CardValue.SIX, null);
        cardsList.add(card1);
        cardsList.add(card2);
        Hand hand = new Hand(cardsList);
        String info = hand.showInfo();
        assertEquals("Helper: Ingenting, tyvärr..." +
                "\nAdvice: Du har ett högt 'HIGH CARD'. \nOm det är billigt så kan du prova och se.\n" +
                "\npwrBar: 2" +
                "\ntoHighlight: []", info);
    }

    @Test
    void TF17() {
        ArrayList<Card> cardsList = new ArrayList<>();
        Card card1 = new Card(Suit.HEARTS, CardValue.SIX, null);
        Card card2 = new Card(Suit.SPADES, CardValue.SIX, null);
        cardsList.add(card1);
        cardsList.add(card2);
        Hand hand = new Hand(cardsList);
        String info = hand.showInfo();
        assertEquals("Helper: 'ONE-PAIR' i 6:or\n" +
                "\nAdvice: 'ONE-PAIR' på första-handen är en stark hand!\nSå kör på!\n" +
                "\npwrBar: 4\n" +
                "toHighlight: [6,H, 6,S]", info);
    }

    @Test
    void TF18() {
        ArrayList<Card> cardsList = new ArrayList<>();
        Card card1 = new Card(Suit.HEARTS, CardValue.TWO, null);
        Card card2 = new Card(Suit.SPADES, CardValue.FOUR, null);
        Card card3 = new Card(Suit.SPADES, CardValue.TWO, null);
        Card card4 = new Card(Suit.CLUBS, CardValue.FOUR, null);
        Card card5 = new Card(Suit.DIAMONDS, CardValue.THREE, null);
        cardsList.add(card1);
        cardsList.add(card2);
        cardsList.add(card3);
        cardsList.add(card4);
        cardsList.add(card5);
        Hand hand = new Hand(cardsList);
        String info = hand.showInfo();
        assertEquals("Helper: 'TWO PAIRS' i 2:or och 4:or\n" +
                "Advice: 'TWO PAIRS' är en bra hand, kör på.\n" +
                "\npwrBar: 4\n" +
                "toHighlight: [2,H, 4,S, 2,S, 4,C]", info);
    }

    @Test
    void TF19() {
        ArrayList<Card> cardsList = new ArrayList<>();
        Card card1 = new Card(Suit.HEARTS, CardValue.TWO, null);
        Card card2 = new Card(Suit.SPADES, CardValue.TWO, null);
        Card card3 = new Card(Suit.DIAMONDS, CardValue.TWO, null);
        Card card4 = new Card(Suit.CLUBS, CardValue.FOUR, null);
        Card card5 = new Card(Suit.DIAMONDS, CardValue.THREE, null);
        cardsList.add(card1);
        cardsList.add(card2);
        cardsList.add(card3);
        cardsList.add(card4);
        cardsList.add(card5);
        Hand hand = new Hand(cardsList);
        String info = hand.showInfo();
        assertEquals("Helper: 'THREE OF A KIND' i 2:or\n" +
                "Advice: 'THREE OF A KIND' är en väldigt stark hand. Kör på! Fundera även på att höja!\n" +
                "\npwrBar: 4\n" +
                "toHighlight: [2,H, 2,S, 2,D]", info);
    }


    @Test
    void TF20() {
        ArrayList<Card> cardsList = new ArrayList<>();
        Card card1 = new Card(Suit.HEARTS, CardValue.TWO, null);
        Card card2 = new Card(Suit.HEARTS, CardValue.THREE, null);
        Card card3 = new Card(Suit.HEARTS, CardValue.FOUR, null);
        Card card4 = new Card(Suit.HEARTS, CardValue.FIVE, null);
        Card card5 = new Card(Suit.SPADES, CardValue.SIX, null);
        cardsList.add(card1);
        cardsList.add(card2);
        cardsList.add(card3);
        cardsList.add(card4);
        cardsList.add(card5);
        Hand hand = new Hand(cardsList);
        String info = hand.showInfo();
        assertEquals("Helper: En 'STRAIGHT'!! Du har 5/5.\n" +
                "\nAdvice: En 'STRAIGHT' är en riktigt bra hand. Kör på! \nFundera även på att höja!\nDu har en chans för en 'FLUSH' i hjärter, du har 4/5.\n" +
                "\npwrBar: 4\n" +
                "toHighlight: [2,H, 3,H, 4,H, 5,H, 6,S]", info);
    }

    @Test
    void TF21() {
        ArrayList<Card> cardsList = new ArrayList<>();
        Card card1 = new Card(Suit.HEARTS, CardValue.TWO, null);
        Card card2 = new Card(Suit.HEARTS, CardValue.FOUR, null);
        Card card3 = new Card(Suit.HEARTS, CardValue.SIX, null);
        Card card4 = new Card(Suit.HEARTS, CardValue.EIGHT, null);
        Card card5 = new Card(Suit.HEARTS, CardValue.TEN, null);
        cardsList.add(card1);
        cardsList.add(card2);
        cardsList.add(card3);
        cardsList.add(card4);
        cardsList.add(card5);
        Hand hand = new Hand(cardsList);
        String info = hand.showInfo();
        assertEquals("Helper: En 'FLUSH' i hjärter!! Du har 5/5!!\n" +
                "\nAdvice: Du har en 'FLUSH'! Kör på, din hand är svår att slå!\n" +
                "\npwrBar: 4\n" +
                "toHighlight: [2,H, 4,H, 6,H, 8,H, 10,H]", info);
    }

    @Test
    void TF22() {
        ArrayList<Card> cardsList = new ArrayList<>();
        Card card1 = new Card(Suit.HEARTS, CardValue.TWO, null);
        Card card2 = new Card(Suit.SPADES, CardValue.FOUR, null);
        Card card3 = new Card(Suit.CLUBS, CardValue.TWO, null);
        Card card4 = new Card(Suit.HEARTS, CardValue.FOUR, null);
        Card card5 = new Card(Suit.SPADES, CardValue.TWO, null);
        cardsList.add(card1);
        cardsList.add(card2);
        cardsList.add(card3);
        cardsList.add(card4);
        cardsList.add(card5);
        Hand hand = new Hand(cardsList);
        String info = hand.showInfo();
        assertEquals("Helper: 'FULL HOUSE' med 2:or och 4:or!!\n" +
                "Advice: Det är inte mycket som slår denna hand! Höja är rekomenderat!\n" +
                "\npwrBar: 4\n" +
                "toHighlight: [2,H, 4,S, 2,C, 4,H, 2,S]", info);
    }

    @Test
    void TF23() {
        ArrayList<Card> cardsList = new ArrayList<>();
        Card card1 = new Card(Suit.HEARTS, CardValue.TWO, null);
        Card card2 = new Card(Suit.SPADES, CardValue.TWO, null);
        Card card3 = new Card(Suit.CLUBS, CardValue.TWO, null);
        Card card4 = new Card(Suit.DIAMONDS, CardValue.TWO, null);
        Card card5 = new Card(Suit.HEARTS, CardValue.FOUR, null);
        cardsList.add(card1);
        cardsList.add(card2);
        cardsList.add(card3);
        cardsList.add(card4);
        cardsList.add(card5);
        Hand hand = new Hand(cardsList);
        String info = hand.showInfo();
        assertEquals("Helper: 'FOUR OF A KIND' i 2:or\n" +
                "Advice: 'FOUR OF A KIND' är en av de bästa händerna. Kör på! Fundera även på att höja!\n" +
                "\npwrBar: 4\n" +
                "toHighlight: [2,H, 2,S, 2,C, 2,D]", info);
    }

    @Test
    void TF24() {
        ArrayList<Card> cardsList = new ArrayList<>();
        Card card1 = new Card(Suit.HEARTS, CardValue.TWO, null);
        Card card2 = new Card(Suit.HEARTS, CardValue.THREE, null);
        Card card3 = new Card(Suit.HEARTS, CardValue.FOUR, null);
        Card card4 = new Card(Suit.HEARTS, CardValue.FIVE, null);
        Card card5 = new Card(Suit.HEARTS, CardValue.SIX, null);
        cardsList.add(card1);
        cardsList.add(card2);
        cardsList.add(card3);
        cardsList.add(card4);
        cardsList.add(card5);
        Hand hand = new Hand(cardsList);
        String info = hand.showInfo();
        assertEquals("Helper: En 'STRAIGHT FLUSH' i färgen hjärter!\n" +
                "Advice: 'STRAIGHT FLUSH' är bästa handen i spelet. Kör på och höj!\n" +
                "\npwrBar: 4\n" +
                "toHighlight: [2,H, 3,H, 4,H, 5,H, 6,H]", info);
    }


}