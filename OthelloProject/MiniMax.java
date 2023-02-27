public class MiniMax implements IOthelloAI {


    @Override
    public Position decideMove(GameState s) {
       return miniMaxSearch(s, 5);
    }

    public Position miniMaxSearch(GameState gs, int maxDepth){
        System.out.println("New Turn");
        Position move;
        var search = MaxValue(gs,maxDepth,0);
        move = search.move;
        if (move == null) return new Position(-1, -1);
        return move;
    }

    private Pair MaxValue(GameState gs, int maxDepth, int currentDepth) {
        if (gs.isFinished()) return new Pair(Utility(gs), null);
        if (currentDepth >= maxDepth ) return new Pair(Utility(gs), null);
        var legalMoves = gs.legalMoves();
        int value = Integer.MIN_VALUE;
        Position move = null;
        for (Position a : legalMoves) {
            var tempGS = new GameState(gs.getBoard(), gs.getPlayerInTurn());
            tempGS.insertToken(a);      
            var v2a2 = MinValue(tempGS, maxDepth, (currentDepth + 1));
            if (v2a2.value > value) {
                value = v2a2.value;
                move = a;
            }
        }
        System.out.println(move);
        return new Pair(value, move);
    }

    private Pair MinValue(GameState gs, int maxDepth, int currentDepth) {
        if (gs.isFinished()) return new Pair(Utility(gs), null);
        if (currentDepth >= maxDepth ) return new Pair(Utility(gs), null);
        var legalMoves = gs.legalMoves();
        int value = Integer.MAX_VALUE;
        Position move = null;
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
