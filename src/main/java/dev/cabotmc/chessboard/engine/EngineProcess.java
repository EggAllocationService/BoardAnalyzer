package dev.cabotmc.chessboard.engine;

import dev.cabotmc.chessboard.RichChessGame;
import dev.cabotmc.chessboard.engine.messages.InfoEngineMessage;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EngineProcess extends Thread {
    static String enginePath = null;
    public static void setEnginePath(String in) {
        enginePath = in;
    }
    Process engine;
    RichChessGame owner;
    BufferedReader stdout;
    BufferedWriter stdin;
    String engineName = "Unknown";
    ConcurrentLinkedQueue<String> messageQueue = new ConcurrentLinkedQueue<>();
    ArrayList<EngineListener> listeners = new ArrayList<>();
    int depth = 18;
    boolean hasSentOpeningMessage = false;
    static volatile int threadCount = 0;
    boolean shouldShutdown = false;
    public EngineProcess(RichChessGame owner) {
        super("Chess Analysis Thread " + threadCount);
        threadCount++;
        setDaemon(true);
        this.owner = owner;
    }
    @Override
    public void run() {
        try {
            engine = Runtime.getRuntime().exec(enginePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stdout = engine.inputReader();
        var outStream = engine.getInputStream();
        stdin = engine.outputWriter();

        while(true) {
            try {
                if (stdout.ready()) {
                    // things to read from engine;
                    var line = stdout.readLine();
                    //System.out.println(" engine -> " + line);
                    processEngineResponse(line);
                    if (!hasSentOpeningMessage) {
                        queueCommand("uci");
                        hasSentOpeningMessage = true;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (messageQueue.size() > 0) {
                try {
                    var msg = messageQueue.poll();
                    //System.out.println(" engine <- " + msg);
                    stdin.write(msg + "\r\n");
                    stdin.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            // wait
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (shouldShutdown) {
                engine.destroy();
                return;
            }
        }
    }
    public void queueCommand(String command) {
        messageQueue.add(command);
    }
    void processEngineResponse(String line) {
        var s = new Scanner(line);
        if (!s.hasNext()) return;
        var verb = s.next();
        if (verb.equals("id")) {
            processId(s.nextLine());
        } else if (verb.equals("info")) {
            processInfo(s.nextLine());
        }
    }
    public void shutdown() {
        shouldShutdown = true;
    }
    void processId(String id) {
        engineName = id.trim();
    }
    static final char[] allowedPatterns = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    void processInfo(String infoLine) {
        var s = new Scanner(infoLine);
        var next = InfoEngineMessage.builder();
        while (s.hasNext()) {
            var currentVerb = s.next();
            if (currentVerb.equals("depth")) {
                var curD = s.nextInt();
                next.depth(curD);
                next.doneEval(curD == depth);
            } else if (currentVerb.equals("score")) {
                var scoreString = s.next() + " " + s.next();
                next.currentScore(scoreString, owner.getBoard().getSideToMove());
            } else if (currentVerb.equals("pv")) {
                next.line(s.nextLine().trim());
            }
        }
        var made = next.build();
        for (var l : listeners) {
            l.accept(made);
        }
    }

    public void analyzeCurrentPosition() {
        queueCommand("stop");
        queueCommand("position fen " + owner.getBoard().getFen());
        queueCommand("go depth " + depth);
    }
    public void addListener(EngineListener l) {
        listeners.add(l);
    }
}
