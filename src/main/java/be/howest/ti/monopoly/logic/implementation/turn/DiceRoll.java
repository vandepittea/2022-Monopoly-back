package be.howest.ti.monopoly.logic.implementation.turn;

import java.security.SecureRandom;
import java.util.Objects;

public class DiceRoll {
    private static SecureRandom random = new SecureRandom();

    private int die1;
    private int die2;

    public DiceRoll() {
        die1 = random.nextInt(6) + 1;
        die2 = random.nextInt(6) + 1;
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

    public boolean isDoubleRoll() {
        return die1 == die2;
    }
}
