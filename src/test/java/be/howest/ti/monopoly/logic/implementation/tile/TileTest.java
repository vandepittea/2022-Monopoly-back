package be.howest.ti.monopoly.logic.implementation.tile;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {
    @Test
    void decideNameAsPathParameter(){
        Tile t = new Tile(15, "this is a name of a tile");

        assertEquals("this_is_a_name_of_a_tile", t.getNameAsPathParameter());
    }
}