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

        System.out.println("-----I AM A LINE-----");
        ////////////////////////////////////////////////////////////////////////////////////////////////
        //PRINT A TABLE FOR NFA
        for (int i = 0; i < 4; i++) {
            System.out.print("+");
            for (int j = 0; j < 7 ; j++) {
                System.out.print("-");
            }
        }
        System.out.println("+");
        ///////////////////////////////////////////
        System.out.print("| ");
        System.out.printf("%-5s ", "XXX");
        System.out.print("| ");
        System.out.printf("%-5s ", "0");
        System.out.print("| ");
        System.out.printf("%-5s ", "1");
        System.out.print("| ");
        System.out.printf("%-5s ", EPSILON);
        System.out.println("|");

        for (int i = 0; i < 4; i++) {
            System.out.print("+");
            for (int j = 0; j < 7 ; j++) {
                System.out.print("-");
            }
        }
        System.out.println("+");
        ///////////////////////////////////////////
        System.out.print("| ");
        System.out.printf("%-5s ", "A");
        System.out.print("| ");
        System.out.printf("%-5s ", findNFAResult(finite_automat, 'A', '0'));
        System.out.print("| ");
        System.out.printf("%-5s ", findNFAResult(finite_automat, 'A', '1'));
        System.out.print("| ");
        System.out.printf("%-5s ", findNFAResult(finite_automat, 'A', EPSILON));
        System.out.println("|");
        
        for (int i = 0; i < 4; i++) {
            System.out.print("+");
            for (int j = 0; j < 7 ; j++) {
                System.out.print("-");
            }
        }
        System.out.println("+");
        ///////////////////////////////////////////
        System.out.print("| ");
        System.out.printf("%-5s ", "B");
        System.out.print("| ");
        System.out.printf("%-5s ", findNFAResult(finite_automat, 'B', '0'));
        System.out.print("| ");
        System.out.printf("%-5s ", findNFAResult(finite_automat, 'B', '1'));
        System.out.print("| ");
        System.out.printf("%-5s ", findNFAResult(finite_automat, 'B', EPSILON));
        System.out.println("|");
        
        for (int i = 0; i < 4; i++) {
            System.out.print("+");
            for (int j = 0; j < 7 ; j++) {
                System.out.print("-");
            }
        }
        System.out.println("+");
        ///////////////////////////////////////////
        System.out.print("| ");
        System.out.printf("%-5s ", "C");
        System.out.print("| ");
        System.out.printf("%-5s ", findNFAResult(finite_automat, 'C', '0'));
        System.out.print("| ");
        System.out.printf("%-5s ", findNFAResult(finite_automat, 'C', '1'));
        System.out.print("| ");
        System.out.printf("%-5s ", findNFAResult(finite_automat, 'C', EPSILON));
        System.out.println("|");
        
        for (int i = 0; i < 4; i++) {
            System.out.print("+");
            for (int j = 0; j < 7 ; j++) {
                System.out.print("-");
            }
        }
        System.out.println("+");
        ////////////////////////////////////////////////////////////////////////////////////////////////

        System.out.println("-----I AM A LINE-----");

        //For the purpose of calling the function 
        //from the 'NondeterministicFiniteAutomaton' class
        FiniteAutomat fa = new FiniteAutomat();

        //Keep track of which state/rule being visited
        List<LinkedList<Character>> visitedStates = new ArrayList<LinkedList<Character>>(); 

        //Output of all state and its possible transition
        for (State state : finite_automat.states) {
            for (Transition transitions : state.transitions){
                System.out.println(state.getName() + " to " + transitions.toState.getName());
            }
        }

        //Testing of a function "checkReachFinal()" where it will test 
        //if a state can reach final state
        //NFA.states.get(1) ---IS---> State A
        //then it will test if A can reach final state
        System.out.println(fa.checkReachFinal(finite_automat.states.get(1), finite_automat, visitedStates));

        System.out.println("-----I AM A LINE-----");

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
        
        System.out.println("-----I AM A LINE-----");

        ////////////////////////////////////////////////////////////////////////////////////////////////
        //PRINT A TABLE FOR NFA WITHOUT EPSILON
        for (int i = 0; i < 3; i++) {
            System.out.print("+");
            for (int j = 0; j < 7 ; j++) {
                System.out.print("-");
            }
        }
        System.out.println("+");
        ///////////////////////////////////////////
        System.out.print("| ");
        System.out.printf("%-5s ", "XXX");
        System.out.print("| ");
        System.out.printf("%-5s ", "0");
        System.out.print("| ");
        System.out.printf("%-5s ", "1");
        System.out.println("| ");

        for (int i = 0; i < 3; i++) {
            System.out.print("+");
            for (int j = 0; j < 7 ; j++) {
                System.out.print("-");
            }
        }
        System.out.println("+");
        ///////////////////////////////////////////
        System.out.print("| ");
        System.out.printf("%-5s ", "A");
        System.out.print("| ");
        for (String foundState : findNFAwithoutEpsilonResult(finite_automat, 'A', '0')){
            if (findNFAwithoutEpsilonResult(finite_automat, 'A', '0').size() > 1){
                System.out.printf("%-2s ", foundState, ",");
            } else {
                System.out.printf("%-5s ", foundState);
            }
        }
        System.out.print("| ");
        for (String foundState : findNFAwithoutEpsilonResult(finite_automat, 'A', '1')){
            if (findNFAwithoutEpsilonResult(finite_automat, 'A', '1').size() > 1){
                System.out.printf("%-2s ", foundState, ",");
            } else {
                System.out.printf("%-5s ", foundState);
            }
        }
        System.out.println("| ");
        
        for (int i = 0; i < 3; i++) {
            System.out.print("+");
            for (int j = 0; j < 7 ; j++) {
                System.out.print("-");
            }
        }
        System.out.println("+");
        ///////////////////////////////////////////
        System.out.print("| ");
        System.out.printf("%-5s ", "B");
        System.out.print("| ");
        for (String foundState : findNFAwithoutEpsilonResult(finite_automat, 'B', '0')){
            if (findNFAwithoutEpsilonResult(finite_automat, 'B', '0').size() > 1){
                System.out.printf("%-2s ", foundState);
            } else {
                System.out.printf("%-5s ", foundState);
            }
        }
        System.out.print("| ");
        for (String foundState : findNFAwithoutEpsilonResult(finite_automat, 'B', '1')){
            if (findNFAwithoutEpsilonResult(finite_automat, 'B', '1').size() > 1){
                System.out.printf("%-2s ", foundState);
            } else {
                System.out.printf("%-5s ", foundState);
            }
        }
        System.out.println("| ");
        
        for (int i = 0; i < 3; i++) {
            System.out.print("+");
            for (int j = 0; j < 7 ; j++) {
                System.out.print("-");
            }
        }
        System.out.println("+");
        ///////////////////////////////////////////
        System.out.print("| ");
        System.out.printf("%-5s ", "C");
        System.out.print("| ");
        for (String foundState : findNFAwithoutEpsilonResult(finite_automat, 'C', '0')){
            if (findNFAwithoutEpsilonResult(finite_automat, 'C', '0').size() > 1){
                System.out.printf("%-2s ", foundState);
            } else {
                System.out.printf("%-5s ", foundState);
            }
        }
        System.out.print("| ");
        for (String foundState : findNFAwithoutEpsilonResult(finite_automat, 'C', '1')){
            if (findNFAwithoutEpsilonResult(finite_automat, 'C', '1').size() > 1){
                System.out.printf("%-2s ", foundState);
            } else {
                System.out.printf("%-5s ", foundState);
            }
        }
        System.out.println("| ");
        
        for (int i = 0; i < 3; i++) {
            System.out.print("+");
            for (int j = 0; j < 7 ; j++) {
                System.out.print("-");
            }
        }
        System.out.println("+");
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
    public static String findNFAResult(FiniteAutomat NFA, char nonTerminal, char symbolInput){
        
        String result = "X";

        final char EPSILON = '\u03B5';

        for (State state : NFA.states) { //Checking state by state from the NFA

            List<Transition> toStateName = state.getTransition();

            if (toStateName.size() > 0){

                if (toStateName.get(0).toState.transitions.size() > 0){
                    
                    for (int i = 0; i < toStateName.size(); i++){
                        
                        //Compare if starting state same as the current state row
                        if(toStateName.get(i).toState.getName().charAt(0) == nonTerminal){

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
    public static List<String> findNFAwithoutEpsilonResult(FiniteAutomat NFAwithoutEpsilon, char nonTerminal, char symbolInput){
        
        //List for storing result
        List<String> resultList = new ArrayList<>();

        String result = "X"; //Represent no Result

        final char EPSILON = '\u03B5';

        for (State state : NFAwithoutEpsilon.states) { //Checking state by state from the NFA without Epsilon

            List<Transition> toStateName = state.getTransition();

            if (toStateName.size() > 0){

                for (int i = 0; i < toStateName.size(); i++){
                    
                    //Compare if starting state same as the current state row
                    if(state.getName().charAt(0) == nonTerminal){

                        //Compare if input symbol same as the current state column (Exclude E)
                        if((toStateName.get(i).getSymbol() == symbolInput) && (toStateName.get(i).toState.getName().charAt(0) != EPSILON)){    
                            
                            resultList.add(String.valueOf(toStateName.get(i).toState.getName()));
                            
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