package snl.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Max Schwaab, Katharina Laube
 * @since 27.08.2014
 */
public class Game {

    private Map<Integer, JumpRule> specialPositions = new HashMap<>();

    private List<Player> players = new ArrayList<>();

    private int currentPlayer = 0;

    void addPlayer(final Player player) {
        players.add(player);
    }

    void moveCurrentPlayer(int diceValue) {
        final int lastPosition = getCurrentPlayer().getPosition();
        final String playerName = getCurrentPlayer().getName();

        // roll dice
        int newPosition = lastPosition + diceValue;
        if (newPosition > getFieldSize()) {
        	System.out.println("You have to roll a '" 
        			+ (getFieldSize()-lastPosition) + "' or lower!");
        	return;
        }
        System.out.println(playerName + " has rolled '" +  diceValue+ "'. "
        		+ "This moves him to " + newPosition + ".");
        
        // use snake or ladder
		JumpRule jumpRule = specialPositions.get(newPosition);
		if (jumpRule != null) {
			newPosition = jumpRule.getEndPosition();
			System.out.println("He has found a " + jumpRule.getType()
					+ ". It moves him to " + newPosition + ".");
		}
		
		getCurrentPlayer().setPosition(newPosition);
    }

    void shiftPlayer() {
        if(this.currentPlayer == players.size() -1 ) {
            this.currentPlayer = 0;
        } else {
            this.currentPlayer ++;
        }
    }

    boolean isOver() {
        return getCurrentPlayer().getPosition() == getFieldSize();
    }

    Player getCurrentPlayer() {
        return players.get(currentPlayer);
    }

    int getDiceValue() {
        return new Random().nextInt(6) + 1;
    }

    int getFieldSize() {
        return 100;
    }

	int getPlayersPosition(Player searchedPlayer) {
		for (Player player : players) {
			if (player.equals(searchedPlayer)) {
				return player.getPosition();
			}
		}
		throw new IllegalArgumentException("Player [" + searchedPlayer + "] is unknown!");
	}

	void addJumpRule(JumpRule jumpRule) {
		specialPositions.put(jumpRule.getStartPosition(), jumpRule);
		
		Type type = jumpRule.getType();
		switch (type) {
		case LADDER:
			if (jumpRule.getStartPosition() >= getFieldSize()) {
				throw new IllegalArgumentException(type + " must not start before field's end!");
			}
			if (jumpRule.getStartPosition() < 1) {
				throw new IllegalArgumentException(type + " must start at least on field's start!");
			}
			break;
		case SNAKE:
			if (jumpRule.getStartPosition() < 2) {
				throw new IllegalArgumentException(type + " must start after field's start!");
			}
			if (jumpRule.getStartPosition() >= getFieldSize()) {
				throw new IllegalArgumentException(type + " must start before field's end!");
			}
			if (jumpRule.getEndPosition() < 1) {
				throw new IllegalArgumentException(type + " must not end before field's start!");
			}
			break;
		}
	}
}
