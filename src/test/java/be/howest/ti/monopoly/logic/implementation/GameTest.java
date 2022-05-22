package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.exceptions.IllegalMonopolyActionException;
import be.howest.ti.monopoly.logic.exceptions.MonopolyResourceNotFoundException;
import be.howest.ti.monopoly.logic.implementation.enums.TileType;
import be.howest.ti.monopoly.logic.implementation.tile.Tile;
import be.howest.ti.monopoly.logic.implementation.turn.DiceRoll;
import be.howest.ti.monopoly.logic.implementation.turn.Turn;
import be.howest.ti.monopoly.logic.implementation.enums.TurnType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    MonopolyService service;
    Game game;

    @BeforeEach
    void init() {
        service = new MonopolyService();
        game = service.createGame(2, "group17", "gameName");
    }

    @Test
    void joinGameSuccesful(){
        Player p = new Player("Bob", null);

        game.joinGame("Bob");

        assertTrue(game.getPlayers().contains(p));
        assertFalse(game.isStarted());
    }

    @Test
    void joinGameAndStartGame(){
        Player p = new Player("Bob", null);
        Player p2 = new Player("Jan", null);

        game.joinGame("Bob");
        game.getPlayer("Bob").assignPawn(game, "pawnBob");
        game.joinGame("Jan");
        game.getPlayer("Jan").assignPawn(game, "pawnJan");


        assertTrue(game.getPlayers().contains(p));
        assertTrue(game.getPlayers().contains(p2));
        assertTrue(game.isStarted());
    }

    @Test
    void joinGameTwoUserWithTheSameName(){
        game.joinGame("Bob");

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> game.joinGame( "Bob"));
    }

    @Test
    void joinAlreadyStartedGame() {
        Player player1 = new Player("Bob", null);
        Player player2 = new Player("Jan", null);

        game.joinGame("Bob");
        game.getPlayer("Bob").assignPawn(game, "pawnBob");
        game.joinGame("Jan");
        game.getPlayer("Jan").assignPawn(game, "pawnJan");

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> game.joinGame( "Jonas"));
    }

    @Test
    void getPlayerSuccesful(){
        Player player = new Player("Bob", null);
        game.joinGame("Bob");

        assertEquals(player, game.getPlayer("Bob"));
    }

    @Test
    void getPlayerUnexistedPlayer(){
        Assertions.assertThrows(MonopolyResourceNotFoundException.class, () -> game.getPlayer("Unexisting"));
    }

    @Test
    void rollDiceGameNotStarted() {
        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> game.rollDice("Jonas"));
    }

    @Test
    void rollDiceGameEnded() {
        Player player1 = new Player("Jonas", null);
        Player player2 = new Player("Thomas", null);

        game.joinGame("Jonas");
        game.getPlayer("Jonas").assignPawn(game, "pawnJonas");
        game.joinGame("Thomas");
        game.getPlayer("Thomas").assignPawn(game, "pawnThomas");

        game.declareBankruptcy("Jonas");

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> game.rollDice("Thomas"));
    }

    @Test
    void rollDiceInDebt() {
        Player player1 = new Player("Jonas", null);
        Player player2 = new Player("Thomas", null);

        game.joinGame("Jonas");
        player1.assignPawn(game, "pawnJonas");
        game.joinGame("Thomas");
        player2.assignPawn(game, "pawnThomas");

        game.setCurrentPlayer("Jonas");
        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> game.getCurrentPlayer().payDebt(5000, player2));
        assertEquals(5000, game.getCurrentPlayer().getDebt());
        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> game.rollDice("Jonas"));
    }

    @Test
    void rollDiceWrongPlayer() {
        game.joinGame("Jonas");
        game.joinGame("Thomas");
        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> game.rollDice("Thomas"));
    }

    @Test
    void rollDiceWhenDirectSale() {
        Player player1 = new Player("Jonas", null);
        Player player2 = new Player("Thomas", null);

        game.joinGame("Jonas");
        game.getPlayer("Jonas").assignPawn(game, "pawnJonas");
        game.joinGame("Thomas");
        game.getPlayer("Thomas").assignPawn(game, "pawnThomas");

        while (game.getDirectSale() == null) {
            assertEquals(game, service.rollDice(game.getId(), game.getCurrentPlayer().getName()));
        }
        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> game.rollDice("Jonas"));
        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> game.rollDice("Thomas"));
    }

    @Test
    void rollDiceToJail() {
        Player player1 = new Player("Jonas", null);
        Player player2 = new Player("Thomas", null);

        game.joinGame("Jonas");
        game.getPlayer("Jonas").assignPawn(game, "pawnJonas");
        game.joinGame("Thomas");
        game.getPlayer("Thomas").assignPawn(game, "pawnThomas");

        Turn lastTurn;
        do {
            game.rollDice(game.getCurrentPlayer().getName());
            lastTurn = game.getTurns().get(game.getTurns().size() - 1);

            if (game.getDirectSale() != null) {
                service.dontBuyProperty(game.getId(), game.getCurrentPlayer().getName(), Tile.decideNameAsPathParameter(game.getDirectSale()));
            }
        } while (lastTurn.getType() != TurnType.GO_TO_JAIL);

        Player currentPlayer = game.getCurrentPlayer();
        List<Turn> turns = game.getTurns();
        for (int i = 2; i < turns.size(); i++) {
            Turn turn1 = turns.get(i - 2);
            Turn turn2 = turns.get(i - 1);
            Turn turn3 = turns.get(i);

            if (turn1.getPlayer().equals(turn2.getPlayer()) && turn2.getPlayer().equals(turn3.getPlayer())) {
                DiceRoll roll1 = turn1.getRoll();
                DiceRoll roll2 = turn2.getRoll();
                DiceRoll roll3 = turn3.getRoll();

                if (roll1.isDoubleRoll() && roll2.isDoubleRoll() && roll3.isDoubleRoll()) {
                    assertEquals(i, turns.size() - 1);
                    assertTrue(currentPlayer.isJailed());
                }
            }
        }
    }

    @Test
    void rollDiceGoToJailTile() {
        Player player1 = new Player("Jonas", null);
        Player player2 = new Player("Thomas", null);

        game.joinGame("Jonas");
        game.getPlayer("Jonas").assignPawn(game, "pawnJonas");
        game.joinGame("Thomas");
        game.getPlayer("Thomas").assignPawn(game, "pawnThomas");

        Turn lastTurn;
        do {
            game.rollDice(game.getCurrentPlayer().getName());
            lastTurn = game.getTurns().get(game.getTurns().size() - 1);

            if (game.getDirectSale() != null) {
                service.dontBuyProperty(game.getId(), game.getCurrentPlayer().getName(), Tile.decideNameAsPathParameter(game.getDirectSale()));
            }
        } while (!lastTurn.getMoves().get(0).getTile().getName().equals("Go to Jail"));

        assertTrue(game.getPlayer(lastTurn.getPlayer()).isJailed());
    }

    @Test
    void rollDiceThriceWhenInJail() {
        boolean endedWithoutDoubleRoll = false;
        while(!endedWithoutDoubleRoll) {
            game = service.createGame(2, "group17", "jailGame");
            Player player1 = new Player("Jonas", null);
            Player player2 = new Player("Thomas", null);

            game.joinGame("Jonas");
            game.getPlayer("Jonas").assignPawn(game, "pawnJonas");
            game.joinGame("Thomas");
            game.getPlayer("Thomas").assignPawn(game, "pawnThomas");

            Turn turn = new Turn(game.getPlayer("Jonas"));
            game.jailCurrentPlayer(turn);
            game.getTurns().add(turn);

            for (int i = 0; i < 2; i++) {
                turn = new Turn(game.getPlayer("Jonas"), 1, 2);
                turn.setType(TurnType.JAIL_STAY);
                turn.addMove(service.getTile("Jail"), "");
                game.getTurns().add(turn);
            }

            game.rollDice("Jonas");
            Turn lastTurn = game.getTurns().get(game.getTurns().size() - 1);

            TileType type = lastTurn.getMoves().get(lastTurn.getMoves().size() - 1).getTile().getType();

            if ((type == TileType.CHANCE) || (type == TileType.COMMUNITY_CHEST)) {
                if (game.getPlayer("Jonas").getGetOutOfJailFreeCards() > 0) {
                    assertEquals(1450, game.getPlayer("Jonas").getMoney());
                } else {
                    assertNotEquals(1450, game.getPlayer("Jonas").getMoney());
                }
            } else if (type == TileType.GO) {
                assertEquals(1700, game.getPlayer("Jonas").getMoney());
            } else {
                if (lastTurn.getRoll().isDoubleRoll()){
                    assertEquals(1500, game.getPlayer("Jonas").getMoney());
                } else {
                    assertEquals(1450, game.getPlayer("Jonas").getMoney());
                    endedWithoutDoubleRoll = true;
                }
            }
        }
    }

    @Test
    void rollDicePassGo() {
        Player player1 = new Player("Jonas", null);
        Player player2 = new Player("Thomas", null);

        game.joinGame("Jonas");
        game.getPlayer("Jonas").assignPawn(game, "pawnJonas");
        game.joinGame("Thomas");
        game.getPlayer("Thomas").assignPawn(game, "pawnThomas");

        game.getCurrentPlayer().moveTo(service.getTile(39));
        game.rollDice("Jonas");
        assertTrue(1500 <= game.getPlayer("Jonas").getMoney());
    }

    @Test
    void rollDice() {
        Player player1 = new Player("Jonas", null);
        Player player2 = new Player("Thomas", null);

        game.joinGame("Jonas");
        game.getPlayer("Jonas").assignPawn(game, "pawnJonas");
        game.joinGame("Thomas");
        game.getPlayer("Thomas").assignPawn(game, "pawnThomas");

        game.setCurrentPlayer("Jonas");
        assertEquals("Peach Castle", game.getPlayer("Jonas").getCurrentTile().getName());
        assertEquals(game, service.rollDice(game.getId(), "Jonas"));
        assertNotEquals("Peach Castle", game.getPlayer("Jonas").getCurrentTile().getName());

        DiceRoll lastDiceRoll = game.getLastDiceRoll();
        if ((game.getDirectSale() == null) && !lastDiceRoll.isDoubleRoll()) {
            assertEquals("Thomas", game.getCurrentPlayer().getName());
        } else {
            assertEquals("Jonas", game.getCurrentPlayer().getName());
        }

        assertEquals(1, game.getTurns().size());
        assertEquals(game.getLastDiceRoll().getDie1(), game.getTurns().get(game.getTurns().size() - 1).getRoll().getRoll()[0]);
        assertEquals(game.getLastDiceRoll().getDie2(), game.getTurns().get(game.getTurns().size() - 1).getRoll().getRoll()[1]);
    }

    @Test
    void declareBankruptcy(){
        Game game = service.createGame(3, "group17", "gameName");
        Player player1 = new Player("Bob", null);
        Player player2 = new Player("Jan", null);
        Player player3 = new Player("Tim", null);

        game.joinGame("Bob");
        game.getPlayer("Bob").assignPawn(game, "pawnBob");
        game.joinGame("Jan");
        game.getPlayer("Jan").assignPawn(game, "pawnJan");
        game.joinGame("Tim");
        game.getPlayer("Tim").assignPawn(game, "pawnTim");
        game.declareBankruptcy("Bob");

        assertNotEquals(player1, game.getCurrentPlayer());
        assertTrue(game.getPlayers().get(0).isBankrupt());
        assertTrue(game.getPlayers().get(0).getProperties().isEmpty());
        assertEquals(0, game.getPlayers().get(0).getMoney());
    }
}