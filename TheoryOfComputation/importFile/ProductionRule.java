package importFile;
import java.util.*;

import importFile.token;


class ProductionRule {
    String nonterminal;
    List<String> rightHandSide;

    boolean isTerminal;

    public ProductionRule(String nonterminal, List<String> rightHandSide) {
        this.nonterminal = nonterminal;
        this.rightHandSide = rightHandSide;
    }
//     Convert a regular grammar string to a list of production rules in BNF
    static List<ProductionRule> parseBNF(List<String> grammarString) {
        List<ProductionRule> grammar = new ArrayList<>();


        // Split the grammar string into a list of lines
       // String[] lines = grammarString.split("\n");

        // Parse each line as a production rule
        for (String line : grammarString) {

            String[] parts = line.split("::=");
            String nonterminal = parts[0].trim();
            String[] rightHandSides = parts[1].split("\\|");

            List<String> rightHandSideList = new ArrayList<>();
            for (String rightHandSide : rightHandSides) {
                rightHandSideList.add(rightHandSide.trim());
            }


            grammar.add(new ProductionRule(nonterminal, rightHandSideList));
        }

        return grammar;
    }
//    static ContextFreeGrammar parseBNF1(List<String> grammarString) {
//        Set<Character> terminalSymbols = new HashSet<>();
//        Set<String> nonterminalSymbols = new HashSet<>();
//        List<ProductionRule> productionRules = new ArrayList<>();
//        String startSymbol = null;
//
//        // Parse each line as a production rule
//        for (String line : grammarString) {
//            String[] parts = line.split("::=");
//            String nonterminal = parts[0].trim();
//            String[] rightHandSide = parts[1].split("\\|");
//            nonterminalSymbols.add(nonterminal);
//            if (startSymbol == null) {
//                startSymbol = nonterminal;
//            }
//
//            // Split the expansion into individual symbols
//            for (String symbol : rightHandSide) {
//                symbol = symbol.trim();
//                if (Character.isLowerCase(symbol.charAt(0))) {
//                    terminalSymbols.add(symbol.charAt(0));
//                    productionRules.add(new ProductionRule(nonterminal, Arrays.asList(symbol)));
//                } else {
//                    nonterminalSymbols.add(symbol);
//                    productionRules.add(new ProductionRule(nonterminal, Arrays.asList(symbol)));
//                }
//            }
//        }
//
//        return new ContextFreeGrammar(terminalSymbols, nonterminalSymbols, productionRules, startSymbol);
//    }
}