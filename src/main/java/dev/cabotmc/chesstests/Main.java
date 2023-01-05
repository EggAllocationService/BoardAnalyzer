package dev.cabotmc.chesstests;

import dev.cabotmc.chessboard.RichChessGame;
import dev.cabotmc.chessboard.engine.EngineProcess;
import dev.cabotmc.chessboard.engine.messages.InfoEngineMessage;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        EngineProcess.setEnginePath("C:\\Users\\kyle\\Downloads\\stockfish_15.1_win_x64_avx2\\stockfish-windows-2022-x86-64-avx2.exe");
        var b = new RichChessGame();
        b.startEngine();
        var s = new Scanner(System.in);
        b.getEngine().addListener(l -> {
            var msg = (InfoEngineMessage) l;
            if (!msg.isFinalEvaluation()) return;
            System.out.println("depth " + msg.getCurrentDepth() + "/18 " + msg.getCurrentScore().toString());
        });
        while (true) {
            System.out.println(b.getBoard().toStringFromWhiteViewPoint());
            var command = s.nextLine();
            if (command.equals("exit")) {
                break;
            } else if (command.startsWith("eng"))  {
                b.getEngine().queueCommand(command.replace("eng ", "").strip());
            } else {
                b.move(command);
            }
        }
    }
}