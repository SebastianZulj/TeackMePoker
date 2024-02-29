package com.example.demo.deck;

public class CardValueTest {

    public static void main(String[] args) {
        Card card = new Card(Suit.HEARTS, CardValue.ACE, null);
        System.out.println(card.toSwedishString());

        card = new Card(Suit.SPADES, CardValue.KING, null);
        System.out.println(card.toSwedishString());

        card = new Card(Suit.CLUBS, CardValue.QUEEN, null);
        System.out.println(card.toSwedishString());

        card = new Card(Suit.DIAMONDS, CardValue.JACK, null);
        System.out.println(card.toSwedishString());

        card = new Card(Suit.HEARTS, CardValue.TEN, null);
        System.out.println(card.toSwedishString());

        card = new Card(Suit.SPADES, CardValue.NINE, null);
        System.out.println(card.toSwedishString());

    }
}
