package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.ServiceAdapter;

import java.util.HashSet;
import java.util.Set;


public class MonopolyService extends ServiceAdapter {
    private Set<Game> games = new HashSet<>();

    @Override
    public String getVersion() {
        return "0.0.1";
    }

    @Override
    public Game createGame() {
        Game newGame = new Game();
        games.add(newGame);
        return newGame;
    }
}
