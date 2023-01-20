package importFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.List;

import importFile.FiniteAutomat.State;
import importFile.FiniteAutomat.Transition;

import java.util.*;


public class JavaFileImport {
    public static void main(String[] args) throws IOException {
        // Read the text file into a list of strings, one string per line
        List<String> textLines = Files.readAllLines(Paths.get("regularGrammar.txt"));
        List<String> ruleStringList = new ArrayList<>();

        //Symbol for EPSILON
        final char EPSILON = '\u03B5';

        System.out.println("-----I AM A LINE-----");
        System.out.println("-----Regular Grammar-----");

        //Turn the the content from 'regularGrammar.txt' content into
        //Each grammar rule per line
        for (String line : textLines) {
            String ruleString = line; //.replace("->", "::=");
            System.out.println(ruleString);
            ruleStringList.add(ruleString);
        }

        System.out.println("-----I AM A LINE-----");
        System.out.println("-----NFA-----");
        
        //Turn the content from 'regularGrammar.txt' 
        //into a RegularGrammar class object
        List<RegularGrammar> grammarRules = RegularGrammar.parseGrammar(ruleStringList);

        ////////////////////////////////////////////////////////////////////////////////////////////////
        //Convert RegularGrammar into NFA
        FiniteAutomat finite_automat = new FiniteAutomat(grammarRules);

        //Print each state of NFA
        for (State state : finite_automat.states) {
            List<Transition> toStateName = state.getTransition();
            
            if (toStateName.size() > 0){ //Making sure it only print State with transition
                if (toStateName.get(0).toState.transitions.size() > 0){
                    for (int i = 0; i < toStateName.size(); i++){
                        System.out.println("StartState: "+ toStateName.get(i).toState.getName() +", toState: "+ toStateName.get(i).toState.transitions.get(0).toState.getName() +", Input: "+ toStateName.get(i).toState.transitions.get(0).getSymbol() +", Accept?: "+ toStateName.get(i).toState.transitions.get(0).toState.getAcceptState());
                    }
                }
            }
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////

        System.out.println("-----I AM A LINE-----");
        System.out.println("-----NFA without Epsilon-----");

        //For the purpose of calling the function 
        //from the 'NondeterministicFiniteAutomaton' class
        FiniteAutomat fa = new FiniteAutomat();

        //Keep track of which state/rule being visited
        List<LinkedList<Character>> visitedStates = new ArrayList<LinkedList<Character>>(); 

        //Templist for storing the list of states of NFA without Epsilon
        List<State> StatesWithoutEpsilon = new ArrayList<State>();
        
        //Templist that stored the states of NFA
        List<State> oldStateList = finite_automat.states;

        //Convert states with EPSILON into without EPSILON, then add into the templist
        StatesWithoutEpsilon = fa.renewStates(finite_automat, oldStateList);
        
        //Replace the current list of states of the NFA, which make it become NFA without EPSILON
        finite_automat.setStateList(StatesWithoutEpsilon);

        for (State state : finite_automat.states) { 
            List<Transition> toStateName = state.getTransition();

            for (Transition transition : toStateName) {
                System.out.println("StartState: "+ state.getName() +", toState: "+ transition.toState.getName() +", Input: "+ transition.getSymbol() +", Status: "+ state.getAcceptState());
            }
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////

        System.out.println("-----I AM A LINE-----");
        System.out.println("-----DFA-----");
        
        //Templist for storing the list of states of NFA without Epsilon
        List<State> dfaStates = new ArrayList<State>();
        
        //Templist that stored the states of NFA
        List<State> oldStateListwoEPSILON = finite_automat.states;

        dfaStates = fa.convertToDFA(finite_automat, oldStateListwoEPSILON);

        //Replace the current list of states of the NFA, which make it become NFA without EPSILON
        finite_automat.setStateList(dfaStates);

        for (State state : finite_automat.states) { 
            //System.out.println("StartState: "+ state.getName() + ", Status: "+ state.getAcceptState() + ", Their transistion: " + state.transitions.size());
            List<Transition> toStateName = state.getTransition();

            for (Transition transition : toStateName) {
                System.out.println("StartState: "+ state.getName() +", toState: "+ transition.toState.getName() +", Input: "+ transition.getSymbol() +", Status: "+ state.getAcceptState());
            }
        }
        

        System.out.println("-----I AM A LINE-----");
        System.out.println("-----MIN_DFA-----");

        List<State> minDfaStates = new ArrayList<State>();
        minDfaStates = fa.minimizeState(dfaStates);

        finite_automat.setStateList(minDfaStates);

        for (State state : finite_automat.states) { 
            //System.out.println("StartState: "+ state.getName() + ", Status: "+ state.getAcceptState() + ", Their transistion: " + state.transitions.size());
            List<Transition> toStateName = state.getTransition();

            for (Transition transition : toStateName) {
                System.out.println("StartState: "+ state.getName() +", toState: "+ transition.toState.getName() +", Input: "+ transition.getSymbol() +", Status: "+ state.getAcceptState());
            }
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////
        System.out.println("-----I AM A LINE-----");
        System.out.println(fa.testInput("1", finite_automat));
    }
    
}