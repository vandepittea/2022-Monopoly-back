package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.exceptions.IllegalMonopolyActionException;
import be.howest.ti.monopoly.logic.exceptions.MonopolyResourceNotFoundException;
import be.howest.ti.monopoly.logic.implementation.tile.Tile;
import be.howest.ti.monopoly.logic.implementation.turn.Turn;

import java.util.*;

public class Game {
    private static final Map<String, Integer> idCounter = new HashMap<>();

    private final int numberOfPlayers;
    private final String id;
    private boolean started;
    private List<Player> players;
    private String directSale;
    private int availableHouses;
    private int availableHotels;
    private List<Turn> turns;
    private Integer[] lastDiceRoll;
    private boolean canRoll;
    private boolean ended;
    private Player currentPlayer;
    private String winner;
    private Tile startingTile;

    private MonopolyService service;

    public Game(MonopolyService service, int numberOfPlayers, String prefix, Tile startingTile) {
        this.numberOfPlayers = numberOfPlayers;
        if (idCounter.containsKey(prefix)) {
            this.id = prefix + "_" + idCounter.get(prefix);
            idCounter.put(prefix, idCounter.get(prefix) + 1);
        } else {
            this.id = prefix + "_0";
            idCounter.put(prefix, 1);
        }
        this.started = false;
        this.players = new ArrayList<>();
        this.directSale = null;
        this.availableHouses = 32;
        this.availableHotels = 12;
        this.turns = new ArrayList<>();
        this.lastDiceRoll = new Integer[2];
        this.canRoll = true;
        this.ended = false;
        this.currentPlayer = null;
        this.winner = null;
        this.startingTile = startingTile;
        this.service = service;
    }

    public int getNumberOfPlayers() {
        return this.numberOfPlayers;
    }

    public String getId() {
        return this.id;
    }

    public boolean isStarted() {
        return this.started;
    }

    public List<Player> getPlayers() {
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

    public List<Turn> getTurns() {
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
        if (currentPlayer == null) {
            return null;
        }
        return currentPlayer.getName();
    }

    public String getWinner() {
        return winner;
    }

    public void setDirectSale(String directSale) {
        this.directSale = directSale;
    }

    public void setCurrentPlayer(String currentPlayerName) {
        Player currentPlayer = null;

        for (Player player : players) {
            if (player.getName().equals(currentPlayerName)) {
                currentPlayer = player;
                break;
            }
        }

        this.currentPlayer = currentPlayer;
    }

    public void joinGame(String playerName) {
        if (isExistedUser(playerName) || isStartedGame()) {
            throw new IllegalMonopolyActionException("You tried to do something which is against the " +
                    "rules of Monopoly. In this case, it is most likely that you tried to join a game which has " +
                    "already started, or you used a name that is already taken in this game.");
        } else {
            Player p = new Player(playerName, startingTile);
            addPlayer(p);
            changeStartedIfNeeded();
        }
    }

    private void addPlayer(Player p) {
        players.add(p);
    }

    private void changeStartedIfNeeded() {
        if (checkForReachingOfMaximumPlayers()) {
            started = true;
            currentPlayer = players.get(0);
        }
    }

    private boolean checkForReachingOfMaximumPlayers() {
        return players.size() >= numberOfPlayers;
    }

    private boolean isExistedUser(String playerName) {
        for (Player p : players) {
            if (p.getName().equals(playerName)) {
                return true;
            }
        }
        return false;
    }

    private boolean isStartedGame() {
        return this.isStarted();
    }

    public Player getPlayer(String playerName) {
        for (Player p : players) {
            if (p.getName().equals(playerName)) {
                return p;
            }
        }
        throw new MonopolyResourceNotFoundException("The player you are looking for do not exist. " +
                "Double check the name.");
    }

    public void rollDice(String playerName) {
        if (!started) {
            throw new IllegalMonopolyActionException("The game has not started yet.");
        }

        if (!currentPlayer.getName().equals(playerName)) {
            throw new IllegalMonopolyActionException("It is not your turn.");
        }

        if (directSale != null) {
            throw new IllegalMonopolyActionException("You can't roll the dice as long as you can buy property");
        }

        Turn turn = new Turn(currentPlayer);
        lastDiceRoll = turn.generateRoll();

        movePlayer(turn, lastDiceRoll);
        turns.add(turn);

        if (!Objects.equals(lastDiceRoll[0], lastDiceRoll[1])) {
            changeCurrentPlayer();
        }
    }

    private void movePlayer(Turn turn, Integer[] roll) {
        List<Tile> tiles = service.getTiles();
        Tile currentPlayerTile = service.getTile(Tile.decideNameAsPathParameter(currentPlayer.getCurrentTile()));
        int nextTileIdx = currentPlayerTile.getPosition() + roll[0] + roll[1];
        if (nextTileIdx >= tiles.size()) {
            nextTileIdx -= tiles.size();
        }
        Tile newTile = service.getTile(nextTileIdx);
        currentPlayer.MoveTo(newTile);

        turn.addMove(newTile.getName(), "Description");
    }

    private void changeCurrentPlayer() {
        int playerIdx = players.indexOf(currentPlayer);
        playerIdx++;
        if (playerIdx >= players.size()) {
            playerIdx = 0;
        }
        currentPlayer = players.get(playerIdx);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return numberOfPlayers == game.numberOfPlayers && id.equals(game.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfPlayers, id);
    }
}
