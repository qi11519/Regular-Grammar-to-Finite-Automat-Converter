package importFile;

import java.util.*;

public class RegularGrammar {
    String nonterminal; //Front
    //List<String> rightHandSide; //Stores multiple result as list element
    String rightHandSide; //Change to String for splitting a rule for different output
    char input; //input(symbol) of a transition

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
            String terminal = String.valueOf(EPSILON); //Temporary 

            //Since right side can have multiple result, so store as a list
            List<String> rightHandSideList = new ArrayList<>();
            for (String rightHandSide : rightHandSides) {
                //Trim prevent random empty spaces remain in the string rule
                rightHandSideList.add(rightHandSide.trim()); //Trim remove empty spaces
            }

            //Add into list for each parsed regular grammar rules
            for (int i = 0; i < rightHandSideList.size(); i++) {

                for (int j = 0; j < rightHandSideList.get(i).length(); j++) {

                    if ((rightHandSideList.get(i).length() < 2)){
                        ///Basically, if "A -> 0C", then input(symbol) is 0;
                        ///But if "A -> C", then no input(symbol) before the next state, that means the input(symbol) is Epsilon
                        if (!(nonTerminal.contains(rightHandSideList.get(i).charAt(j))) && (rightHandSideList.get(i).charAt(j) != 'F')) {
                            //if has input(symbol)
                            symbolInput = rightHandSideList.get(i).charAt(j);
                            terminal = String.valueOf(rightHandSideList.get(i).charAt(j));

                        } else { //if without input(symbol)
                            symbolInput = EPSILON;
                            terminal = String.valueOf(rightHandSideList.get(i).charAt(j));
                        }
                    } else { //if only one character on the right hand side
                        if (!(nonTerminal.contains(rightHandSideList.get(i).charAt(j)))) {

                            symbolInput = rightHandSideList.get(i).charAt(j);
                        } else {
                            terminal = String.valueOf(rightHandSideList.get(i).charAt(j));
                        }
                    }
                }
                grammarRules.add(new RegularGrammar(nonterminal, terminal, symbolInput));
            }
        }

        return grammarRules;
    }

}