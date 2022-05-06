package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.exceptions.IllegalMonopolyActionException;
import be.howest.ti.monopoly.logic.exceptions.MonopolyResourceNotFoundException;
import be.howest.ti.monopoly.logic.implementation.tile.Property;
import be.howest.ti.monopoly.web.views.PropertyView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    void buyPropertySuccesful(){
        Player pl = new Player("Bob");
        Property pr = new Property(1, "name", 5, 3, 2, "PURPLE");
        PropertyView prView = new PropertyView(pr);

        pl.buyProperty(pr);

        assertEquals(1500 - 5, pl.getMoney());
        assertTrue(pl.getProperties().contains(prView));
    }

    @Test
    void buyPropertyFailPayment(){
        Player pl = new Player("Bob");
        Property pr = new Property(1, "name", 1505, 3, 2, "PURPLE");

        Assertions.assertThrows(IllegalMonopolyActionException.class, () -> pl.buyProperty(pr));
    }
}