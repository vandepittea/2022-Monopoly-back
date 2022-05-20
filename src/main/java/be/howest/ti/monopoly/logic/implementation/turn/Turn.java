package be.howest.ti.monopoly.logic.implementation.turn;

import be.howest.ti.monopoly.logic.implementation.Player;
import be.howest.ti.monopoly.logic.implementation.enums.TurnType;
import be.howest.ti.monopoly.logic.implementation.tile.Tile;

import java.util.ArrayList;
import java.util.List;

public class Turn {
    private final Player player;
    private final DiceRoll roll;
    private final List<Move> moves;

    private TurnType type;

    public Turn(Player player) {
        this.player = player;
        this.roll = new DiceRoll();
        this.moves = new ArrayList<>();
    }

    public Turn(Player player, int die1, int die2) {
        this.player = player;
        this.roll = new DiceRoll(die1, die2);
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

    public void addMove(Tile tile, String description) {
        moves.add(new Move(tile, description));
    }
    public void setType(TurnType type) {
        this.type = type;
    }
}
