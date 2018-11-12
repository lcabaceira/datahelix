package com.scottlogic.deg.generator.generation;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;

import java.util.*;
import java.util.stream.Collectors;

class AutomationUtils {

    private static final char printableChar = ' ';

    // Returns a string of printable characters based on the supplied automaton.
    // This method ignores transitions that lead back to the same node so recursive states will only ever produce a
    // single character. This means for infinite automatons the resulting string isn't the longest possible (as the
    // longest example would have an infinite length) but is based on the longest path from the start state to
    // the "furthest" end state.
    static String getLongestExample(Automaton automaton) {

        Transition[] solution = {};

        // The start node always has one transition to start
        Transition startTransition = automaton.getInitialState().getSortedTransitions(true).get(0);
        Transition[] startTransitionPath = { startTransition };

        Stack<Transition[]> transitionsToVisit = new Stack<>();
        transitionsToVisit.add(startTransitionPath);

        while (!transitionsToVisit.empty()) {
            Transition[] currentTransitionPath = transitionsToVisit.pop();
            Transition currentTransition = currentTransitionPath[currentTransitionPath.length - 1];

            State dest = currentTransition.getDest();

            if (dest.isAccept() && currentTransitionPath.length > solution.length) {
                solution = currentTransitionPath;
            }

            List<Transition> nextTransitions = dest.getTransitions()
                .stream()
                .filter(x -> x.getDest() != dest)
                .collect(Collectors.toList());

            for (Transition nextTransition : nextTransitions) {
                Transition[] nextTransitionPath = Arrays.copyOf(
                    currentTransitionPath,
                    currentTransitionPath.length + 1);
                nextTransitionPath[nextTransitionPath.length - 1] = nextTransition;

                transitionsToVisit.push(nextTransitionPath);
            }
        }

        StringBuilder sb = new StringBuilder();

        for (Transition transition : solution) {
            sb.append((char) Math.max(printableChar, transition.getMin()));
        }

        return sb.toString();
    }

    // Taken from Automaton but updated to return printable characters
    static String getShortestExample(Automaton a) {
        State initialState = a.getInitialState();

        Map<State, String> stateToOutput = new HashMap<>();
        LinkedList<State> queue = new LinkedList<>();

        stateToOutput.put(initialState, "");
        queue.add(initialState);

        String currentBest = null;

        while (!queue.isEmpty()) {
            State currentState = queue.removeFirst();
            String currentOutput = stateToOutput.get(currentState);

            if (currentState.isAccept()) {

                if (currentBest == null
                    || currentOutput.length() < currentBest.length()
                    || (currentOutput.length() == currentBest.length() && currentOutput.compareTo(currentBest) < 0)) {
                    currentBest = currentOutput;
                }

            } else {
                for (Transition transition : currentState.getTransitions()) {
                    String nextOutput = stateToOutput.get(transition.getDest());
                    String nextOutputCalculated = currentOutput + (char)Math.max(transition.getMin(), printableChar);

                    if (nextOutput == null
                        || (nextOutput.length() == nextOutputCalculated.length() && nextOutputCalculated.compareTo(nextOutput) < 0)) {
                        if (nextOutput == null) {
                            queue.addLast(transition.getDest());
                        }
                        stateToOutput.put(transition.getDest(), nextOutputCalculated);
                    }
                }
            }
        }

        return currentBest;
    }
}