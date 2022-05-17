package be.howest.ti.monopoly.logic.implementation.tile;

import be.howest.ti.monopoly.logic.implementation.Game;
import be.howest.ti.monopoly.logic.implementation.MonopolyService;
import be.howest.ti.monopoly.logic.implementation.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StreetTest {
    MonopolyService service = new MonopolyService();
    Game game = service.createGame(2, "group17");
    Player player = new Player("Thomas", null);

    Street street1 = (Street) service.getTile(1);
    Street street2 = (Street) service.getTile(3);

    @Test
    void calculateRentNoHouses(){
        player.buyProperty(street1);

        assertEquals(2, street1.calculateRent(player, game));
    }

    @Test
    void calculateRentThreeHouses(){
        player.buyProperty(street1);
        player.buyProperty(street2);

        player.buyHouseOrHotel(service, game, street1);
        player.buyHouseOrHotel(service, game, street2);
        player.buyHouseOrHotel(service, game, street1);
        player.buyHouseOrHotel(service, game, street2);
        player.buyHouseOrHotel(service, game, street1);

        assertEquals(90, street1.calculateRent(player, game));
    }

    @Test
    void calculateRentOneHotel(){
        player.buyProperty(street1);
        player.buyProperty(street2);

        player.buyHouseOrHotel(service, game, street1);
        player.buyHouseOrHotel(service, game, street2);
        player.buyHouseOrHotel(service, game, street1);
        player.buyHouseOrHotel(service, game, street2);
        player.buyHouseOrHotel(service, game, street1);
        player.buyHouseOrHotel(service, game, street2);
        player.buyHouseOrHotel(service, game, street1);
        player.buyHouseOrHotel(service, game, street2);
        player.buyHouseOrHotel(service, game, street1);

        assertEquals(250, street1.calculateRent(player, game));
    }
}