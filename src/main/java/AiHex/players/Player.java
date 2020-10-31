package AiHex.players;

import java.util.ArrayList;

import AiHex.hexBoards.Board;
import AiHex.gameMechanics.Move;

public interface Player {

	public static final int CLICK_PLAYER = 3;
	public static final int CLICK_AI = 6;
	public static final String CLICK_DEFAULT_ARGS = "n/a";
	public static final String CLICK_AI_ARGS = "n/a";


	public static final String[] playerList = {"Human Player","AI Player"};
	public static final int[] playerIndex = {  CLICK_PLAYER, CLICK_AI };

	public static final String[] argsList = { CLICK_DEFAULT_ARGS, CLICK_AI_ARGS};
	public Move getMove();

	public ArrayList<Board> getAuxBoards();
}
