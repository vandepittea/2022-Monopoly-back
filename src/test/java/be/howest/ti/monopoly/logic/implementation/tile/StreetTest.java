package be.howest.ti.monopoly.logic.implementation.tile;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StreetTest {
    @Test
    void calculateRent(){
        Street s = new Street(1, "Peach's Garden", 60, 30, 2, "PURPLE",
                new Integer[]{10, 30, 90, 160, 250}, 50, "PURPLE", 2);

        s.addHouse();
        s.addHouse();

        assertEquals(30, s.calculateRent());
    }
}