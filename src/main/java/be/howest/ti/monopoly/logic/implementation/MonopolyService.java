package be.howest.ti.monopoly.logic.implementation;

import be.howest.ti.monopoly.logic.ServiceAdapter;

import java.util.List;


public class MonopolyService extends ServiceAdapter {

    @Override
    public String getVersion() {
        return "0.0.1";
    }

    @Override
    public List<Tile> getTiles(){
        return List.of(
                new Tile(0, "Go"),
                new Tile(1, "Med"),
                new Tile (2, "Community Chest")
        );
    }
}
