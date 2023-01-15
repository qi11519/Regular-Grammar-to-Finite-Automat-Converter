package com.example.rgtonfa;


//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//public class ContextFreeGrammar {
//    Set<Character> terminalSymbols;
//    Set<String> nonterminalSymbols;
//    List<ProductionRule> productionRules;
//    String startSymbol;
//
//    public ContextFreeGrammar(Set<Character> terminalSymbols, Set<String> nonterminalSymbols,
//                              List<ProductionRule> productionRules, String startSymbol) {
//        this.terminalSymbols = terminalSymbols;
//        this.nonterminalSymbols = nonterminalSymbols;
//        this.productionRules = productionRules;
//        this.startSymbol = startSymbol;
//    }
//
//
//
//    public Set<Character> getTerminalSymbols() {
//        return terminalSymbols;
//    }
//
//    public Set<String> getNonterminalSymbols() {
//        return nonterminalSymbols;
//    }
//
//    public List<ProductionRule> getProductionRules() {
//        return productionRules;
//    }
//
//    public String getStartSymbol() {
//        return startSymbol;
//    }
//
//    // Apply the transition function to a state and a symbol
//    public Set<String> transition(Set<String> state, char symbol) {
//        Set<String> nextState = new HashSet<>();
//        for (String nonterminal : state) {
//            for (ProductionRule rule : productionRules) {
//                if (rule.nonterminal.equals(nonterminal) && rule.rightHandSide.get(0).charAt(0) == symbol) {
//                    nextState.addAll(rule.rightHandSide);
//                }
//            }
//        }
//        return nextState;
//    }
//}