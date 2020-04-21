package server;

import static spark.Spark.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*; 

import abalonBoardGame.AbalonBoard;
import abalonBoardGame.AbalonBoardDataStructure;
import abalonBoardGame.AbalonBoardGame;
import abalonBoardGame.AbalonSoldier;
import boardAI.GameBoardAI;
import boardGame.Board;
import enums.Player;
import exceptions.AbalonException;
import spark.Spark;
import spark.utils.IOUtils;

import java.awt.Point;
import java.util.Collection;
import java.util.Vector; 

public class Main {
    public static void main(String[] args) {
		port(12345);
		
        staticFiles.location("/public");
    	get("/", (q, a) -> IOUtils.toString(Spark.class.getResourceAsStream("/public/index.html")));
    	
		path("/api", () -> {
			get("/health", (req, res) -> {
	    		return "Service is up and running";
	    	});
			
	        post("/abalongame/moves/all", (req, res) -> {
	            Object obj = new JSONParser().parse(req.body());
	            JSONObject jo = (JSONObject) obj;
	            AbalonBoardDataStructure ds = new AbalonBoardDataStructure(9, 5);
	           
	            Point currentPos = serverUtils.parseCurrentPos(jo);
	            Player currentTurn = serverUtils.parsePlayer((String) jo.get("currentTurn"));
	            serverUtils.setBoard(ds, jo);
	            
	            AbalonBoard ab = new AbalonBoard(ds);
	            Vector<Point> points = ab.unconfirmedPossibleMoves(currentPos);
	            	            
	            JSONArray ja = new JSONArray();
	            for (Point point : points) {
			        try {
			            ab.validateMove(currentPos, point, currentTurn);

			            JSONObject possibleMovesJo = new JSONObject();
			            possibleMovesJo.put("column", point.x);
			            possibleMovesJo.put("row", point.y);
			            ja.add(possibleMovesJo);
		            } catch (Exception e) {
		            	System.out.println(e.getMessage());
		            }
	            }
	            
	        	res.type("application/json");
	        	res.status(200);
	        	return ja.toString();
	        });
	        
	        post("/abalongame/rules/winner", (req, res) -> {
	            Object obj = new JSONParser().parse(req.body());
	            JSONObject jo = (JSONObject) obj;
	            
	            AbalonBoardDataStructure ds = new AbalonBoardDataStructure(9, 5);
	            serverUtils.setBoard(ds, jo);
	            AbalonBoard ab = new AbalonBoard(ds);
	            
	            Player winner = ab.getWinner();
	            
	            if (winner != null) {
	            	return winner.name();
	            }
	            
	            return "NONE";
	        });
	        
	        post("/abalongame/moves/execute", (req, res) -> {
	            Object obj = new JSONParser().parse(req.body()); 	            
	            JSONObject jo = (JSONObject) obj; 
	            AbalonBoardDataStructure ds = new AbalonBoardDataStructure(9, 5);
	            serverUtils.setBoard(ds, jo);
	            AbalonBoard ab = new AbalonBoard(ds);
	            
	            JSONObject sourceJo = (JSONObject) jo.get("sourcePosition");
	            int sourceRow = ((Long) sourceJo.get("row")).intValue();
	            int sourceCol = ((Long) sourceJo.get("column")).intValue();
	            Point source = new Point(sourceCol, sourceRow);
	            
	            JSONObject destJo = (JSONObject) jo.get("destPosition");
	            int destRow = ((Long) destJo.get("row")).intValue();
	            int destCol = ((Long) destJo.get("column")).intValue();
	            Point dest = new Point(destCol, destRow);
	            
	            Player currentTurn = serverUtils.parsePlayer((String) jo.get("currentTurn"));

	            // this is code of move method from game class
	    		try {
	    			ab.validateMove(source, dest, currentTurn);
	    			ab.commitMove(source, dest);
	    		} catch (AbalonException e) {
	    			// in case the move was illegal
	    			System.out.println(e.getMessage());
	    		}
	    		
	        	res.type("application/json");
	    		return serverUtils.getBoard(ab);
	        });
	        
	        post("/abalongame/moves/ai", (req, res) -> {
	            Object obj = new JSONParser().parse(req.body()); 	            
	            JSONObject jo = (JSONObject) obj; 
	            AbalonBoardDataStructure ds = new AbalonBoardDataStructure(9, 5);
	            serverUtils.setBoard(ds, jo);
	            AbalonBoard ab = new AbalonBoard(ds);
	            Player currentTurn = serverUtils.parsePlayer((String) jo.get("currentTurn"));

	        	GameBoardAI<Board<AbalonBoardDataStructure, AbalonSoldier[][]>> _ai=new GameBoardAI<Board<AbalonBoardDataStructure, AbalonSoldier[][]>>();
				_ai.setLevel(3);
				Collection<Board<AbalonBoardDataStructure, AbalonSoldier[][]>> nextStates= ab.getNextStates(currentTurn);
				AbalonBoard aiMove=(AbalonBoard) _ai.findBestMove(nextStates, currentTurn);
				
	        	res.type("application/json");
	    		return serverUtils.getBoard(aiMove);
	    	});
    	});
    }
}