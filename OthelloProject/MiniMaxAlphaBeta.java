import java.util.ArrayList;

public class MiniMaxAlphaBeta implements IOthelloAI {

    @Override
    public Position decideMove(GameState s) {
       return MiniMaxSearch(s, 6);
    }

    public Position MiniMaxSearch(GameState gs, int maxDepth){
        Position move;
        var search = MaxValue(gs, Integer.MIN_VALUE,Integer.MAX_VALUE, maxDepth,0);
        move = search.move;
        return move;
    }

    private Pair MaxValue(GameState gs, int alpha, int beta, int maxDepth, int currentDepth) {
        // Early termination clauses. they just return the utility of the gamestate
        if (gs.isFinished()) return new Pair(Utility(gs), null);
        if (currentDepth >= maxDepth ) return new Pair(Utility(gs), null);
        var legalMoves = gs.legalMoves();
        if(legalMoves.isEmpty()) return new Pair(Utility(gs), null);
        
        int value = Integer.MIN_VALUE;
        Position move = null;
        for (Position a : legalMoves) {      
            var v2a2 = MinValue(Result(gs, a), alpha , beta , maxDepth , (currentDepth + 1));
            if (v2a2.value > value) {
                value = v2a2.value;
                move = a;
                alpha = Integer.max(alpha, value);
            }
            if (value >= beta) return new Pair(value, move);
        }
        return new Pair(value, move);
    }

    private Pair MinValue(GameState gs, int alpha, int beta, int maxDepth, int currentDepth) {
        // Early termination clauses. they just return the utility of the gamestate
        if (gs.isFinished()) return new Pair(Utility(gs), null);
        if (currentDepth >= maxDepth ) return new Pair(Utility(gs), null);
        var legalMoves = gs.legalMoves();
        if(legalMoves.isEmpty()) return new Pair(Utility(gs), null);
        
        int value = Integer.MAX_VALUE;
        Position move = null;
        for (Position a : legalMoves) {
            var v2a2 = MaxValue(Result(gs, a), alpha , beta , maxDepth , (currentDepth + 1));
            if (v2a2.value < value) {
                value = v2a2.value;
                move = a;
                beta = Integer.min(beta, value);
            }
            if (value <= alpha) return new Pair(value, move);
        }
        return new Pair(value, move);
    }


    private Boolean isCorner(Position pos, int boardSize){
        int row = pos.row;
        int col = pos.col;

        if(row == 0 && col == 0) return true;

        if(row == boardSize && col == boardSize) return true;

        if(row == 0 && col == boardSize-1) return true;

        if(row == boardSize && col == 0) return true;

        return false;
    }

    /*
     * Bool representing whether or not a position is next to a corner eg.
     *   0 1 2 3 
     * 0 i j 
     * 1 j j
     * 3
     * 
     * Position i would return false, while j would return true.
     * 
     */
    private Boolean isNextToCorner(Position pos, int bs){
        ArrayList<Position> nextToCornerPositions = new ArrayList<>()
        {
            {
                add(new Position(1, 1));
                add(new Position(0, 1));
                add(new Position(1, 0));
                add(new Position(1, 1));
                add(new Position(0, 1));
                add(new Position(1, 0));
                add(new Position((bs-1), 0));
                add(new Position((bs-1), (bs+1)));
                add(new Position((bs), 1));
                add(new Position(0, (bs-1)));
                add(new Position(1, (bs-1)));
                add(new Position(1, bs));
                add(new Position((bs-1), (bs-1)));
                add(new Position((bs-1), bs));
                add(new Position(bs, (bs-1)));
            }
        };  
        return nextToCornerPositions.contains(pos); 
    }
    
    // Bool, representing whether or not a board piece is on the edge
    private Boolean isBorder(Position pos, int bs){
        int row = pos.row;
        int col = pos.col;

        if (row == 0 || row == bs || col == 0 || col == bs) return true;
        return false;
    }
    
    private int CalculatePositionValue(Position pos, int bs){
        if (isCorner(pos , bs)) return 10;
        if (isNextToCorner(pos, bs)) return -4;
        if (isBorder(pos, bs)) return 3;
        return 1;
    }



    private int Utility(GameState gs){
        int bs = gs.getBoard().length;
        var util = 0;
        // Early evaluation, if the game is over and white wins
        if(gs.isFinished() && (gs.countTokens()[1] > gs.countTokens()[0])) return 1000;
        // Early evaluation, if the game is over and black wins
        if(gs.isFinished() && (gs.countTokens()[1] < gs.countTokens()[0])) return -1000;
        // Summing up the total utility of a board
        for(int col = 0; col < bs; col++){
            for (int row = 0; row < bs; row++) {
                if(gs.getBoard()[col][row] == 2) util += CalculatePositionValue(new Position(col, row), bs-1);
            }
        }
        return util;
    }


    // Helper function to create a new gamestate. Naming was done in compliance with Norvig 2021
    private GameState Result(GameState gs, Position a){
        var tempGS = new GameState(gs.getBoard(), gs.getPlayerInTurn());
        tempGS.insertToken(a);
        return tempGS;
    }
   
}
// Class to represent a pair of a Move and the value of the move. The class was inspired by the same notation as in Norvig 2021.
class Pair{
    Position move;
    int value;
    public Pair(int _value, Position _move){
        this.move = _move;
        this.value = _value;
    }
}