package be.howest.ti.monopoly.logic.implementation.tile;

import be.howest.ti.monopoly.logic.implementation.*;
import be.howest.ti.monopoly.logic.implementation.enums.ChanceCard;
import be.howest.ti.monopoly.logic.implementation.enums.CommunityChest;
import be.howest.ti.monopoly.logic.implementation.turn.Turn;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CardExecutingTileTest {
    @Test
    void executeChanceCards() {
        Set<ChanceCard> gottenChanceCards = new HashSet<>();

        while (gottenChanceCards.size() != ChanceCard.values().length) {
            MonopolyService service = new MonopolyService();
            Game game = service.createGame(2, "group17");
            game.joinGame("Jonas");
            game.joinGame("Thomas");

            Player player = game.getPlayer("Jonas");
            Street street = (Street) service.getTile(1);
            player.buyProperty(street);
            player.buyProperty((Property) service.getTile(3));
            player.buyHouseOrHotel(service, game, street);

            Tile newTile = service.getTile(7);
            player.moveTo(newTile);
            Turn turn = new Turn(player);

            CardExecutingTile chanceTile = (CardExecutingTile) service.getTile(7);
            chanceTile.execute(service, game, turn);

            Player currentplayerObject = game.getCurrentPlayer();
            ChanceCard type = CardExecutingTile.getChanceType(turn.getMoves().get(0).getDescription());
            gottenChanceCards.add(type);
            switch (type) {
                case ADV_BOWSER_CASTLE:
                    assertEquals("Bowser Castle", currentplayerObject.getCurrentTile().getName());
                    assertEquals(1330, currentplayerObject.getMoney());
                    break;
                case ADV_GO:
                    assertEquals("Peach Castle", currentplayerObject.getCurrentTile().getName());
                    assertEquals(1530, currentplayerObject.getMoney());
                    break;
                case ADV_SHERBET:
                    assertEquals("Sherbet Land", currentplayerObject.getCurrentTile().getName());
                    assertEquals(1330, currentplayerObject.getMoney());
                    break;
                case ADV_DELFINO:
                    assertEquals("Delfino Airship", currentplayerObject.getCurrentTile().getName());
                    assertEquals(1330, currentplayerObject.getMoney());
                    break;
                case ADV_POWERUP:
                    assertEquals("F.L.U.D.D.", currentplayerObject.getCurrentTile().getName());
                    assertEquals(1330, currentplayerObject.getMoney());
                    break;
                case ADV_UT:
                    assertEquals("Electric Koopa Farm", currentplayerObject.getCurrentTile().getName());
                    assertEquals(1330, currentplayerObject.getMoney());
                    break;
                case REC_BANK_50:
                    assertEquals(newTile.getName(), currentplayerObject.getCurrentTile().getName());
                    assertEquals(1380, currentplayerObject.getMoney());
                    break;
                case GET_JAIL_CARD:
                    assertEquals(newTile.getName(), currentplayerObject.getCurrentTile().getName());
                    assertEquals(1, currentplayerObject.getGetOutOfJailFreeCards());
                    break;
                case RETURN_3:
                    Tile returnedTile = service.getTile(4);
                    assertEquals(returnedTile.getName(), currentplayerObject.getCurrentTile().getName());
                    assertEquals(1130, currentplayerObject.getMoney());
                    break;
                case GO_JAIL:
                    assertEquals("Jail", currentplayerObject.getCurrentTile().getName());
                    assertEquals(1330, currentplayerObject.getMoney());
                    break;
                case PAY_REPAIRS:
                    assertEquals(newTile.getName(), currentplayerObject.getCurrentTile().getName());
                    assertEquals(1305, currentplayerObject.getMoney());
                    break;
                case PAY_15:
                    assertEquals(newTile.getName(), currentplayerObject.getCurrentTile().getName());
                    assertEquals(1315, currentplayerObject.getMoney());
                    break;
                case GO_FLOWER:
                    assertEquals("Fire Flower", currentplayerObject.getCurrentTile().getName());
                    assertEquals(1530, currentplayerObject.getMoney());
                    break;
                case PAY_PLAYERS_50:
                    assertEquals(newTile.getName(), currentplayerObject.getCurrentTile().getName());
                    assertEquals(1280, currentplayerObject.getMoney());
                    assertEquals(1550, game.getPlayer("Thomas").getMoney());
                    break;
                case REC_150:
                    assertEquals(newTile.getName(), currentplayerObject.getCurrentTile().getName());
                    assertEquals(1480, currentplayerObject.getMoney());
                    break;
            }
        }
    }

    @Test
    void executeCommunityCards() {
        Set<CommunityChest> gottenCommmunityChests = new HashSet<>();

        while (gottenCommmunityChests.size() != CommunityChest.values().length) {
            MonopolyService service = new MonopolyService();
            Game game = service.createGame(2, "group17");
            game.joinGame("Jonas");
            game.joinGame("Thomas");

            Player player = game.getPlayer("Jonas");
            Street street = (Street) service.getTile(1);
            player.buyProperty(street);
            player.buyProperty((Property) service.getTile(3));
            player.buyHouseOrHotel(service, game, street);

            Tile newTile = service.getTile(17);
            player.moveTo(newTile);
            Turn turn = new Turn(player);

            CardExecutingTile communityTile = (CardExecutingTile) service.getTile(17);
            communityTile.execute(service, game, turn);

            Player currentplayerObject = game.getCurrentPlayer();
            CommunityChest type = CardExecutingTile.getCommunityType(turn.getMoves().get(0).getDescription());
            gottenCommmunityChests.add(type);
            switch (type) {
                case ADV_GO:
                    assertEquals("Peach Castle", currentplayerObject.getCurrentTile().getName());
                    assertEquals(1530, currentplayerObject.getMoney());
                    break;
                case REC_BANK_ERR:
                    assertEquals(newTile.getName(), currentplayerObject.getCurrentTile().getName());
                    assertEquals(1530, currentplayerObject.getMoney());
                    break;
                case PAY_DOCTOR_FEE:
                case PAY_STAR:
                    assertEquals(newTile.getName(), currentplayerObject.getCurrentTile().getName());
                    assertEquals(1280, currentplayerObject.getMoney());
                    break;
                case REC_SOLD_STOCK:
                    assertEquals(newTile.getName(), currentplayerObject.getCurrentTile().getName());
                    assertEquals(1380, currentplayerObject.getMoney());
                    break;
                case GET_JAIL_CARD:
                    assertEquals(newTile.getName(), currentplayerObject.getCurrentTile().getName());
                    assertEquals(1, currentplayerObject.getGetOutOfJailFreeCards());
                    break;
                case GO_JAIL:
                    assertEquals("Jail", currentplayerObject.getCurrentTile().getName());
                    assertEquals(1330, currentplayerObject.getMoney());
                    break;
                case REC_DELFINO:
                case REC_KOOPALING:
                case REC_ROSALINA:
                    assertEquals(newTile.getName(), currentplayerObject.getCurrentTile().getName());
                    assertEquals(1430, currentplayerObject.getMoney());
                    break;
                case REC_BANK_OWES:
                    assertEquals(newTile.getName(), currentplayerObject.getCurrentTile().getName());
                    assertEquals(1350, currentplayerObject.getMoney());
                    break;
                case REC_BIRTHDAY:
                    assertEquals(newTile.getName(), currentplayerObject.getCurrentTile().getName());
                    assertEquals(1340, currentplayerObject.getMoney());
                    assertEquals(1490, game.getPlayer("Thomas").getMoney());
                    break;
                case PAY_BOO:
                    assertEquals(newTile.getName(), currentplayerObject.getCurrentTile().getName());
                    assertEquals(1230, currentplayerObject.getMoney());
                    break;
                case REC_FRUIT:
                    assertEquals(newTile.getName(), currentplayerObject.getCurrentTile().getName());
                    assertEquals(1355, currentplayerObject.getMoney());
                    break;
                case PAY_REPAIRS:
                    assertEquals(newTile.getName(), currentplayerObject.getCurrentTile().getName());
                    assertEquals(1290, currentplayerObject.getMoney());
                    break;
                case REC_PRIZE:
                    assertEquals(newTile.getName(), currentplayerObject.getCurrentTile().getName());
                    assertEquals(1340, currentplayerObject.getMoney());
                    break;
            }
        }
    }
}