package abalonBoardGame;

import java.awt.Point;

import boardGame.BoardDataStructure;
import enums.Player;

/**
 * the class that is in charge of holding the information on the abalon board
 *
 */
public class AbalonBoardDataStructure extends
        BoardDataStructure<AbalonSoldier[][]> {

    private int _size;
    private int _numOfColsInFirstRow;
    private int _numOfWhites;
    private int _numOfBlacks;

    /**
     * constructor
     *
     * @param numOfRows           - number of rows
     * @param numOfColsInFirstRow - number of columns in first row
     */
    public AbalonBoardDataStructure(int numOfRows, int numOfColsInFirstRow) {
        _size = numOfRows;
        _numOfColsInFirstRow = numOfColsInFirstRow;
        _dataSet = new AbalonSoldier[_size][_size];
    }

    /**
     * constructor that copies another data structure
     *
     * @param dataStructure
     */
    public AbalonBoardDataStructure(AbalonBoardDataStructure dataStructure) {
        _size = dataStructure._size;
        _numOfBlacks = dataStructure._numOfBlacks;
        _numOfWhites = dataStructure._numOfWhites;
        _numOfColsInFirstRow = dataStructure._numOfColsInFirstRow;
        _dataSet = dataStructure.cloneDataSet();
    }

    /**
     * clones the object that holds the board's data
     *
     * @return an exact copy of the object
     */
    private AbalonSoldier[][] cloneDataSet() {
        AbalonSoldier[][] mat = new AbalonSoldier[_size][_size];
        for (int i = 0; i < _size; i++) {
            for (int j = 0; j < _size; j++) {
                // if soldier exists in that spot
                if (_dataSet[i][j] != null) {
                    mat[i][j] = new AbalonSoldier(_dataSet[i][j].getType());
                } else {
                    mat[i][j] = null;
                }
            }
        }
        return mat;
    }

    /**
     * returns the content of the square of a certain position assuming the
     * coordinate is in the bounds of the board
     */
    public Player getSquareContent(Point pos) {
        if (_dataSet[pos.y][pos.x] != null) {
            return _dataSet[pos.y][pos.x].getType();
        }
        return null;
    }

    /**
     * sets the content of a square with a another assuming the coordinate is in
     * the bounds of the board
     */
    public void setSquareContent(Point pos, Player content) {
        if (content == null) {
            _dataSet[pos.y][pos.x] = null;
        } else {
            _dataSet[pos.y][pos.x] = new AbalonSoldier(content);
        }
    }

    /**
     * swaps the contents between two positions
     *
     * @param pos1
     * @param pos2
     */
    public void swapSquaresContents(Point pos1, Point pos2) {
        Player c1 = null, c2 = null;
        if (_dataSet[pos1.y][pos1.x] != null) {
            c1 = _dataSet[pos1.y][pos1.x].getType();
        }
        if (_dataSet[pos2.y][pos2.x] != null) {
            c2 = _dataSet[pos2.y][pos2.x].getType();
        }
        setSquareContent(pos1, c2);
        setSquareContent(pos2, c1);
    }

    /**
     * initializes the first state of the board
     */
    public void initBoard() {
        // initialize first board state
        _numOfWhites = _numOfBlacks = 0;
        int colsInRow = _numOfColsInFirstRow;
        int rows = _size / 2;
        Point pos;
        Player soldier;
        // all the rows except the middle row
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colsInRow; j++) {
                pos = new Point(j, i);
                soldier = soldierInPos(pos);
                addSoldierToPos(pos, soldier);
                pos = new Point(j, _size - 1 - i);
                soldier = soldierInPos(pos);
                addSoldierToPos(pos, soldier);
            }
            colsInRow++;
        }
        // middle row
        for (int i = 0; i < colsInRow; i++) {
            pos = new Point(i, _size / 2);
            soldier = soldierInPos(pos);
            addSoldierToPos(pos, soldier);
        }
    }

    /**
     * returns the soldier that is supposed to be on a given position according
     * to the initial state
     *
     * @param pos
     * @return
     */
    private Player soldierInPos(Point pos) {
        // if first or second row
        if (pos.y == 0 || pos.y == 1 || (pos.y == 2 && pos.x > 1 && pos.x < 5)) {
            return Player.BLACK;
        }
        // if last and second last row
        if (pos.y == _size - 1 || pos.y == _size - 2
                || (pos.y == _size - 3 && pos.x > 1 && pos.x < 5)) {
            return Player.WHITE;
        }
        return null;
    }

    /**
     * adds a given soldier to a given position
     *
     * @param pos
     * @param player
     */
    private void addSoldierToPos(Point pos, Player player) {
        if (player != null) {
            _dataSet[pos.y][pos.x] = new AbalonSoldier(player);
            incNumOfSoldiersOfPlayer(player);
        } else {
            _dataSet[pos.y][pos.x] = null;
        }
    }

    /**
     * increases the number of soldiers according to the given player
     *
     * @param p - given player
     */
    private void incNumOfSoldiersOfPlayer(Player p) {
        if (p == Player.WHITE) {
            _numOfWhites++;
        }
        if (p == Player.BLACK) {
            _numOfBlacks++;
        }
    }

    /**
     * decreases the number of soldiers according to the given player
     *
     * @param p - given player
     */
    public void decNumOfSoldiersOfPlayer(Player p) {
        if (p == Player.WHITE) {
            _numOfWhites--;
        }
        if (p == Player.BLACK) {
            _numOfBlacks--;
        }
    }

    /**
     * @param p
     * @return the number of soldier of a given player
     */
    public int numOfSoldiers(Player p) {
        if (p == Player.WHITE) {
            return _numOfWhites;
        }
        if (p == Player.BLACK) {
            return _numOfBlacks;
        }
        return -1;
    }

    /**
     * @return a matrix that describes the board for graphical use
     */
    public Player[][] boardDescription() {
        Player[][] boardLayout = new Player[_size][_size];
        int colsInRow = _numOfColsInFirstRow;
        int rows = _size / 2;
        Point pos;
        // all the rows except the middle row
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < colsInRow; j++) {
                pos = new Point(j, i);
                boardLayout[i][j] = getSquareContent(pos);
                pos = new Point(j, _size - 1 - i);
                boardLayout[_size - 1 - i][j] = getSquareContent(pos);
            }
            colsInRow++;
        }
        // middle row
        for (int i = 0; i < colsInRow; i++) {
            pos = new Point(i, _size / 2);
            boardLayout[_size / 2][i] = getSquareContent(pos);
        }
        return boardLayout;
    }

    /**
     * returns a string that describes the board
     */
    public String toString() {
        String st = "";
        int rowCount = _numOfColsInFirstRow;
        for (int i = 0; i < _size / 2; i++)// top half without middle row
        {
            st += generateSpaces(_size / 2 - i) + "[";
            for (int j = 0; j < rowCount; j++) {
                st += soldierToString(i, j);
            }
            rowCount++;
            st += "]\n";
        }
        for (int i = _size / 2; i < _size; i++)// bottom half
        {
            st += generateSpaces(i - _size / 2) + "[";
            for (int j = 0; j < rowCount; j++) {
                st += soldierToString(i, j);
            }
            rowCount--;
            st += "]\n";
        }
        return st;
    }

    /**
     * @param st
     * @param i  - row
     * @param j  - column
     * @return a string that describes a soldier in a given position
     */
    private String soldierToString(int i, int j) {
        if (_dataSet[i][j] != null) {
            if (_dataSet[i][j].getType() == Player.BLACK)
                return " B ";
            if (_dataSet[i][j].getType() == Player.WHITE)
                return " W ";
        }
        return " , ";
    }

    /**
     * @param num
     * @return a string with spaces according to a given number
     */
    private String generateSpaces(int num) {
        String st = "";
        for (int i = 1; i <= num * 1.5; i++) {
            st += " ";
        }
        return st;
    }

    // getters and setters
    public int get_numOfWhites() {
        return _numOfWhites;
    }

    public void set_numOfWhites(int _numOfWhites) {
        this._numOfWhites = _numOfWhites;
    }

    public int get_numOfBlacks() {
        return _numOfBlacks;
    }

    public void set_numOfBlacks(int _numOfBlacks) {
        this._numOfBlacks = _numOfBlacks;
    }

    public int get_size() {
        return _size;
    }

    public int get_numOfColsInFirstRow() {
        return _numOfColsInFirstRow;
    }
}
