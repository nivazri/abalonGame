package enums;

import java.awt.Point;

/**
 * the enum of direction contains all the possible moves of a soldier on the board
 * @author omer
 *
 */
public enum Direction {
	
	RIGHT(new Point(1,0)),
	LEFT(new Point(-1,0)),
	RIGHTUP(null),
	RIGHTDOWN(null),
	LEFTUP(null),
	LEFTDOWN(null);
	
	private Point _move;
	
	Direction(Point p)
	{
		_move=p;
	}
	
	public Point getDirectionOffset()
	{
		return _move;
	}
	
	/**
	 * sets the offset of a direction by the given postion and rows of the board
	 * @param currPos- current position
	 * @param numOfRows- number of rows in the board
	 */
	public void setMovementOffsetByCurrentLocation(Point currPos,int numOfRows)
	{
		Direction d=this;
		switch(d)
		{
		case RIGHTUP:
			RIGHTUP._move=new Point(currPos.y/(numOfRows/2+1), -1);
			break;
		case RIGHTDOWN:
			RIGHTDOWN._move=new Point( (currPos.y/(numOfRows/2))^1, 1);
			break;
		case LEFTUP:
			LEFTUP._move=new Point(-((currPos.y/(numOfRows/2+1))^1), -1);
			break;
		case LEFTDOWN:
			LEFTDOWN._move=new Point(-((currPos.y/(numOfRows/2))),1);
			break;
		case RIGHT:
		case LEFT:
			break;
		}
	}
	
	/**
	 * 
	 * @param source
	 * @return the destination point of a direction with the source point
	 */
	public Point getDestPoint(Point source)
	{
		return new Point(source.x+this._move.x,source.y+this._move.y);
	}
	
	/**
	 * 
	 * @param source
	 * @param dest
	 * @param numOfRows
	 * @return the direction between to coordinates
	 */
	public static Direction getDirectionFromPoints(Point source, Point dest,int numOfRows)
	{
		setDirectionsOffsetsByCurrPos(source, numOfRows);
		int diffX=dest.x-source.x, diffY=dest.y-source.y;
		for(Direction d: Direction.values())
		{
			if(d._move.x==diffX && d._move.y==diffY)
				return d;
		}
		return null;
	}
	
	/**
	 * sets all the offsets of the directions by a current position
	 * @param currPos
	 * @param numOfRows
	 */
	public static void setDirectionsOffsetsByCurrPos(Point currPos, int numOfRows)
	{
		for (Direction d : Direction.values()) {
			d.setMovementOffsetByCurrentLocation(currPos, numOfRows);
		}
	}
	
	/**
	 * 
	 * @param source
	 * @param offset
	 * @return a new point that started from the source and was added an offset
	 */
	public static Point addOffsetToSource(Point source,Point offset)
	{
		return new Point(source.x+offset.x,source.y+offset.y);
	}
}
