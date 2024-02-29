package com.example.demo.deck;

/**
 * Enums for the allowed suits
 * @author Vedrana Zeba
 */
public enum Suit {
	HEARTS('h'),
	SPADES('s'),
	CLUBS('c'),
	DIAMONDS('d');

	private char suit;
	
	
	/**
	 * Creates the suits
	 * @param firstLetter h, s, c or d
	 */
	Suit(char firstLetter) {
		this.suit = firstLetter;
	}

	
	/**
	 * Returns the suit
	 * @return the suit
	 */
	public char getSuitLetter() {
		return suit;
	}

	/**
	 * Returns the suit
	 * @param suitCode
	 * @return
	 */
	public static Suit fromSuitCode(char suitCode) {
		for (Suit suit : Suit.values()) {
			if (suit.getSuitLetter() == suitCode) {
				return suit;
			}
		}
		throw new IllegalArgumentException("No enum constant for suitCode: " + suitCode);
	}

	/**
	 * Returns the suit in Swedish
	 * @param shuitCode
	 * @return
	 */
	public static String toSwedish(char shuitCode) {
		switch (shuitCode) {
		case 'h':
			return "Hjärter";
		case 's':
			return "Spader";
		case 'c':
			return "Klöver";
		case 'd':
			return "Ruter";
		default:
			return "Fel";
		}
	}
}