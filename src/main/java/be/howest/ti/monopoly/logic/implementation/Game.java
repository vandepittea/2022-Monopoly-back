package be.howest.ti.monopoly.logic.implementation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Game<set> {
    private static final Map<String, Integer> idCounter = new HashMap<>();

    private final int numberOfPlayers;
    private final String id;

    private boolean started;
    private Set<Player> players;

    public Game(int numberOfPlayers, String prefix)
    {
        this.numberOfPlayers = numberOfPlayers;
        if (idCounter.containsKey(prefix))
        {
            this.id = prefix + "_" + idCounter.get(prefix);
            idCounter.put(prefix, idCounter.get(prefix) + 1);
        }
        else {
            this.id = prefix + "_0";
            idCounter.put(prefix, 1);
        }
        this.started = false;
    }

    public int getNumberOfPlayers()
    {
        return this.numberOfPlayers;
    }

    public String getId() {
        return this.id;
    }

    public boolean isStarted() {
        return this.started;
    }

    public void addPlayer(Player p){
        players.add(p);
    }
}
