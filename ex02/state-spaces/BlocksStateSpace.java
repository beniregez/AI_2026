/*
  This file contains the state space definition for blocks world.

  These are the only problem-specific (i.e., specific to the blocks
  world) parts of the code; everything else is generic and can be used
  without change for other search problems.
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class BlocksStateSpace implements StateSpace {
    /*
      We make Blocks states and actions private since the search code
      cannot and should not look into the state. Since they are only
      accessed from this class, we can make them plain old datatypes
      without violating encapsulation.
    */

    private static class Tower extends ArrayList<Integer> {
        // Helper class to have some nicer type names.
    }

    private static class BlocksState implements State {
        /*
          State of a blocks world instance. We represent each tower as
          an ArrayList of ints, and the set of towers as a sorted
          ArrayList of towers. (We perform sorting so that each state
          has a canonical representation: we cannot have two states
          that differ only because of the order of towers.)

          Please note that this is not the most efficient
          representation -- vectors of vectors are more expensive than
          necessary, and sorting is also expensive. However, for
          didactical purposes, this is a good trade-off between
          simplicity and efficiency.
        */

        public ArrayList<Tower> towers;

        public BlocksState(ArrayList<Tower> towers) {
            this.towers = new ArrayList<Tower>(towers);

            // Sort the towers by first element. (Since all elements are
            // distinct, this is enough for a canonical ordering.)
            Collections.sort(this.towers, new Comparator<Tower>() {
                    public int compare(Tower lhs, Tower rhs) {
                        return lhs.get(0) - rhs.get(0);
                    }
                });
        }

        /*
          In the blocks world, the user can specify any state to be
          the goal state in the input file, so we need an
          equality-testing operator to test if we have reached the
          goal. An equality-testing method is not always necessary.
        */

        public boolean equals(BlocksState other) {
            return towers.equals(other.towers);
        }

        public String toString() {
            String result = "[";
            for (int i = 0; i < towers.size(); i++) {
                if (i != 0)
                    result += ", ";
                result += "[";
                Tower tower = towers.get(i);
                for (int j = 0; j < tower.size(); j++) {
                    if (j != 0)
                        result += ", ";
                    result += tower.get(j);
                }
                result += "]";
            }
            result += "]";
            return result;
        }
    }

    private static class BlocksAction implements Action {
        /*
          Action that represents moving block "movedBlock" onto block
          "moveTarget". Target can be -1 to represent moving to the table.

          We just use actions as "plain old datatypes". All
          interesting functionality related to actions lives in the
          BlocksStateSpace class.
        */

        public int movedBlock;
        public int moveTarget;

        public BlocksAction(int movedBlock, int moveTarget) {
            this.movedBlock = movedBlock;
            this.moveTarget = moveTarget;
        }

        public int cost() {
            return 1;
        }

        public String toString() {
            if (moveTarget == -1)
                return "totable(" + movedBlock + ")";
            else
                return "move(" + movedBlock + "," + moveTarget + ")";
        }
    }

    private int numberOfBlocks;

    private BlocksState initState;
    private BlocksState goalState;

    private BlocksStateSpace(int numberOfBlocks, BlocksState initState,
                             BlocksState goalState) {
        this.numberOfBlocks = numberOfBlocks;
        this.initState = initState;
        this.goalState = goalState;
        System.out.println("Instantiating problem instance with "
                           + numberOfBlocks + " blocks...");
    }

    /*
      The following four methods define the interface of state spaces
      used by the search code.

      We use the method names "init", "isGoal", "succ" and "cost" to
      stay as close as possible to the names in the lecture slides.
      Without this constraint, it would be better to use more
      self-explanatory names like "getSuccessorStates" instead of
      "succ".

      All methods are const because the state space itself never
      changes.

    */

    public State init() {
        // Just return the initial state that we stored.
        return initState;
    }

    public boolean isGoal(State s_) {
        BlocksState s = (BlocksState) s_;

        // Compare against the goal state that we stored.
        return s.equals(goalState);
    }

    public ArrayList<ActionStatePair> succ(State s_) {
        BlocksState s = (BlocksState) s_;

        ArrayList<ActionStatePair> result = new ArrayList<ActionStatePair>();

        int numTowers = s.towers.size();

        /*
          We can only move blocks that are at the top (in our
          representation: back) of a tower, and we can only move them to
          the top of the tower. Hence it's easiest to iterate over the
          towers between which we move to generate all successors.
        */

        for (int srcTower = 0; srcTower < numTowers; srcTower++) {
            // destTower == -1 means move to the table here
            for (int destTower = -1; destTower < numTowers; destTower++) {
                if (srcTower != destTower) {
                    result.add(createSuccessor(s, srcTower, destTower));
                }
            }
        }

        return result;
    }

    private ActionStatePair createSuccessor(BlocksState s,
                                            int srcTowerNo, int destTowerNo) {
        /*
          Helper function that generates a single (action,
          successorState) pair for moving from one tower to another.
          destTower == -1 means "move to the table"
        */

        /*
          We first copy the state to form the new successor state. It
          is a shallow copy except for the two towers that are
          modified, which must also be copied.
        */
        ArrayList<Tower> newTowers = new ArrayList<Tower>(s.towers);

        Tower srcTower = (Tower) s.towers.get(srcTowerNo).clone();
        newTowers.set(srcTowerNo, srcTower);

        Tower destTower;
        if (destTowerNo == -1) {
            // Form a new tower and make it the destTower.
            destTower = new Tower();
            newTowers.add(destTower);
        } else {
            destTower = (Tower) s.towers.get(destTowerNo).clone();
            newTowers.set(destTowerNo, destTower);
        }

        // Compute moved block and target block and build the action.
        int movedBlock = srcTower.get(srcTower.size() - 1);
        int moveTarget = (destTowerNo == -1) ?
            -1 : destTower.get(destTower.size() - 1);

        // Modify the two changed towers.
        srcTower.remove(srcTower.size() - 1);
        destTower.add(movedBlock);

        // Make sure we have no empty towers in the state.
        if (srcTower.isEmpty())
            newTowers.remove(srcTowerNo);

        BlocksAction action = new BlocksAction(movedBlock, moveTarget);
        BlocksState succState = new BlocksState(newTowers);

        return new ActionStatePair(action, succState);
    }

    public int cost(Action a) {
        return a.cost();
    }

    /*
      The following method instantiates the state space by reading the
      problem description from a file specified on the command line.
      The blocks world state space is a *parameterized* one (i.e., the
      number of blocks as well as the initial and goal state depend on
      arguments specified by the user of the code).

      Syntax of input:
      - first line: total number of blocks
      - second line: blocks in initial state (separated with -1)
      - third line: blocks in goal state (separated with -1)

    */
    public static StateSpace buildFromCmdline(ArrayList<String> args) {
        if (args.size() != 1) {
            Errors.usageError("need one input file argument");
        }

        String filename = args.get(0);
        System.out.println("Reading input from file " + filename + "...");
        Scanner scanner;
        try {
            scanner = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            Errors.fileError("input file not found: " + filename);
            scanner = new Scanner(""); // unreachable; silences compiler
        }

        int numBlocks = scanner.nextInt();

        if (numBlocks < 0)
            Errors.fileError("invalid number of blocks");

        BlocksState init = scanState(numBlocks, scanner);
        BlocksState goal = scanState(numBlocks, scanner);

        if (scanner.hasNext())
            Errors.fileError("expected end of file");
        scanner.close();

        return new BlocksStateSpace(numBlocks, init, goal);
    }

    private static BlocksState scanState(int numBlocks, Scanner scanner) {
        boolean[] usedBlocks = new boolean[numBlocks];
        int blocksRemaining = numBlocks;

        ArrayList<Tower> towers = new ArrayList<Tower>();

        while (blocksRemaining != 0) {
            Tower tower = new Tower();
            while (true) {
                int blockNo = scanner.nextInt();
                if (blockNo == -1)
                    break;
                if (blockNo < 0 || blockNo >= numBlocks)
                    Errors.fileError("invalid block");
                if (usedBlocks[blockNo])
                    Errors.fileError("duplicate block");
                usedBlocks[blockNo] = true;
                blocksRemaining--;
                tower.add(blockNo);
            }
            if (tower.isEmpty())
                Errors.fileError("empty tower");
            towers.add(tower);
        }

        return new BlocksState(towers);
    }
}
