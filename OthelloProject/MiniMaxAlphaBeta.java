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
        if (gs.isFinished()) return new Pair(Utility(gs), null);
        if (currentDepth >= maxDepth ) return new Pair(Utility(gs), null);
        int value = Integer.MIN_VALUE;
        var legalMoves = gs.legalMoves();
        if(legalMoves.isEmpty()) return new Pair(Utility(gs), null);
        Position move = legalMoves.get(0);
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
        if (gs.isFinished()) return new Pair(Utility(gs), null);
        if (currentDepth >= maxDepth ) return new Pair(Utility(gs), null);
        int value = Integer.MAX_VALUE;
        var legalMoves = gs.legalMoves();
        if(legalMoves.isEmpty()) return new Pair(Utility(gs), null);
        Position move = legalMoves.get(0);
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

    private Boolean isNextToCorner(Position pos, int bs){
        ArrayList<Position> nextToCornerPositions = new ArrayList<>();  

        nextToCornerPositions.add(new Position(1, 1));
        nextToCornerPositions.add(new Position(0, 1));
        nextToCornerPositions.add(new Position(1, 0));
        nextToCornerPositions.add(new Position((bs-1), 0));
        nextToCornerPositions.add(new Position((bs-1), (bs+1)));
        nextToCornerPositions.add(new Position((bs), 1));
        nextToCornerPositions.add(new Position(0, (bs-1)));
        nextToCornerPositions.add(new Position(1, (bs-1)));
        nextToCornerPositions.add(new Position(1, bs));
        nextToCornerPositions.add(new Position((bs-1), (bs-1)));
        nextToCornerPositions.add(new Position((bs-1), bs));
        nextToCornerPositions.add(new Position(bs, (bs-1)));

        return nextToCornerPositions.contains(pos); 
    }
    
    private Boolean isBorder(Position pos, int bs){
        int row = pos.row;
        int col = pos.col;

        if (row == 0 || row == bs || col == 0 || col == bs) return true;
        return false;
    }
    
    private int CalculatePositionValue(Position pos, int bs){
        if (isCorner(pos , bs)) return 4;
        if (isNextToCorner(pos, bs)) return -4;
        if (isBorder(pos, bs)) return 2;
        return 1;
    }



    private int Utility(GameState gs){
        int bs = gs.getBoard().length;
        var util = 0;
        for(int col = 0; col < bs; col++){
            for (int row = 0; row < bs; row++) {
                if(gs.getBoard()[col][row] == 1) util += CalculatePositionValue(new Position(col, row), bs-1);
            }
        }
        return util;
    }



    private GameState Result(GameState gs, Position a){
        var tempGS = new GameState(gs.getBoard(), gs.getPlayerInTurn());
        tempGS.insertToken(a);
        return tempGS;
    }
   
}

class Pair{
    Position move;
    int value;
    public Pair(int _value, Position _move){
        this.move = _move;
        this.value = _value;
    }
}