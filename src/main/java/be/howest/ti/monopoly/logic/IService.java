package be.howest.ti.monopoly.logic;

import be.howest.ti.monopoly.logic.implementation.Game;

public interface IService {
    String getVersion();
    Game createGame();
}
