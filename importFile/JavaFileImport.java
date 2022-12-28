package importFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.List;

import importFile.NondeterministicFiniteAutomaton.State;
import importFile.NondeterministicFiniteAutomaton.Transition;

import java.util.*;


public class JavaFileImport {

    static final char EPSILON = '\u03B5';
    public static void main(String[] args) throws IOException {
        // Read the text file into a list of strings, one string per line
        List<String> textLines = Files.readAllLines(Paths.get("regularGrammar.txt"));
        List<String> ruleStringList = new ArrayList<>();

        final char EPSILON = '\u03B5';
        /*
        // Print the contents of the text file
        for (String line : lines) {
            System.out.println(line);
        }*/

        System.out.println("-----I AM A LINE-----");

        //Turn the imported text file content into
        //each grammar rule per line
        for (String line : textLines) {
            String ruleString = line; //.replace("->", "::=");
            System.out.println(ruleString);
            ruleStringList.add(ruleString);
        }

        System.out.println("-----I AM A LINE-----");
        
        List<RegularGrammar> grammarRules = RegularGrammar.parseGrammar(ruleStringList);

        /* For printing all the shit in the rule list
        for (int i = 0; i < grammarRules.size(); i++){
            
            System.out.println("Non-terminal: " + grammarRules.get(i).nonterminal);
            
            for (int j = 0; j < grammarRules.get(i).rightHandSide.size(); j++){
        
                System.out.println("Right hand side: " + grammarRules.get(i).rightHandSide.get(j));
            }
        }
        System.out.println("-----I AM A LINE-----");
        */

        ////////////////////////////////////////////////////////////////////////////////////////////////
        
        NondeterministicFiniteAutomaton NFA = new NondeterministicFiniteAutomaton(grammarRules);

        for (State state : NFA.states) {
            List<Transition> toStateName = state.getTransition();
            
            if (toStateName.size() > 0){
                if (toStateName.get(0).toState.transitions.size() > 0){
                    //if (toStateName.get(0).toState.transitions.get(0).toState.transitions.size() > 0){
                    
                    for (int i = 0; i < toStateName.size(); i++){
                        if (toStateName.get(i).toState.transitions.get(0).toState.getName() != 'F'){
                            System.out.println("StartState: "+ toStateName.get(i).toState.getName() +", toState: "+ toStateName.get(i).toState.transitions.get(0).toState.getName() +", Input: "+ toStateName.get(i).toState.transitions.get(0).getSymbol()+", State: "+ toStateName.get(i).toState.transitions.get(0).toState.getAcceptState());
                        } else {
                            System.out.println("StartState: "+ toStateName.get(i).toState.getName() +", State: "+ toStateName.get(i).toState.transitions.get(0).toState.getAcceptState() + ", THIS IS ULTIMATE SUPER FINAL STATE");
                        }
                    }
                }
            }
        }
        //A
            //Symbol: 1
            //List<Transition>
                //B
                    //Symbol: EPSILON
                    //List<Transition>

        System.out.println("-----I AM A LINE-----");
        ////////////////////////////////////////////////////////////////////////
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
        System.out.printf("%-5s ", String.valueOf(EPSILON));
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
        System.out.printf("%-5s ", findNFAResult(grammarRules, 'A', "0"));
        System.out.print("| ");
        System.out.printf("%-5s ", findNFAResult(grammarRules, 'A', "1"));
        System.out.print("| ");
        System.out.printf("%-5s ", findNFAResult(grammarRules, 'A', String.valueOf(EPSILON)));
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
        System.out.printf("%-5s ", findNFAResult(grammarRules, 'B', "0"));
        System.out.print("| ");
        System.out.printf("%-5s ", findNFAResult(grammarRules, 'B', "1"));
        System.out.print("| ");
        System.out.printf("%-5s ", findNFAResult(grammarRules, 'B', String.valueOf(EPSILON)));
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
        System.out.printf("%-5s ", findNFAResult(grammarRules, 'C', "0"));
        System.out.print("| ");
        System.out.printf("%-5s ", findNFAResult(grammarRules, 'C', "1"));
        System.out.print("| ");
        System.out.printf("%-5s ", findNFAResult(grammarRules, 'C', String.valueOf(EPSILON)));
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
    }

    //FOR PRINTING CORESPONDING RESULT IN THE TABLE
    public static String findNFAResult(List<RegularGrammar> grammarRules, char nonTerminal, String symbolInput){
        
        String result = "X";

        NondeterministicFiniteAutomaton NFA = new NondeterministicFiniteAutomaton(grammarRules);

        for (State state : NFA.states) { //Checking state by state

            List<Transition> toStateName = state.getTransition();

            if (toStateName.size() > 0){

                if (toStateName.get(0).toState.transitions.size() > 0){
                    
                    for (int i = 0; i < toStateName.size(); i++){
                        
                        //Compare if starting state same as the current state row
                        if(toStateName.get(i).toState.getName() == nonTerminal){

                            //Compare if input symbol same as the current state column (Exclude E)
                            if((toStateName.get(i).toState.transitions.get(0).getSymbol() == symbolInput.charAt(0)) && (toStateName.get(i).toState.transitions.get(0).toState.getName() != 'F')){    
                                
                                result = String.valueOf(toStateName.get(i).toState.transitions.get(0).toState.getName());
                                
                            }      
                        }
                    }
                }
            }
        }
        return result;
    }
    ///////////////////////////////////////
}