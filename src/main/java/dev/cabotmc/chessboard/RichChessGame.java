package dev.cabotmc.chessboard;

import com.github.bhlangonijr.chesslib.*;
import dev.cabotmc.chessboard.engine.EngineProcess;

public class RichChessGame {
    EngineProcess engine = new EngineProcess(this);
    Board board = new Board();

    public Board getBoard() {

        return board;
    }
    public void startEngine() {
        engine.start();
    }
    public void move(String moveString) {
        board.doMove(moveString);
        engine.analyzeCurrentPosition();
    }
    public EngineProcess getEngine() {
        return engine;
    }
}
