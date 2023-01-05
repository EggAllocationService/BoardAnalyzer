package dev.cabotmc.chessboard.engine.messages;

import com.github.bhlangonijr.chesslib.Side;
import dev.cabotmc.chessboard.engine.EngineMessage;
import dev.cabotmc.chessboard.engine.Score;

public class InfoEngineMessage implements EngineMessage {
    int depth;
    String bestLine;
    Score evaluatedScore;
    boolean finalEvaluation;
    public int getCurrentDepth() {
        return depth;
    }
    public String getBestLine() {
        return bestLine;
    }
    public Score getCurrentScore() {
        return evaluatedScore;
    }
    public boolean isFinalEvaluation() {
        return finalEvaluation;
    }

    public static Builder builder() {
        return new Builder();
    }
    static public class Builder {
        InfoEngineMessage msg = new InfoEngineMessage();
        public Builder depth(int depth) {
            msg.depth = depth;
            return this;
        }
        public Builder line(String bestLine) {
            msg.bestLine = bestLine;
            return this;
        }
        public Builder currentScore(Score s) {
            msg.evaluatedScore = s;
            return this;
        }
        public Builder currentScore(String scoreString, Side currentSide) {
            msg.evaluatedScore = new Score(scoreString, currentSide);
            return this;
        }
        public Builder doneEval(boolean fi) {
            msg.finalEvaluation = fi;
            return this;
        }
        public InfoEngineMessage build() {
            return msg;
        }
    }
}
