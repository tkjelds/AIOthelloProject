public class MiniMaxAlphaBeta implements IOthelloAI {

    public int numberOfValueCalls = 0;

    @Override
    public Position decideMove(GameState s) {
       return miniMaxSearch(s, 10);
    }

    public Position miniMaxSearch(GameState gs, int maxDepth){
        numberOfValueCalls = 0;
        Position move;
        var search = MaxValue(gs, Integer.MIN_VALUE,Integer.MAX_VALUE, maxDepth,0);
        move = search.move;
        System.out.println(numberOfValueCalls);
        return move;
    }

    private Pair MaxValue(GameState gs,int alpha, int beta, int maxDepth, int currentDepth) {
        numberOfValueCalls++;
        if (gs.isFinished()) return new Pair(Utility(gs), null);
        if (currentDepth >= maxDepth ) return new Pair(Utility(gs), null);
        var legalMoves = gs.legalMoves();
        int value = Integer.MIN_VALUE;
        Position move = null;
        for (Position a : legalMoves) {
            var tempGS = new GameState(gs.getBoard(), gs.getPlayerInTurn());
            tempGS.insertToken(a);      
            var v2a2 = MinValue(tempGS, alpha , beta , maxDepth , (currentDepth + 1));
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
        numberOfValueCalls++;
        if (gs.isFinished()) return new Pair(Utility(gs), null);
        if (currentDepth >= maxDepth ) return new Pair(Utility(gs), null);
        var legalMoves = gs.legalMoves();
        int value = Integer.MAX_VALUE;
        Position move = null;
        for (Position a : legalMoves) {
            var tempGS = new GameState(gs.getBoard(), gs.getPlayerInTurn());
            tempGS.insertToken(a);
            var v2a2 = MaxValue(tempGS, alpha , beta , maxDepth , (currentDepth + 1));
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
    
}

class Pair{
    Position move;
    int value;
    public Pair(int _value, Position _move){
        this.move = _move;
        this.value = _value;
    }
}