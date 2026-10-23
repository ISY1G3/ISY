package model;

// iedere game moet wat hier in staat hebben dus een start, eind en het hebben van een status
public interface Game {

    void start();

    void finish();

    GameStatus getGameStatus();
}
