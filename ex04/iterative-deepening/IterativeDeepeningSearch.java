import java.util.ArrayList;

public class IterativeDeepeningSearch extends SearchAlgorithmBase{

    public IterativeDeepeningSearch(String[] args) {
        super(args);
    }

    @Override
    protected ArrayList<Action> run() {
        long totalNodes = 0;
        for (long i = 0; true; i++){
            ListNumPair solution = depthLimitedSearch(this.stateSpace.init(), i);
            totalNodes += solution.num;
            System.out.println(String.format("Iteration: %d | Generated nodes: %d | total: %d", i, solution.num, totalNodes));
            if (solution.list != null) {
                return solution.list;
            }
        }
    }

    private ListNumPair depthLimitedSearch(State s, long depthLim) {
        long numCalls = 1;

        if (this.stateSpace.isGoal(s)) {
            return new ListNumPair(new ArrayList<Action>(), numCalls);
        }
        if (depthLim > 0) {
            ArrayList<ActionStatePair> actionStatePairs = this.stateSpace.succ(s);
            for (ActionStatePair actionStatePair : actionStatePairs) {
                ListNumPair solution = depthLimitedSearch(actionStatePair.state, depthLim - 1);
                numCalls += solution.num;

                if (solution.list != null) {
                    solution.list.add(0, actionStatePair.action);
                    return new ListNumPair(solution.list, numCalls);
                }
            }
        }
        return new ListNumPair(null, numCalls);
    }

    public class ListNumPair {
        public ArrayList<Action> list;
        public long num;

        public ListNumPair(ArrayList<Action> list, long num) {
            this.list = list;
            this.num = num;
        }
    }

    public static void main(String[] args) {
        IterativeDeepeningSearch ids = new IterativeDeepeningSearch(args);
        ids.runSearchAlgorithm();
    }
}