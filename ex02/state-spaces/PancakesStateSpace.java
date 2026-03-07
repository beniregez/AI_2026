import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class PancakesStateSpace implements StateSpace{

    private static class PancakeState implements State{
        public ArrayList<Integer> pancakes;
        public int size;

        public PancakeState(ArrayList<Integer> pancakes) {
            this.pancakes = pancakes;
            this.size = pancakes.size();
        }

        public boolean equals(PancakeState other) {
            return pancakes.equals(other.pancakes);
        }

        public String toString() {
            String result = "";
            for (int i = 0; i < this.size; i++) {
                result += pancakes.get(i);
                result += " ";
            }
            return result;
        }
    }

    private static class PancakeAction implements Action {
        
        public int numberToFlip;

        public PancakeAction(int numberToFlip) {
            this.numberToFlip = numberToFlip;
        }

        @Override
        public int cost() {
            return 1;
        }

        public String toString() {
            return "flip-" + numberToFlip;
        }
    }


    private int numberOfPancakes;
    private PancakeState initState;
    private PancakeState goalstate;

    public PancakesStateSpace(int numberOfPancakes, PancakesStateSpace.PancakeState initState,
            PancakesStateSpace.PancakeState goalstate) {
        this.numberOfPancakes = numberOfPancakes;
        this.initState = initState;
        this.goalstate = goalstate;
    }

    @Override
    public State init() {
        return this.initState;
    }

    @Override
    public boolean isGoal(State s_) {
        PancakeState s = (PancakeState) s_;
        return s.equals(this.goalstate);
    }

    @Override
    public ArrayList<ActionStatePair> succ(State s_) {
        /* Get all possible successor states from a given state */

        PancakeState s = (PancakeState) s_;
        ArrayList<ActionStatePair> result = new ArrayList<ActionStatePair>();
        for (int i = 0; i <= this.numberOfPancakes; i++) {
            result.add(this.createSuccessor(s, i));
        }
        return result;
    }
    
    private ActionStatePair createSuccessor(PancakeState s, int numberToFlip) {
        /* Helper method to get next successor state and action by flipping the given number of pancakes on top */
        ArrayList<Integer> newPancakes = new ArrayList<>();
        for (int i = 0; i < s.size; i++) {
            if (i < numberToFlip) {
                newPancakes.add(s.pancakes.get(numberToFlip - i - 1));
            } else {
                newPancakes.add(s.pancakes.get(i));
            }
        }
        PancakeState successor = new PancakeState(newPancakes);
        PancakeAction action = new PancakeAction(numberToFlip);
        return new ActionStatePair(action, successor);
    }

    @Override
    public int cost(Action a) {
        return a.cost();
    }

    public static PancakeState createGoalState(int numberOfPancakes) {
        ArrayList<Integer> pancakes = new ArrayList<>();
        for (int i = 1; i <= numberOfPancakes; i++) {
            pancakes.add(i);
        }
        PancakeState goalState = new PancakeState(pancakes);
        return goalState;
    }


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

        String firstline = "";
        if (scanner.hasNextLine()) {
            firstline = scanner.nextLine() + " ";
            System.out.println("Content of first line: " + firstline);
        } else {
            Errors.fileError("input file has no lines: " + filename);
        }

        scanner.close();

        ArrayList<Integer> pancakes = new ArrayList<>();
        String nextPancake = "";
        for (int i = 0; i < firstline.length(); i++) {
            if (firstline.charAt(i) == ' ') {
                pancakes.add(Integer.parseInt(nextPancake));
                nextPancake = "";
            } else {
                nextPancake += firstline.charAt(i);
            }
        }

        int numberOfPancakes = pancakes.size();
        PancakeState goalState = createGoalState(numberOfPancakes);

        // Check if input is valid
        ArrayList<Integer> copyPancakes = new ArrayList<>(pancakes);
        Collections.sort(copyPancakes);
        System.out.println(copyPancakes);
        for (int i = 0; i < copyPancakes.size(); i++) {
            if (copyPancakes.get(i) != goalState.pancakes.get(i)) {
                Errors.inputError("Invalid pancake in input file detected: " + copyPancakes.get(i));
            }
            // System.out.println(copyPancakes.get(i) + " " + goalState.pancakes.get(i));
        }

        PancakeState initState = new PancakeState(pancakes);        
        return new PancakesStateSpace(numberOfPancakes, initState, goalState);
    }

    // Method for debug purpose
    public static void main(String[] args) {
        ArrayList<Integer> pancakes = new ArrayList<>();
        pancakes.add(1);
        pancakes.add(2);
        pancakes.add(3);
        pancakes.add(4);
        pancakes.add(5);
        PancakeState pancakeState = new PancakeState(pancakes);
        System.out.println(pancakeState.toString());

        PancakesStateSpace stateSpace = new PancakesStateSpace(5, pancakeState, pancakeState);

        ArrayList<ActionStatePair> successors = stateSpace.succ(pancakeState);

        for (int i = 1; i < successors.size(); i++) {
            System.out.println(successors.get(i).state.toString() + " | i = " + i);
        }
    }
}
