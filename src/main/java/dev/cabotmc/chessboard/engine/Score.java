package dev.cabotmc.chessboard.engine;

import com.github.bhlangonijr.chesslib.Side;

import java.util.Scanner;

/**
 * An engine's evaluation of a position, from the view of White
 */
public class Score {
    public enum ScoreType {
        CENTIPAWNS,
        MATE

    }
    ScoreType type;
    int score;
    public Score(String scoreString, Side currentSide) {
        var s = new Scanner(scoreString);
        var type = s.next();
        this.type = type.equals("mate") ? ScoreType.MATE : ScoreType.CENTIPAWNS;
        score = s.nextInt();
        if (currentSide == Side.BLACK) {
            score = score * -1;
        }
    }
    public ScoreType getType() {
        return type;
    }

    /**
     * If getType() returns MATE, this is the number of moves until mate
     * Else, this is the advantage in centi pawns
     * If this number is negative, the advantage goes to black. Otherwise, white is winning.
     * @return
     */
    public int getScore() {
        return score;
    }
    public String toString() {
        if (type == ScoreType.CENTIPAWNS) {
            if (score > 0) {
                // white is winning
                return "White is winning +" + (score / 100f);
            } else if (score < 0) {
                // black is winning
                return "Black is winning " + (score / 100f);
            } else {
                return "The position is equal +0.00";
            }
        } else {
            // mate
            if (score < 0) {
                // black has mate in x
                return "Black has mate in " + (score * -1);
            } else {
                return "White has mate in " + score;
            }
        }
    }
}
