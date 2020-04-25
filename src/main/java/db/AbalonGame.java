package db;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class AbalonGame {

    private String winner;

    private String boardState;

    private long playTime;

    private boolean isAI;

    public AbalonGame(String winner, String boardState, long playTime, boolean isAI) {
        this.winner = winner;
        this.boardState = boardState;
        this.playTime = playTime;
        this.isAI = isAI;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getBoardState() {
        return boardState;
    }

    public void setBoardState(String boardState) {
        this.boardState = boardState;
    }

    public long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    public JSONObject toJSON() {

        JSONObject jo = new JSONObject();
        jo.put("play_time", playTime);
        jo.put("board_state", boardState);
        jo.put("winner", winner);
        jo.put("is_ai", isAI);

        return jo;
    }

    public boolean isAI() {
        return isAI;
    }

    public void setAI(boolean AI) {
        isAI = AI;
    }
}
