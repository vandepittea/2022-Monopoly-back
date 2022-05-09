package be.howest.ti.monopoly.logic.implementation.tile;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StreetTest {
    @Test
    void calculateRentNoHouses(){
        Street s = new Street(1, "Peach's Garden", 60, 30, 2,
                "PURPLE", new Integer[]{10, 30, 90, 160, 250}, 50, "PURPLE", 2);

        assertEquals(2, s.calculateRent(null, null));
    }

    @Test
    void calculateRentThreeHouses(){
        Street s = new Street(1, "Peach's Garden", 60, 30, 2,
                "PURPLE", new Integer[]{10, 30, 90, 160, 250}, 50, "PURPLE", 2);

        s.buyHouse();
        s.buyHouse();
        s.buyHouse();

        assertEquals(90, s.calculateRent(null, null));
    }

    @Test
    void calculateRentOneHotel(){
        Street s = new Street(1, "Peach's Garden", 60, 30, 2,
                "PURPLE", new Integer[]{10, 30, 90, 160, 250}, 50, "PURPLE", 2);

        s.buyHouse();
        s.buyHouse();
        s.buyHouse();
        s.buyHouse();
        s.buyHotel();

        assertEquals(250, s.calculateRent(null, null));
    }
}