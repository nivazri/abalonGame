package server3;

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
    	// CRUD get post put delete
    	
    	// JSON - get and post, accepts json body, and returns json as a result
    	
    	// Abalon with requests
    	
    	// serving static assets, entire folder
    	
    	// url of web app ui: "/"
    	
    	// rest of api will have "/api" prefix
    	// CORS?
    	// enable CORS in spark java
    	// https://gist.github.com/saeidzebardast/e375b7d17be3e0f4dddf
    	
    	// api for abalon game
    	
    	// accept json: boardState, currentPlayerTurn
    	// post "/api/" accpet boardState, currentPlayerTurn, position
    		//, returns list of possible moves (list of positions) 
    	
    	// post "/api/" boardState, sourcePosition, destPosition, 
    		// returns a new board state after this move (boardState)
    	
    	// post "/api/" accept boardState, currentPlayerTurn,
    		// returns a new board state that selectes the move with the ai
    	
    	
    	// Static files
    	// for external directory outside of the project
    	// access on http://localhost:12345/test.html
//        staticFiles.externalLocation("C:\\Users\\niazriel\\Desktop\\UI"); 
        
        // for directory on the project
        staticFiles.location("/public");
        
    	port(12345);
    	
    	get("/", (q, a) -> IOUtils.toString(Spark.class.getResourceAsStream("/public/index.html")));
    	
		path("/api", () -> {
			get("/health", (req, res) -> {
	    		return "Service is up and running";
	    	});
			
	        post("/possibleMoves", (req, res) -> {
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
	        
	        post("/winner", (req, res) -> {
	            Object obj = new JSONParser().parse(req.body());
	            JSONObject jo = (JSONObject) obj;
	            
	            AbalonBoardDataStructure ds = new AbalonBoardDataStructure(9, 5);
	            serverUtils.setBoard(ds, jo);
	            AbalonBoard ab = new AbalonBoard(ds);
	            
	            Player winner = ab.getWinner();
	            return winner;
	        });
	        
	        post("/nextPlayerMove", (req, res) -> {
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
	        
	        post("/ai", (req, res) -> {
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