package importFile;

import java.util.ArrayList;
import java.util.List;

class CNFRule {
    String nonterminal;
    String[] rightHandSide;



    public CNFRule(String nonterminal, String[] rightHandSide) {
        this.nonterminal = nonterminal;
        this.rightHandSide = rightHandSide;

    }


    // Convert a BNF rule to a list of CNF rules
    static List<CNFRule> bnfToCNF(List<ProductionRule> bnfRule) {
        List<CNFRule> cnfRules = new ArrayList<>();
        for (ProductionRule rule : bnfRule) {

            if (rule.rightHandSide.size() == 1) {

                // If the right-hand side of the BNF rule consists of a single symbol,
                // create a single CNF rule with the same left-hand side and right-hand side
                cnfRules.add(new CNFRule(rule.nonterminal, new String[]{rule.rightHandSide.get(0)}));
            } else {
                // If the right-hand side of the BNF rule consists of two or more symbols,
                // create a new nonterminal symbol and create a CNF production rule with
                // the new nonterminal symbol on the left-hand side and the first symbol on the right-hand
                String newNonterminal = rule.nonterminal + "'";
                cnfRules.add(new CNFRule(newNonterminal, new String[]{rule.rightHandSide.get(0)}));
                for (int i = 1; i < rule.rightHandSide.size() ; i++) {

                    cnfRules.add(new CNFRule(newNonterminal, new String[]{rule.rightHandSide.get(i)}));
                }
                // Create a CNF production rule with the original nonterminal symbol on the left-hand side
                // and the new nonterminal symbol on the right-hand side
               // cnfRules.add(new CNFRule(rule.nonterminal, new String[]{newNonterminal}));
            }
        }
        return cnfRules;
    }
}

