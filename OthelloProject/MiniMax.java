public class MiniMax implements IOthelloAI {


    @Override
    public Position decideMove(GameState s) {
       return miniMaxSearch(s, 4);
    }

    public Position miniMaxSearch(GameState gs, int maxDepth){
        Position move;
        var search = MaxValue(gs,maxDepth,0);
        move = search.move;
        return move;
    }

    private Pair MaxValue(GameState gs, int maxDepth, int currentDepth) {
        if (gs.isFinished()) return new Pair(Utility(gs), null);
        if (currentDepth >= maxDepth ) return new Pair(Utility(gs), null);
        int value = Integer.MIN_VALUE;
        var legalMoves = gs.legalMoves();
        if(legalMoves.isEmpty()) return new Pair(Utility(gs), null);
        Position move = legalMoves.get(0);
        for (Position a : legalMoves) {
            var tempGS = new GameState(gs.getBoard(), gs.getPlayerInTurn());
            tempGS.insertToken(a);      
            var v2a2 = MinValue(tempGS, maxDepth, (currentDepth + 1));
            if (v2a2.value > value) {
                value = v2a2.value;
                move = a;
            }
        }
        return new Pair(value, move);
    }

    private Pair MinValue(GameState gs, int maxDepth, int currentDepth) {
        if (gs.isFinished()) return new Pair(Utility(gs), null);
        if (currentDepth >= maxDepth ) return new Pair(Utility(gs), null);
        int value = Integer.MAX_VALUE;
        var legalMoves = gs.legalMoves();
        if(legalMoves.isEmpty()) return new Pair(Utility(gs), null);
        Position move = legalMoves.get(0);
        for (Position a : legalMoves) {
            var tempGS = new GameState(gs.getBoard(), gs.getPlayerInTurn());
            tempGS.insertToken(a);
            var v2a2 = MaxValue(tempGS,maxDepth, (currentDepth + 1));
            if (v2a2.value < value) {
                value = v2a2.value;
                move = a;
            }
        }
        return new Pair(value, move);
    }

    private int Utility(GameState gs){
        return gs.countTokens()[1];
    }
    
}
