package com.example.rgtonfa;

import java.util.ArrayList;
import java.util.List;

public class RegularGrammar {
    String nonterminal; //Front
    //List<String> rightHandSide; //Stores multiple result as list element
    String rightHandSide; //Change to String for splitting a rule for different output
    char input;

    public RegularGrammar(String nonterminal, String rightHandSide, char input) { //List<String> rightHandSide
        this.nonterminal = nonterminal;
        this.rightHandSide = rightHandSide;
        this.input = input;
    }

    //Parse the input grammar into Regular Grammar object
    static List<RegularGrammar> parseGrammar(List<String> grammar) {
        //Container for all regular grammar rules
        List<RegularGrammar> grammarRules = new ArrayList<>();

        List<Character> nonTerminal = new ArrayList<>();

        for (int i=0; i < grammar.size(); i++){
            nonTerminal.add(grammar.get(i).charAt(0));
        }

        //Parse each rule string into 2 parts,
        //nonterminal & rightHandSides
        for (String rule : grammar) {

            //Split rule string
            String[] parsedRule = rule.split("->");
            //First part is left hand side
            //Second part is right hand side
            String nonterminal = parsedRule[0].trim(); //Trim remove empty spaces
            String[] rightHandSides = parsedRule[1].split("\\|");
            
            final char EPSILON = '\u03B5';
            char symbolInput = EPSILON;
            String terminal = "F";

            //Since right side can have multiple result, so store as a list
            List<String> rightHandSideList = new ArrayList<>();
            for (String rightHandSide : rightHandSides) {
                //Trim prevent random empty spaces remain in the string rule
                rightHandSideList.add(rightHandSide.trim()); //Trim remove empty spaces
            }

            //Add into list for each parsed regular grammar rules
            for (int i = 0; i < rightHandSideList.size(); i++) {
                
                for (int j = 0; j < rightHandSideList.get(i).length(); j++) {
                    
                    if (!(nonTerminal.contains(rightHandSideList.get(i).charAt(j)))) {
                        symbolInput = rightHandSideList.get(i).charAt(j);
                    } else {
                        terminal = String.valueOf(rightHandSideList.get(i).charAt(j));
                    }
                }
                grammarRules.add(new RegularGrammar(nonterminal, terminal, symbolInput));
            }
        }

        return grammarRules;
    }

}

//check one character contain in a list in java?