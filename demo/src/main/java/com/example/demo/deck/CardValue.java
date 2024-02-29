package com.example.demo.deck;

/**
 * Enums for the allowed card values
 * @author Vedrana Zeba
 */
public enum CardValue {
	TWO(2),
	THREE(3),
	FOUR(4),
	FIVE(5),
	SIX(6),
	SEVEN(7),
	EIGHT(8),
	NINE(9),
	TEN(10),
	JACK(11),
	QUEEN(12),
	KING(13),
	ACE(14);

	private int cardValue;


	/**
	 * Creates the card values
	 * @param value an int between 2-14
	 */
	private CardValue(int value) {
		this.cardValue = value;
	}


	/**
	 * Returns the card value
	 * @return the card value
	 */
	public int getCardValue() {
		return cardValue;
	}

	public static CardValue fromCardValueCode(int cardValueCode) {
		for (CardValue cardValue : CardValue.values()) {
			if (cardValue.getCardValue() == cardValueCode) {
				return cardValue;
			}
		}
		throw new IllegalArgumentException("No enum constant for cardValueCode: " + cardValueCode);
	}

	public static String toSwedish(int cardValueCode) {
		return switch (cardValueCode) {
			case 2, 3, 4, 5, 6, 7, 8, 9, 10 -> String.valueOf(cardValueCode);
			case 11 -> "Knekt";
			case 12 -> "Dam";
			case 13 -> "Kung";
			case 14 -> "Ess";
			default -> "Fel";
		};
    }
}
