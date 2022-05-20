package be.howest.ti.monopoly.logic.implementation.turn;

import com.fasterxml.jackson.annotation.JsonValue;

import java.security.SecureRandom;

public class DiceRoll {
    private static final SecureRandom random = new SecureRandom();

    private final int die1;
    private final int die2;

    public DiceRoll() {
        die1 = random.nextInt(6) + 1;
        die2 = random.nextInt(6) + 1;
    }

    public DiceRoll(int die1, int die2) {
        this.die1 = die1;
        this.die2 = die2;
    }

    @JsonValue
    public int[] getRoll() {
        return new int[]{die1, die2};
    }

    public int getDie1() {
        return die1;
    }

    public int getDie2() {
        return die2;
    }

    public int getValue() {
        return die1 + die2;
    }

    public boolean isDoubleRoll() {
        return die1 == die2;
    }
}
