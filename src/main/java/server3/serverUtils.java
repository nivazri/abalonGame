package server3;

import java.awt.Point;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import abalonBoardGame.AbalonBoardDataStructure;
import enums.Player;

public class serverUtils {
	public static Player parsePlayer(String playerStr) {
		if(playerStr.equals("WHITE")) {
			return Player.WHITE;
		} else {
			return Player.BLACK;
		}
	}
	
	public static void setBoard(AbalonBoardDataStructure ds, JSONObject jo) {
        JSONArray boardState = (JSONArray) jo.get("boardState");	 

        for (int i = 0 ; i < boardState.size(); i++) {
            JSONObject arrObj = (JSONObject) boardState.get(i);
			int row = ((Long) arrObj.get("row")).intValue();
			int col = ((Long) arrObj.get("column")).intValue();
			Player solider = serverUtils.parsePlayer((String) arrObj.get("solider"));
			ds.setSquareContent(new Point(col, row), solider);
        }
	}
	
	public static Point parseCurrentPos(JSONObject jo) {
        JSONObject currentPosJo = (JSONObject) jo.get("currentPos");
        int currentPosRow = ((Long) currentPosJo.get("row")).intValue();
        int currentPosCol = ((Long) currentPosJo.get("column")).intValue();
        return new Point(currentPosCol, currentPosRow);
	}
	
}
