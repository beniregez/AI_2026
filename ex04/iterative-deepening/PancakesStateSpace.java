/*
  This file contains the state space definition for the pancake problem.

  These are the only problem-specific (i.e., specific to the
  pancake problem) parts of the code; everything else is generic
  and can be used without change for other search problems.
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class PancakesStateSpace implements StateSpace {
    /*
      We make pancakes states and actions private since the search code
      cannot and should not look into the state.
    */

    private static class PancakeState implements State {
        /*
          A state is represented as an array of integers, representing the
          positions of the pancakes, from top to bottom.
        */
        public ArrayList<Integer> pancakePositions;

        public PancakeState(ArrayList<Integer> pancakePositions) {
            this.pancakePositions = pancakePositions;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof PancakeState)) {
                return false;
            }
            PancakeState other = (PancakeState)o;
            return this.pancakePositions.equals(other.pancakePositions);
        }

        @Override
        public int hashCode() {
            return pancakePositions.hashCode();
        }

        public String toString() {
            String result = "\n";
            int numPancakes = pancakePositions.size();
            int tab = numPancakes;
            for (int i = 0; i < numPancakes; i++) {
              int pancake = pancakePositions.get(i);
              for (int j = 0; j < tab - pancake; j++) {
                  result += " ";
              }
              for (int j = 0; j < pancake; j++) {
                  result += "--";
              }
              result += "\n";
            }
            return result;
        }
    }

    private static class PancakeAction implements Action {
        /*
          Every action is defined by the number of pancakes to be swapped
        */

        public int amount_swapped;

        public PancakeAction(int amount_swapped) {
            this.amount_swapped = amount_swapped;
        }

        public int cost() {
            return 1;
        }

        public String toString() {
            return "Swap the upper " + amount_swapped
                    + " pancakes.";
        }
    }

    /*
      A state space instance must store the initial state, the
      goal state, and it also stores all actions (number of pancakes
      many).
    */
    private int numPancakes;
    private PancakeState initialState;
    private PancakeAction actions[];

    private PancakesStateSpace(int numPancakes,
                              PancakeState initialState) {
        this.numPancakes = numPancakes;
        this.initialState = initialState;
        System.out.println("Instantiating pancake instance...");

        actions = new PancakeAction[numPancakes];
        for (int i = 0; i < numPancakes; i++) {
            actions[i] = new PancakeAction(i+1);
        }
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
        return initialState;
    }

    public boolean isGoal(State s_) {
        PancakeState s = (PancakeState) s_;

        /*
          The (only) goal state of the pancake problem is the one where the
          positions, in index order, are occupied by pancakes of increasing
          size: (1, 2, ..., numPancakes-1, numPancakes)
        */

        for (int pos = 0; pos < numPancakes; pos++) {
            int pancake = s.pancakePositions.get(pos);
            if (pancake != pos+1)
                return false;
        }

        return true;
    }

    public ArrayList<ActionStatePair> succ(State s_) {
        PancakeState s = (PancakeState) s_;

        ArrayList<ActionStatePair> result = new ArrayList<ActionStatePair>();

        for (PancakeAction action : actions) {
            result.add(createSuccessor(s,action));
        }
        return result;
    }

    private ActionStatePair createSuccessor(PancakeState s, PancakeAction action) {
        /*
          Copy all pancakes positions. 
        */
        ArrayList<Integer> newPancakePositions = new ArrayList<Integer>(s.pancakePositions);
        int numPancakes = newPancakePositions.size();
        /*
          The first action.amount_swapped pancakes must be swapped. Since
          the array index starts at 0, we need to swap positions
          0, ..., action.amount_swapped-1
        */
        for (int i = 0; i < action.amount_swapped; i++) {
            int upperIndex = action.amount_swapped - 1 - i;
            int lowerIndex = i;
            newPancakePositions.set(lowerIndex, s.pancakePositions.get(upperIndex));
            newPancakePositions.set(upperIndex, s.pancakePositions.get(lowerIndex));
        }
        PancakeState succ = new PancakeState(newPancakePositions);
        return new ActionStatePair(action,succ);
    }

    public int cost(Action a) {
        return 1;
    }

    /*
      The following method instantiates the state space by reading the
      problem description from a file specified on the command line.
      The pancake state space is a *parameterized* one (i.e., the
      initial state depends on arguments specified by the user of the
      code).
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

        Set<Integer> usedPancakes = new HashSet<Integer>();

        ArrayList<Integer> initialPancakePositions = new ArrayList<Integer>();

        int size = 0;
        while (scanner.hasNext()) {
            int pancake = scanner.nextInt();
            if (pancake < 1)
                Errors.fileError("invalid pancake (below 1): " + pancake);
            if (usedPancakes.contains(pancake))
                Errors.fileError("duplicate pancake " + pancake);
            usedPancakes.add(pancake);
            initialPancakePositions.add(pancake);
            size++;
        }
        scanner.close();

        for (int i = 1; i <= size; i++) {
            if (!usedPancakes.contains(i)) {
                Errors.fileError("Missing pancake " + i);
            }
        }

        PancakeState initialState = new PancakeState(initialPancakePositions);
        return new PancakesStateSpace(size, initialState);
    }
}
