public class MiniMaxAlphaBeta implements IOthelloAI {

    public int numberOfValueCalls = 0;

    @Override
    public Position decideMove(GameState s) {
       return MiniMaxSearch(s, 6);
    }

    public Position MiniMaxSearch(GameState gs, int maxDepth){
        System.out.println("-------- New Turn --------");
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

    private int Utility(GameState gs){
        return gs.countTokens()[0];
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