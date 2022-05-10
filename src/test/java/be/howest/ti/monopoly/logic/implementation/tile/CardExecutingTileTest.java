package be.howest.ti.monopoly.logic.implementation.tile;

import be.howest.ti.monopoly.logic.implementation.ChanceCards;
import be.howest.ti.monopoly.logic.implementation.Game;
import be.howest.ti.monopoly.logic.implementation.MonopolyService;
import be.howest.ti.monopoly.logic.implementation.Player;
import be.howest.ti.monopoly.logic.implementation.turn.Turn;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CardExecutingTileTest {

    @Test
    void execute() {
        Set<ChanceCards> gottenChanceCards = new HashSet<>();

        while (gottenChanceCards.size() != ChanceCards.values().length) {
            MonopolyService service = new MonopolyService();
            Game game = service.createGame(2, "group17");
            game.joinGame("Jonas");
            game.joinGame("Thomas");

            Player player = game.getPlayer("Jonas");
            Street street = (Street) service.getTile(1);
            player.buyProperty(street);
            player.buyProperty((Property) service.getTile(3));
            player.buyHouseOrHotel(service, game, street);

            Tile newTile = service.getTile(12);
            player.moveTo(newTile);
            Turn turn = new Turn(player);

            CardExecutingTile chanceCard = (CardExecutingTile) service.getTile(7);
            chanceCard.execute(service, game, turn);

            Player currentplayerObject = game.getCurrentplayerObject();
            ChanceCards type = CardExecutingTile.getChanceType(turn.getMoves().get(0).getDescription());
            gottenChanceCards.add(type);
            switch (type) {
                case ADV_BOWSER_CASTLE:
                    assertEquals("Bowser Castle", currentplayerObject.getCurrentTile());
                    assertEquals(1330, currentplayerObject.getMoney());
                    break;
                case ADV_GO:
                    assertEquals("Peach Castle", currentplayerObject.getCurrentTile());
                    assertEquals(1530, currentplayerObject.getMoney());
                    break;
                case ADV_SHERBET:
                    assertEquals("Sherbet Land", currentplayerObject.getCurrentTile());
                    assertEquals(1330, currentplayerObject.getMoney());
                    break;
                case ADV_DELFINO:
                    assertEquals("Delfino Airship", currentplayerObject.getCurrentTile());
                    assertEquals(1530, currentplayerObject.getMoney());
                    break;
                case ADV_POWERUP:
                    assertEquals("F.L.U.D.D.", currentplayerObject.getCurrentTile());
                    assertEquals(1330, currentplayerObject.getMoney());
                    break;
                case ADV_UT:
                    assertEquals("Gas Pump", currentplayerObject.getCurrentTile());
                    assertEquals(1330, currentplayerObject.getMoney());
                    break;
                case REC_BANK_50:
                    assertEquals(newTile.getName(), currentplayerObject.getCurrentTile());
                    assertEquals(1380, currentplayerObject.getMoney());
                    break;
                case JAIL_CARD:
                    assertEquals(newTile.getName(), currentplayerObject.getCurrentTile());
                    assertEquals(1, currentplayerObject.getGetOutOfJailCards());
                    break;
                case RETURN_3:
                    Tile returnedTile = service.getTile(9);
                    assertEquals(returnedTile.getName(), currentplayerObject.getCurrentTile());
                    assertEquals(1330, currentplayerObject.getMoney());
                    break;
                case GO_JAIL:
                    assertEquals("Jail", currentplayerObject.getCurrentTile());
                    assertEquals(1330, currentplayerObject.getMoney());
                    break;
                case PAY_REPAIRS:
                    assertEquals(newTile.getName(), currentplayerObject.getCurrentTile());
                    assertEquals(1305, currentplayerObject.getMoney());
                    break;
                case PAY_15:
                    assertEquals(newTile.getName(), currentplayerObject.getCurrentTile());
                    assertEquals(1315, currentplayerObject.getMoney());
                    break;
                case GO_FLOWER:
                    assertEquals("Fire Flower", currentplayerObject.getCurrentTile());
                    assertEquals(1530, currentplayerObject.getMoney());
                    break;
                case PAY_PLAYERS_50:
                    assertEquals(newTile.getName(), currentplayerObject.getCurrentTile());
                    assertEquals(1280, currentplayerObject.getMoney());
                    assertEquals(1550, game.getPlayer("Thomas").getMoney());
                    break;
                case REC_150:
                    assertEquals(newTile.getName(), currentplayerObject.getCurrentTile());
                    assertEquals(1480, currentplayerObject.getMoney());
                    break;
            }
        }
    }
}