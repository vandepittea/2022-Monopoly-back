package be.howest.ti.monopoly.logic.implementation.turn;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.security.SecureRandom;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "roll")
public class DiceRoll {
    private static final SecureRandom random = new SecureRandom();

    private final int die1;
    private final int die2;

    private final int[] roll = new int[2];

    public DiceRoll() {
        die1 = random.nextInt(6) + 1;
        die2 = random.nextInt(6) + 1;
        roll[0] = die1;
        roll[1] = die2;
    }

    public DiceRoll(int die1, int die2) {
        this.die1 = die1;
        this.die2 = die2;
    }

    public Integer[] getRoll() {
        return new Integer[]{die1, die2};
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
