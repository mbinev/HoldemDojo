package com.nedogeek.holdem.combinations;

import com.nedogeek.holdem.gamingStuff.Card;
import com.nedogeek.holdem.gamingStuff.CardValue;

import java.util.Arrays;

/**
 * User: Konstantin Demishev
 * Date: 27.11.12
 * Time: 14:23
 */
public class PlayerCardCombination implements Comparable<PlayerCardCombination> {
    private Card[] cards;

    public PlayerCardCombination(Card... cards) {
        this.cards = cards;
        downSort(cards);
    }

    private void downSort(Card[] cards) {
        for (int n = 0; n < cards.length; n++) {
            for (int i = 0; i < cards.length - n - 1; i++) {
                if (cards[i].compareTo(cards[i + 1]) < 0) {
                    Card tempCard = cards[i];
                    cards[i] = cards[i + 1];
                    cards[i + 1] = tempCard;
                }
            }
        }
    }

    @Override
    public int compareTo(PlayerCardCombination o) {
        return 0;
    }

    public String getCombination() {
        switch (cards.length) {
            case 2:
                return twoCardsCombination();
            case 5:
                return fiveCardsCombination();
            default:
                return null;
        }
    }

    private String fiveCardsCombination() {
        if (Combination.hasCombination(cards, Combination.ROYAL_FLASH)) {
            return Combination.ROYAL_FLASH.generateMessage();
        }

        if (Combination.hasCombination(cards, Combination.STRAIGHT_FLASH)) {
            return Combination.STRAIGHT_FLASH.generateMessage(cards[0]);
        }

        if (Combination.hasCombination(cards, Combination.FOUR_OF_KIND)) {
            return Combination.FOUR_OF_KIND.generateMessage(cards[2]);
        }

        if (Combination.hasCombination(cards, Combination.STRAIGHT)) {
            return Combination.STRAIGHT.generateMessage(cards[0]);
        }

        if (Combination.hasCombination(cards, Combination.FULL_HOUSE)) {
            return (sameCardValues(1, 2)) ? Combination.FULL_HOUSE.generateMessage(cards[0], cards[4]) :
                    Combination.FULL_HOUSE.generateMessage(cards[4], cards[0]);
        }

        if (Combination.hasCombination(cards, Combination.SET)) {
            return Combination.SET.generateMessage(getSetCards());
        }

        if (Combination.hasCombination(cards, Combination.FLASH)) {
            return Combination.FLASH.generateMessage(cards);
        }

        if (Combination.hasCombination(cards, Combination.TWO_PAIRS)) {
            return Combination.TWO_PAIRS.generateMessage(getTwoPairsCards());
        }

        if (Combination.hasCombination(cards, Combination.PAIR)) {
            return Combination.PAIR.generateMessage(getPairsCards());
        }

        return Combination.HIGH_CARD.generateMessage(cards);
    }

    private Card[] getPairsCards() {
        Card[] pairCards = new Card[4];
        for (int i = 0; i < cards.length - 1; i++) {
            if (sameCardValues(i, i + 1)) {
                pairCards[0] = cards[i];
                if (i == 0) {
                    System.arraycopy(cards, 2, pairCards, 1, 3);
                    return pairCards;
                }
                if (i == 1) {
                    pairCards[1] = cards[0];
                    System.arraycopy(cards, 3, pairCards, 2, 2);
                    return pairCards;
                }
                if (i == 3) {
                    System.arraycopy(cards, 0, pairCards, 1, 3);
                    return pairCards;
                }
                System.arraycopy(cards, 0, pairCards, 1, 2);
                pairCards[3] = cards[4];
                return pairCards;
            }
        }
        return new Card[0];
    }



    private Card[] getTwoPairsCards() {
        Card[] setCards = new Card[3];
        if (!sameCardValues(0,1)) {
            setCards[0] = cards[2];
            setCards[1] = cards[4];
            setCards[2] = cards[0];
        } else if (!sameCardValues(3,4)) {
            setCards[0] = cards[0];
            setCards[1] = cards[2];
            setCards[2] = cards[4];
        } else {
            setCards[0] = cards[0];
            setCards[1] = cards[4];
            setCards[2] = cards[2];
        }
        return setCards;
    }

    private Card[] getSetCards() {
        Card[] setCards = new Card[3];
        setCards[0] = cards[2];

        if (sameCardValues(0, 1, 2)) {
            setCards[1] = cards[3];
            setCards[2] = cards[4];
        }

        if (sameCardValues(1, 2, 3)) {
            setCards[1] = cards[0];
            setCards[2] = cards[4];
        }

        if (sameCardValues(2, 3, 4)) {
            setCards[1] = cards[0];
            setCards[2] = cards[1];
        }

        return setCards;
    }

    private boolean sameCardValues(int... cardNumbers) {
        CardValue valueNeeded = cards[cardNumbers[0]].getCardValue();
        for (int cardNumber : cardNumbers) {
            if (cards[cardNumber].getCardValue() != valueNeeded) {
                return false;
            }
        }
        return true;
    }


    private String twoCardsCombination() {
        if (sameCardValues(0,1)) {
            return Combination.PAIR_TWO_CARDS.generateMessage(cards[0]);
        }

        return Combination.HIGH_CARD_TWO_CARDS.generateMessage(cards);
    }

    @Override
    public String toString() {
        return getCombination() + ": " + Arrays.asList(cards);
    }
}