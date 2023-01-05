package dev.cabotmc.chessboard;

import com.github.bhlangonijr.chesslib.*;
import com.github.bhlangonijr.chesslib.move.Move;
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
    public void move(Move m) {
        board.doMove(m);
        engine.analyzeCurrentPosition();

    }
    public EngineProcess getEngine()     {
        return engine;
    }
}
