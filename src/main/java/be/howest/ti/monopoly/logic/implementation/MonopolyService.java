package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.ServiceAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MonopolyService extends ServiceAdapter {
    private Set<Game> games = new HashSet<>();

    @Override
    public List<Game> getGames(boolean started, boolean startedMatters, int numberOfPlayers, boolean playersMatter, String prefix) {
        List<Game> gamesList = new ArrayList<>();

        games.forEach(game -> {
            if (!startedMatters || (started == game.isStarted())) {
                if (!playersMatter || (numberOfPlayers == game.getNumberOfPlayers())) {
                    String gamePrefix = game.getId().split("_")[0];
                    if (gamePrefix.equals(prefix)) {
                        gamesList.add(game);
                    }
                }
            }
        });

        return gamesList;
    }

    @Override
    public String getVersion() {
        return "0.0.1";
    }

    @Override
    public Game createGame(int numberOfPlayers, String prefix) {
        Game newGame = new Game(numberOfPlayers, prefix);
        games.add(newGame);
        return newGame;
    }
}
