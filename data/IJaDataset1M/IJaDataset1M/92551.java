package safreader.nodes;

import java.util.ArrayList;
import java.util.Iterator;
import saf.Logger;

public class Move implements Node {

    private ArrayList<String> moves = new ArrayList<String>();

    private static final ArrayList<String> validmoves = new ArrayList<String>() {

        {
            add("jump");
            add("crouch");
            add("stand");
            add("run_towards");
            add("run_away");
            add("walk_towards");
            add("walk_away");
        }
    };

    public Move() {
    }

    public void addMove(String move) {
        moves.add(move);
    }

    public ArrayList<String> getMoves() {
        return moves;
    }

    @Override
    public boolean consistencyCheck() {
        Iterator<String> itr = moves.iterator();
        boolean valid = true;
        while (itr.hasNext()) {
            if (!isMoveValid(itr.next())) {
                valid = false;
            }
        }
        return valid;
    }

    private boolean isMoveValid(String move) {
        if (validmoves.contains(move)) {
            return true;
        } else {
            Logger.staticLog("Invalid move \"" + move + "\".");
            return false;
        }
    }
}
