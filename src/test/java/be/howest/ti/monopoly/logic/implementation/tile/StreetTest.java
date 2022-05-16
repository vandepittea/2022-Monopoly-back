package be.howest.ti.monopoly.logic.implementation.tile;

import be.howest.ti.monopoly.logic.implementation.Game;
import be.howest.ti.monopoly.logic.implementation.MonopolyService;
import be.howest.ti.monopoly.logic.implementation.Player;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StreetTest {
    MonopolyService service = new MonopolyService();
    Game g = service.createGame(2, "group17");
    Player p = new Player("Thomas", null);

    @Test
    void calculateRentNoHouses(){
        Street s = (Street) service.getTile(1);
        p.buyProperty(s);

        assertEquals(2, s.calculateRent(p, g));
    }

    @Test
    void calculateRentThreeHouses(){
        Street s = (Street) service.getTile(1);
        Street s2 = (Street) service.getTile(3);

        p.buyProperty(s);
        p.buyProperty(s2);

        p.buyHouseOrHotel(service, g, s);
        p.buyHouseOrHotel(service, g, s2);
        p.buyHouseOrHotel(service, g, s);
        p.buyHouseOrHotel(service, g, s2);
        p.buyHouseOrHotel(service, g, s);

        assertEquals(90, s.calculateRent(p, g));
    }

    @Test
    void calculateRentOneHotel(){
        Street s = (Street) service.getTile(1);
        Street s2 = (Street) service.getTile(3);

        p.buyProperty(s);
        p.buyProperty(s2);

        p.buyHouseOrHotel(service, g, s);
        p.buyHouseOrHotel(service, g, s2);
        p.buyHouseOrHotel(service, g, s);
        p.buyHouseOrHotel(service, g, s2);
        p.buyHouseOrHotel(service, g, s);
        p.buyHouseOrHotel(service, g, s2);
        p.buyHouseOrHotel(service, g, s);
        p.buyHouseOrHotel(service, g, s2);
        p.buyHouseOrHotel(service, g, s);

        assertEquals(250, s.calculateRent(p, g));
    }
}