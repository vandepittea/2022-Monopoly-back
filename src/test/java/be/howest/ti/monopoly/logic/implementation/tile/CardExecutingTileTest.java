package be.howest.ti.monopoly.logic.implementation.tile;

import be.howest.ti.monopoly.logic.implementation.Game;
import be.howest.ti.monopoly.logic.implementation.MonopolyService;
import be.howest.ti.monopoly.logic.implementation.Player;
import be.howest.ti.monopoly.logic.implementation.turn.Turn;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CardExecutingTileTest {

    @Test
    void execute() {
        MonopolyService service = new MonopolyService();
        Game game = service.createGame(2, "group17");
        game.joinGame("Jonas");
        game.joinGame("Thomas");

        Player player = game.getPlayer("Jonas");
        player.buyProperty((Property) service.getTile(1));

        Tile newTile = service.getTile(12);
        player.moveTo(newTile);
        Turn turn = new Turn(player);

        CardExecutingTile chanceCard = (CardExecutingTile) service.getTile(7);
        chanceCard.execute(service, game, turn);

        Player currentplayerObject = game.getCurrentplayerObject();
        switch (CardExecutingTile.getChanceType(turn.getMoves().get(0).getDescription())) {
            case ADV_BOWSER_CASTLE:
                assertEquals("Bowser Castle", currentplayerObject.getCurrentTile());
                assertEquals(1500, currentplayerObject.getMoney());
                break;
            case ADV_GO:
                assertEquals("Peach's Castle", currentplayerObject.getCurrentTile());
                assertEquals(1700, currentplayerObject.getMoney());
                break;
            case ADV_SHERBET:
                assertEquals("Sherbet Land", currentplayerObject.getCurrentTile());
                assertEquals(1500, currentplayerObject.getMoney());
                break;
            case ADV_DELFINO:
                assertEquals("Delfino Airship", currentplayerObject.getCurrentTile());
                assertEquals(1700, currentplayerObject.getMoney());
                break;
            case ADV_POWERUP:
                assertEquals("F.L.U.D.D.", currentplayerObject.getCurrentTile());
                assertEquals(1500, currentplayerObject.getMoney());
                break;
            case ADV_UT:
                assertEquals("Gas Pump", currentplayerObject.getCurrentTile());
                assertEquals(1500, currentplayerObject.getMoney());
                break;
            case REC_BANK_50:
                assertEquals(newTile.getName(), currentplayerObject.getCurrentTile());
                assertEquals(1550, currentplayerObject.getMoney());
                break;
            case JAIL_CARD:
                assertEquals(newTile.getName(), currentplayerObject.getCurrentTile());
                assertEquals(1, currentplayerObject.getGetOutOfJailCards());
                break;
            case RETURN_3:
                Tile returnedTile = service.getTile(9);
                assertEquals(returnedTile.getName(), currentplayerObject.getCurrentTile());
                assertEquals(1500, currentplayerObject.getMoney());
                break;
            case GO_JAIL:
                assertEquals("Jail", currentplayerObject.getCurrentTile());
                assertEquals(1500, currentplayerObject.getMoney());
                break;
            case PAY_REPAIRS:
                assertEquals(newTile.getName(), currentplayerObject.getCurrentTile());
                assertEquals(1500, currentplayerObject.getMoney());
                break;
            case PAY_15:
                assertEquals(newTile.getName(), currentplayerObject.getCurrentTile());
                assertEquals(1485, currentplayerObject.getMoney());
                break;
            case GO_FLOWER:
                break;
            case PAY_PLAYERS_50:
                break;
            case REC_150:
                break;
        }
    }
}