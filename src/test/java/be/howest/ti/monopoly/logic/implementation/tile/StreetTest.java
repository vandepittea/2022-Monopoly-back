package be.howest.ti.monopoly.logic.implementation.tile;

import be.howest.ti.monopoly.logic.implementation.Game;
import be.howest.ti.monopoly.logic.implementation.MonopolyService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StreetTest {
    MonopolyService service = new MonopolyService();
    Game g = service.createGame(2, "group17");

    @Test
    void calculateRentNoHouses(){
        Street s = new Street(1, "Peach's Garden", 60, 30, 2,
                "PURPLE", new Integer[]{10, 30, 90, 160, 250}, 50, "PURPLE", 2);

        assertEquals(2, s.calculateRent(null, g));
    }

    @Test
    void calculateRentThreeHouses(){
        Street s = new Street(1, "Peach's Garden", 60, 30, 2,
                "PURPLE", new Integer[]{10, 30, 90, 160, 250}, 50, "PURPLE", 2);

        s.buyHouse(g);
        s.buyHouse(g);
        s.buyHouse(g);

        assertEquals(90, s.calculateRent(null, g));
    }

    @Test
    void calculateRentOneHotel(){
        Street s = new Street(1, "Peach's Garden", 60, 30, 2,
                "PURPLE", new Integer[]{10, 30, 90, 160, 250}, 50, "PURPLE", 2);

        s.buyHouse(g);
        s.buyHouse(g);
        s.buyHouse(g);
        s.buyHouse(g);
        s.buyHotel(g);

        assertEquals(250, s.calculateRent(null, g));
    }
}