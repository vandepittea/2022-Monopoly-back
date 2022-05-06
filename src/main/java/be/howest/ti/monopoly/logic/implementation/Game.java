package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.exceptions.IllegalMonopolyActionException;
import be.howest.ti.monopoly.logic.exceptions.MonopolyResourceNotFoundException;

import java.util.*;

public class Game {
    private static final Map<String, Integer> idCounter = new HashMap<>();

    private final int numberOfPlayers;
    private final String id;
    private boolean started;
    private Set<Player> players;
    private String directSale;
    private int availableHouses;
    private int availableHotels;
    private List<String> turns;
    private Integer[] lastDiceRoll;
    private boolean canRoll;
    private boolean ended;
    private String currentPlayer;
    private String winner;

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
        this.players = new HashSet<>();
        this.directSale = null;
        this.availableHouses = 32;
        this.availableHotels = 12;
        this.turns = new ArrayList<>();
        this.lastDiceRoll = new Integer[2];
        this.canRoll = true;
        this.ended = false;
        this.currentPlayer = null;
        this.winner = null;
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
    public Set<Player> getPlayers() {
        return players;
    }
    public String getDirectSale() {
        return directSale;
    }
    public int getAvailableHouses() {
        return availableHouses;
    }
    public int getAvailableHotels() {
        return availableHotels;
    }
    public List<String> getTurns() {
        return turns;
    }
    public Integer[] getLastDiceRoll() {
        return lastDiceRoll;
    }
    public boolean isCanRoll() {
        return canRoll;
    }
    public boolean isEnded() {
        return ended;
    }
    public String getCurrentPlayer() {
        return currentPlayer;
    }
    public String getWinner() {
        return winner;
    }
    public void setDirectSale(String directSale) {
        this.directSale = directSale;
    }
    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void joinGame(String playerName){
        if(isExistedUser(playerName) || isStartedGame()){
            throw new IllegalMonopolyActionException("You tried to do something which is against the " +
                    "rules of Monopoly. In this case, it is most likely that you tried to join a game which has " +
                    "already started, or you used a name that is already taken in this game.");
        }
        else{
            Player p = new Player(playerName);
            addPlayer(p);
            changeStartedIfNeeded();
        }
    }

    private void addPlayer(Player p){
        players.add(p);
    }

    private void changeStartedIfNeeded(){
        if(checkForReachingOfMaximumPlayers()){
            started = true;
        }
    }

    private boolean checkForReachingOfMaximumPlayers(){
        return players.size() >= numberOfPlayers;
    }

    private boolean isExistedUser(String playerName){
        for(Player p: players){
            if(p.getName().equals(playerName)){
                return true;
            }
        }
        return false;
    }

    private boolean isStartedGame(){
        return this.isStarted();
    }

    public Player searchPlayerBasedOnName(String playerName){
        for(Player p: players){
            if(p.getName().equals(playerName)){
                return p;
            }
        }
        throw new MonopolyResourceNotFoundException("The player you are looking for do not exist. " +
                "Double check the name.");
    }
}
