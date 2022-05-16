package be.howest.ti.monopoly.logic.implementation.turn;

import be.howest.ti.monopoly.logic.implementation.Player;
import be.howest.ti.monopoly.logic.implementation.enums.TurnType;

import java.util.ArrayList;
import java.util.List;

public class Turn {
    private Player player;
    private DiceRoll roll;
    private TurnType type;
    private List<Move> moves;

    public Turn(Player player) {
        this.player = player;
        this.roll = new DiceRoll();
        this.moves = new ArrayList<>();
    }

    public String getPlayer() {
        return player.getName();
    }

    public DiceRoll getRoll() {
        return roll;
    }

    public TurnType getType() {
        return type;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public void addMove(String title, String description) {
        moves.add(new Move(title, description));
    }
    public void setType(TurnType type) {
        this.type = type;
    }
}
