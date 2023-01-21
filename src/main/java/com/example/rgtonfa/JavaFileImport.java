package com.example.rgtonfa;

import com.example.rgtonfa.FiniteAutomat.State;
import com.example.rgtonfa.FiniteAutomat.Transition;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class JavaFileImport {
    public static void main(String[] args) throws IOException {
        // Read the text file into a list of strings, one string per line
        List<String> textLines = Files.readAllLines(Paths.get("regularGrammar.txt"));
        List<String> ruleStringList = new ArrayList<>();

        //Symbol for EPSILON
        final char EPSILON = '\u03B5';



        //Turn the the content from 'regularGrammar.txt' content into
        //Each grammar rule per line
        for (String line : textLines) {
            String ruleString = line; //.replace("->", "::=");
            System.out.println(ruleString);
            ruleStringList.add(ruleString);
        }

        System.out.println("-----I AM A LINE-----");

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

        System.out.println("-----I AM A LINE-----");

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
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //NFA RESULT IN TABLE
    //FOR PRINTING CORESPONDING RESULT IN THE TABLE
    public static String findNFAResult(FiniteAutomat NFA, String nonTerminal, char symbolInput){

        String result = "X";

        final char EPSILON = '\u03B5';

        for (State state : NFA.states) { //Checking state by state from the NFA

            List<Transition> toStateName = state.getTransition();

            if (toStateName.size() > 0){

                if (toStateName.get(0).toState.transitions.size() > 0){

                    for (int i = 0; i < toStateName.size(); i++){

                        //Compare if starting state same as the current state row
                        if(toStateName.get(i).toState.getName().equals(nonTerminal)){

                            //Compare if input symbol same as the current state column (Exclude E)
                            if((toStateName.get(i).toState.transitions.get(0).getSymbol() == symbolInput) && (toStateName.get(i).toState.transitions.get(0).toState.getName().charAt(0) != EPSILON)){

                                result = String.valueOf(toStateName.get(i).toState.transitions.get(0).toState.getName());

                            }
                        }
                    }
                }
            }
        }
        return result;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //NFA WITHOUT EPSILON IN TABLE
    //FOR PRINTING CORESPONDING RESULT IN THE TABLE
    public static List<String> findNFAwithoutEpsilonResult(FiniteAutomat NFAwithoutEpsilon, String nonTerminal, char symbolInput){

        //List for storing result
        List<String> resultList = new ArrayList<>();

        String result = "X"; //Represent no Result

        final char EPSILON = '\u03B5';

        for (State state : NFAwithoutEpsilon.states) { //Checking state by state from the NFA without Epsilon

            List<Transition> toStateName = state.getTransition();

            if (toStateName.size() > 0){

                for (int i = 0; i < toStateName.size(); i++){

                    //Compare if starting state same as the current state row
                    if (nonTerminal.length()>1){
                        if(state.getName().equals(String.valueOf(nonTerminal.charAt(nonTerminal.length()-1)))){

                            //Compare if input symbol same as the current state column (Exclude E)
                            if((toStateName.get(i).getSymbol() == symbolInput) && (toStateName.get(i).toState.getName().charAt(0) != EPSILON)){

                                resultList.add(String.valueOf(toStateName.get(i).toState.getName()));

                            }
                        }
                    } else {
                        if(state.getName().equals(nonTerminal)){

                            //Compare if input symbol same as the current state column (Exclude E)
                            if((toStateName.get(i).getSymbol() == symbolInput) && (toStateName.get(i).toState.getName().charAt(0) != EPSILON)){

                                resultList.add(String.valueOf(toStateName.get(i).toState.getName()));

                            }
                        }
                    }
                }
            }
        }

        //If no result, directly add "X" into the list as No Result
        if (resultList.size() < 1){
            resultList.add(result);
        }

        return resultList;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
}