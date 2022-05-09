package be.howest.ti.monopoly.logic.implementation.turn;

import be.howest.ti.monopoly.logic.implementation.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Turn {
    private Player player;
    private Integer[] roll;
    private TurnType type;
    private List<Move> moves;

    public Turn(Player player) {
        this.player = player;
        this.roll = new Integer[2];
        this.moves = new ArrayList<>();
    }

    public String getPlayer() {
        return player.getName();
    }

    public Integer[] getRoll() {
        return roll;
    }

    public TurnType getType() {
        return type;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public Integer[] generateRoll() {
        Random r = new Random();
        roll[0] = r.nextInt(6) + 1;
        roll[1] = r.nextInt(6) + 1;
        return roll;
    }

    public void addMove(String title, String description) {
        moves.add(new Move(title, description));
    }
    public void setType(TurnType type) {
        this.type = type;
    }
}
