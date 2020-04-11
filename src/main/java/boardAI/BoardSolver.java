package boardAI;

import java.util.Collection;

import enums.Player;
import boardGame.Board;

/**
 * an interface that whoever implements can solve a board
 * @author omer
 *
 * @param <T>- a board instance
 */
public interface BoardSolver<T extends Board<?,?>> {
	T findBestMove(Collection<T> currentStates,Player currentTurn);
}
