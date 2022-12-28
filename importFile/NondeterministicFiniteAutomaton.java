package importFile;

import java.util.*;

import importFile.CNFRule;


public class NondeterministicFiniteAutomaton {
    Set<State> states;
    Set<Transition> transitions;
    State startState;
    Set<State> acceptState;

    public NondeterministicFiniteAutomaton(Set<State> states, Set<Transition> transitions, State startState, Set<State> acceptState) {
        this.states = states;
        this.transitions = transitions;
        this.startState = startState;
        this.acceptState = acceptState;
    }

    public Set<State> getStates() {
        return states;
    }

    public Set<State> getAcceptState() {
        return acceptState;
    }

    public Set<Transition> getTransitions() {
        return transitions;
    }
//    @Override
//    public String toString() {
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("Start state: " + startState + "\n");
//        sb.append("Final states: " + acceptState+ "\n");
//        sb.append("States: " + states + "\n");
//        sb.append("Transitions:\n");
//        for (Transition transition : transitions) {
//            sb.append("  " + transition + "\n");
//        }
//        return sb.toString();
//    }

//    public boolean accepts(String input) {
//        // Implementation of the acceptance algorithm goes here
//    }



    public static NondeterministicFiniteAutomaton cnfToNFA(CNFRule cnfRule) {
        


        State startState = new State(cnfRule.nonterminal, false);
        State finalState = new State(cnfRule.rightHandSide[0],false);


        Set<State> states = Set.of(startState, finalState);


        if (cnfRule.rightHandSide.length == 1 && !cnfRule.rightHandSide[0].equals(cnfRule.nonterminal)) {

            Transition transition = new Transition(startState, finalState, cnfRule.rightHandSide[0]);
            Set<Transition> transitions = Set.of(transition);

            return new NondeterministicFiniteAutomaton(states, transitions, startState, Set.of(finalState));
        }


        if (cnfRule.rightHandSide.length == 1 && cnfRule.rightHandSide[0].equals(cnfRule.nonterminal)) {
            Transition transition = new Transition(startState, finalState, cnfRule.nonterminal);
            Set<Transition> transitions = Set.of(transition);
            return new NondeterministicFiniteAutomaton(states, transitions, startState, Set.of(finalState));
        }


//        if (cnfRule.rightHandSide.length > 1) {
//            System.out.println("gg");
////            Set<State> states = new HashSet<>();
//
//
//            State newStartState = new State();
//            State newFinalState = new State();
//
//            states.add(newStartState);
//            states.add(newFinalState);
//
//            Transition transition1 = new Transition(startState, newStartState, "");
//            Transition transition2 = new Transition(newFinalState, finalState, "");
//
//
//            Set<Transition> transitions = Set.of(transition1, transition2);
//
//            for (String symbol : cnfRule.rightHandSide) {
//                NondeterministicFiniteAutomaton symbolNFA = cnfToNFA(new CNFRule(cnfRule.nonterminal, new String[]{symbol}));
//                states.addAll(symbolNFA.states);
//                transitions.addAll(symbolNFA.transitions);
//                transitions.add(new Transition(newStartState, symbolNFA.startState, ""));
//                transitions.add(new Transition(symbolNFA.acceptState.iterator().next(), newFinalState, ""));
//            }
//
//            return new NondeterministicFiniteAutomaton(states, transitions, startState, Set.of(finalState));
//        }



        return null;

    }
    public static NondeterministicFiniteAutomaton cnfRulesToNFA(List<CNFRule> cnfRules) {
        Set<State> states = new HashSet<>();
        Set<Transition> transitions = new HashSet<>();
        State startState = new State();
        Set<State> finalStates = new HashSet<>();


        // Iterate over the list of CNF rules
        for (CNFRule cnfRule : cnfRules) {
            // Convert the CNF rule to an NFA
            NondeterministicFiniteAutomaton nfa = cnfToNFA(cnfRule);

            // Add the states and transitions of the NFA to the overall set of states and transitions
            states.addAll(nfa.states);
            transitions.addAll(nfa.transitions);

            // If this is the first iteration, set the start state and final states of the overall NFA
            // to the start state and final states of the current NFA
            if (cnfRule == cnfRules.get(0)) {
                startState = nfa.startState;
                finalStates = nfa.acceptState;
            } else {
                // If this is not the first iteration, connect the final states of the overall NFA
                // to the start state of the current NFA with an epsilon transition
                for (State finalState : finalStates) {
//                    Transition transition = new Transition(finalState, nfa.startState, Transition.EPSILON);
//                    transitions.add(transition);
                }
                // Set the final states of the overall NFA to the final states of the current NFA
                finalStates = nfa.acceptState;
            }
        }

        // Create and return the NFA
        return new NondeterministicFiniteAutomaton(states, transitions, startState, finalStates);
    }


    static class State {
        String name;
        boolean isAccepting;

        public State() {

        }


        public State(String name,boolean isAccepting) {
            this.name = name;
            this.isAccepting = isAccepting;
        }

        public String getName() {
            return name;
        }

        public boolean isAccepting() {
            return isAccepting;
        }
    }

    static class Transition {
        public static final String EPSILON ="EPSILON" ;

        State fromState;
        State toState;
        String symbol;

        //from state : from which state
        //to state: to which state
        //symbol : execute input

        public Transition(State fromState, State toState, String symbol) {
            this.fromState = fromState;
            this.toState = toState;
            this.symbol = symbol;
        }

        public State getFromState() {
            return fromState;
        }
        public State getToState() {
            return toState;
        }

        public String getSymbol() {
            return symbol;
        }
    }
}


