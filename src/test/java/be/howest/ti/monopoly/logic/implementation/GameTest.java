package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.exceptions.IllegalMonopolyActionException;
import be.howest.ti.monopoly.logic.exceptions.MonopolyResourceNotFoundException;
import be.howest.ti.monopoly.logic.implementation.tile.Tile;
import be.howest.ti.monopoly.logic.implementation.turn.Turn;
import be.howest.ti.monopoly.logic.implementation.enums.TurnType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    MonopolyService service = new MonopolyService();
    Game game = service.createGame(2, "group17");

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
        game.joinGame("Jan");

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
        Game g = service.createGame(2, "group17");

        g.joinGame("Bob");
        g.joinGame("Jan");

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> g.joinGame( "Jonas"));
    }

    @Test
    void getPlayerSuccesful(){
        Game g = service.createGame(2, "group17");
        Player p = new Player("Bob", null);
        g.joinGame("Bob");

        assertEquals(p, g.getPlayer("Bob"));
    }

    @Test
    void getPlayerUnexistedPlayer(){
        Game g = service.createGame(2, "group17");

        Assertions.assertThrows(MonopolyResourceNotFoundException.class, () -> g.getPlayer("Unexisted"));
    }

    @Test
    void rollDiceGameNotStarted() {
        Game game = service.createGame(2, "group17");
        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> game.rollDice("Jonas"));
    }

    //TODO: make rollDice test for an ended game
    //TODO: make rollDice test for a player in debt, when debt is properly implemented

    @Test
    void rollDiceWrongPlayer() {
        Game game = service.createGame(2, "group17");
        game.joinGame("Jonas");
        game.joinGame("Thomas");
        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> game.rollDice("Thomas"));
    }

    @Test
    void rollDiceWhenDirectSale() {
        Game game = service.createGame(2, "group17");
        game.joinGame("Jonas");
        game.joinGame("Thomas");
        while (game.getDirectSale() == null) {
            assertEquals(game, service.rollDice(game.getId(), game.getCurrentPlayer().getName()));
        }
        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> game.rollDice("Jonas"));
        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> game.rollDice("Thomas"));
    }

    @Test
    void rollDiceToJail() {
        Game game = service.createGame(2, "group17");
        game.joinGame("Jonas");
        game.joinGame("Thomas");

        Turn lastTurn = null;
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
                Integer[] roll1 = turn1.getRoll().getRoll();
                Integer[] roll2 = turn2.getRoll().getRoll();
                Integer[] roll3 = turn3.getRoll().getRoll();

                if ((roll1[0] == roll1[1]) && (roll2[0] == roll2[1]) && (roll3[0] == roll3[1])) {
                    assertEquals(i, turns.size() - 1);
                    assertTrue(currentPlayer.isJailed());
                }
            }
        }
    }

    @Test
    void rollDiceGoToJailTile() {
        Game game = service.createGame(2, "group17");
        game.joinGame("Jonas");
        game.joinGame("Thomas");

        Turn lastTurn = null;
        do {
            game.rollDice(game.getCurrentPlayer().getName());
            lastTurn = game.getTurns().get(game.getTurns().size() - 1);

            if (game.getDirectSale() != null) {
                service.dontBuyProperty(game.getId(), game.getCurrentPlayer().getName(), Tile.decideNameAsPathParameter(game.getDirectSale()));
            }
        } while (!lastTurn.getMoves().get(0).getTitle().equals("Go to Jail"));

        assertTrue(game.getPlayer(lastTurn.getPlayer()).isJailed());
    }

    @Test
    void rollDicePassGo() {
        Game game = service.createGame(2, "group17");
        game.joinGame("Jonas");
        game.joinGame("Thomas");

        game.getCurrentPlayer().moveTo(service.getTile(39));
        game.rollDice("Jonas");
        assertTrue(1500 < game.getPlayer("Jonas").getMoney());
    }

    @Test
    void rollDice() {
        Game game = service.createGame(2, "group17");

        game.joinGame("Jonas");
        game.joinGame("Thomas");

        assertEquals("Peach Castle", game.getPlayer("Jonas").getCurrentTile());
        assertEquals(game, service.rollDice(game.getId(), "Jonas"));
        assertNotEquals("Peach Castle", game.getPlayer("Jonas").getCurrentTile());

        Integer[] lastDiceRoll = game.getLastDiceRoll();
        if ((game.getDirectSale() == null) && (!lastDiceRoll[0].equals(lastDiceRoll[1]))) {
            assertEquals("Thomas", game.getCurrentPlayer().getName());
        } else {
            assertEquals("Jonas", game.getCurrentPlayer().getName());
        }

        assertEquals(1, game.getTurns().size());
        assertEquals(game.getLastDiceRoll()[0], game.getTurns().get(game.getTurns().size() - 1).getRoll().getRoll()[0]);
        assertEquals(game.getLastDiceRoll()[1], game.getTurns().get(game.getTurns().size() - 1).getRoll().getRoll()[1]);
    }

    @Test
    void declareBankruptcy(){
        Game g = service.createGame(3, "group17");

        g.joinGame("Bob");
        g.joinGame("Jan");
        g.joinGame("Tim");
        g.declareBankruptcy("Bob");

        assertNotEquals("Bob", game.getCurrentPlayer());
        assertTrue(g.getPlayers().get(0).isBankrupt());
        assertTrue(g.getPlayers().get(0).getProperties().isEmpty());
        assertEquals(0, g.getPlayers().get(0).getMoney());
    }
}