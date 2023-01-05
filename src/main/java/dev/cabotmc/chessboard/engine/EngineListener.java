package dev.cabotmc.chessboard.engine;

public interface EngineListener {
    void accept(EngineMessage msg);
}
