package com.example.demo.aiClass;

import com.example.demo.deck.Card;
import com.example.demo.deck.CardValue;
import com.example.demo.deck.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class AiTest {



    @Test
    void TF4() {
        Ai sebZul = new Ai(50, "Sebastian Zulj");
    }

    @Test
    void TF5() {
        Ai sebZul = new Ai(50, "Sebastian Zulj");
        Card card1 = new Card(Suit.HEARTS, CardValue.TWO, null);
        Card card2 = new Card(Suit.SPADES, CardValue.FOUR, null);
        sebZul.setStartingHand(card1, card2);
        sebZul.makeDecision(100);
        String decision = sebZul.getDecision();
        assertEquals(decision, "fold");
    }

    @Test
    void TF6() {
        Ai sebZul = new Ai(50, "Sebastian Zulj");
        Card card1 = new Card(Suit.HEARTS, CardValue.ACE, null);
        Card card2 = new Card(Suit.SPADES, CardValue.SIX, null);
        sebZul.setStartingHand(card1, card2);
        sebZul.makeDecision(100);
        String decision = sebZul.getDecision();
        assertEquals(decision, "call");
    }

    @Test
    void TF7() {
        Ai sebZul = new Ai(50, "Sebastian Zulj");
        Card card1 = new Card(Suit.HEARTS, CardValue.SIX, null);
        Card card2 = new Card(Suit.SPADES, CardValue.SIX, null);
        sebZul.setStartingHand(card1, card2);
        sebZul.makeDecision(10);
        String decision = sebZul.getDecision();
        if (decision.contains(",")){
            String[] temp = new String[4];
            temp = decision.split(",");
            decision = temp[0];
        }
        assertEquals(decision, "raise");
    }

    @Test
    void TF8() {
        Ai sebZul = new Ai(50, "Sebastian Zulj");
        Card card1 = new Card(Suit.HEARTS, CardValue.TWO, null);
        Card card2 = new Card(Suit.SPADES, CardValue.FOUR, null);
        Card card3 = new Card(Suit.SPADES, CardValue.TWO, null);
        Card card4 = new Card(Suit.CLUBS, CardValue.TWO, null);
        Card card5 = new Card(Suit.DIAMONDS, CardValue.THREE, null);
    }


}