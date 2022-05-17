package be.howest.ti.monopoly.logic.implementation.tile;

import be.howest.ti.monopoly.logic.implementation.enums.TileType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {
    @Test
    void decideNameAsPathParameter(){
        SimpleTile t = new SimpleTile(15, "this is a name of a tile", TileType.GO);

        assertEquals("this_is_a_name_of_a_tile", t.getNameAsPathParameter());
    }
}