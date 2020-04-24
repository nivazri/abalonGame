package db;

import java.sql.*;
import java.util.ArrayList;

public class AbalonController {

    private Connection db;

    public AbalonController() throws SQLException {
        DatabaseConnection dbConn = new DatabaseConnection();
        this.db = dbConn.getConnection();
        this.createTable();
    }

    private void createTable() throws SQLException {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS games"
                + "  (winner           VARCHAR(10),"
                + "   play_time            BIGINT,"
                + "   board_state         text)";

        Statement stmt = db.createStatement();
        stmt.execute(sqlCreate);
    }

    public ArrayList<AbalonGame> getAll() throws SQLException {
        ArrayList<AbalonGame> games = new ArrayList<AbalonGame>();
        Statement stmt = db.createStatement();
        ResultSet rs = stmt.executeQuery("select * from games");
        while (rs.next()) {
            String winner = rs.getString(1);
            long timestamp = rs.getLong(2);
            String boardState = rs.getString(3);
            games.add(new AbalonGame(winner, boardState, timestamp));
        }
        return games;
    }

    public void addNewGame(AbalonGame game) throws SQLException {
        Statement stmt = db.createStatement();
        stmt.executeUpdate(String.format("insert into games values (%s, %d, %s)",
                game.getWinner(), game.getPlayTime(), game.getBoardState()));
    }

}
