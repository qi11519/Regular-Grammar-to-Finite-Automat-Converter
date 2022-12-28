package importFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.List;
import java.util.*;


public class JavaFileImport {
    public static void main(String[] args) throws IOException {
        // Read the text file into a list of strings, one string per line
        List<String> lines = Files.readAllLines(Paths.get("C:/Users/Vs/Desktop/tools/java/TheoryOfComputation/regularGrammar.txt"));
        List<String> newRules = new ArrayList<>();

        // Print the contents of the text file
        for (String line : lines) {

            System.out.println(line);

        }


        //replace all the -> to ::=
        for (String line : lines) {
            String newRule = line.replace("->", "::=");
            newRules.add(newRule);
        }
        //print the replaced string
        for (String line : newRules) {

            System.out.println(line);

        }


        //get the token
//        StringTokenizer result=token.tokenStr(newRules.toString());
//        while (result.hasMoreTokens()) {
//
//            // Getting next tokens
//            System.out.println(result.nextToken());
//
//        }
        List<ProductionRule> grammar = ProductionRule.parseBNF(newRules);
        System.out.println(grammar.get(0).nonterminal);
        System.out.println(grammar.get(0).rightHandSide.get(0));

        List<CNFRule> cnfRules = CNFRule.bnfToCNF(grammar);
        System.out.println("hello");

        System.out.println(cnfRules.get(0).rightHandSide[0]);
        //System.out.println(cnfRules.get(0).rightHandSide[1]);

        System.out.println(cnfRules.get(1).rightHandSide[0]);
        //System.out.println(cnfRules.get(1).rightHandSide[1]);
        System.out.println(cnfRules.get(0).nonterminal);

        System.out.println(cnfRules.get(1).nonterminal);


        //convert all the rule to bnf
//      ContextFreeGrammar cfg=ProductionRule.parseBNF1(newRules);
//
//        System.out.println(cfg.getNonterminalSymbols());
//        System.out.println(cfg.startSymbol);
//
//        //System.out.println(cfg.nonterminalSymbols);
//        System.out.println(cfg.terminalSymbols);
//        System.out.println("the fuck");

//

        NondeterministicFiniteAutomaton nfa = NondeterministicFiniteAutomaton.cnfRulesToNFA(cnfRules);

//        System.out.println(cnfRules.get(0).toString());

//        CNFRule cnfRule = new CNFRule("A", new String[]{"1B", "0C"});
//        NondeterministicFiniteAutomaton nfaexp = cnfToNFA(cnfRule);
//        System.out.println(nfaexp.toString());

//
        for (NondeterministicFiniteAutomaton.Transition transition : nfa.transitions) {
            String toStateName = transition.getToState().name;
            String startState = transition.getFromState().name;
            System.out.println("start state " + startState);
            System.out.println("tostate " + toStateName);


        }
//        System.out.println("fuck");
//        Set<NondeterministicFiniteAutomaton.State> finalStates = nfa.getStates();
//        for (NondeterministicFiniteAutomaton.State state : finalStates) {
//            String stateName = state.getName();
//            Boolean isAccept=state.isAccepting();
//            System.out.println("the state name "+stateName);
//            System.out.println("is this state is final state "+isAccept);
////
////             do something with the state name
//        }

////        List grammar2= parseBNF(newRules);
//        System.out.println(nfa.getStates().getClass().getName());

        //convert all the rule to cnf


//print all the state and where the state can go in cnf form
//        for(int i =0;i<cnfRules.size();i++){
//
//            System.out.println(cnfRules.get(i).nonterminal);
//            System.out.println(cnfRules.get(i).rightHandSide[0]);
////
//
//        }

    }
}



    

