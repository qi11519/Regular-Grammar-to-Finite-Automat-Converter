package importFile;

import java.util.*;



public class NondeterministicFiniteAutomaton {

    //Only for the purpose if has need to call functions in this class
    public NondeterministicFiniteAutomaton() {}

    // A class to represent a state in the NFA
    static class State {
        char stateName; //The state name: A, B, C, etc.
        boolean isAccepting; //Indicate if this state can be final state of a transition
        List<Transition> transitions; //List of possible transition with this state

        //Initiating a state
        public State(boolean isAccepting) {
            this.isAccepting = isAccepting;
            this.transitions = new ArrayList<>();
        }

        public void setTransitions(List<Transition> transitions){
            this.transitions = transitions;
        }

        public void setName(char stateName) {
            this.stateName = stateName;
        }

        public void setAccepting(Boolean isAccepting){
            this.isAccepting = isAccepting;
        }

        public char getName() {
            return this.stateName;
        }

        public List<Transition> getTransition() {
            return this.transitions;
        }

        public boolean getAcceptState() {
            return this.isAccepting;
        }
    }

    // A class to represent a transition in the NFA (In a state)
    static class Transition {
        char symbol; //Input in order to trigger this transition, exp: A -> 1B, then 1 is symbol
        State toState; //The next state of the transition

        //if this transition is the last state(No more transistion), toState will be Epsilon

        public Transition(char symbol, State toState) {
            this.symbol = symbol;
            this.toState = toState;
        }

        public char getSymbol() {
            return this.symbol;
        }
        
        public void setToState(State toState) {
            this.toState = toState;
        }
    }

    // A list of all states in the NFA
    List<State> states;

    // The start state of the NFA
    State startState;

    public void setStateList(List<State> states) {
        this.states = states;
    }

    public char getStartState() {
        return startState.getName();
    }

    //Example, A -> 1B
    //Hierachy of a state NFA as such:
        //State (A) 
            //->stateName (A)
            //->isAccepting (false)
            //->List<Transition>
                //-->>symbol (1)
                //-->>toState (Continue with the next State (B))
                    //--->stateName (B)
                    //->isAccepting (true)
                    //->List<Transition>
                        //-->>symbol (Epsilon)
                        //-->>toState (Epsilon)
                        ////toState = Epsilon means final state,
                        ////but that makes "B -> Epsilon" a final state as well


    ////////////////////////////////////////////////////////////////////////////////////////////
    //Convert RegularGrammar into NFA
    public NondeterministicFiniteAutomaton(List<RegularGrammar> grammarRules) {

        final char EPSILON = '\u03B5';
        // Create a start state for the NFA
        this.startState = new State(false);

        // Initialize the list of states
        this.states = new ArrayList<>();
        this.states.add(startState);

        // Iterate over the list of regular grammar rules
        for (RegularGrammar grammarRule : grammarRules) {

            //Instantiate a new state
            State state;

            boolean checking = false;
            //Check if the current state's right hand side has Epsilon as input(symbol) or not
            //If yes, the state's "isAccepting" will be TRUE
            for (RegularGrammar checkFinal : grammarRules){
                if (((grammarRule.rightHandSide.equals(checkFinal.nonterminal)) && (checkFinal.rightHandSide.equals(String.valueOf(EPSILON))))) {
                    checking = true;
                }  else {
                    if (grammarRule.rightHandSide.equals(String.valueOf(EPSILON))){
                        checking = true;
                    }
                }
            } 

            //Initialize the new state for the nonterminal (left-hand side of the rule), 
            //At this point everthing of the state is empty, only "isAccepting" and an empty list of "List<State>"
            //If the current state can reach accept/final state, then its "isAccepting" attribute is true
            if (checking){
                state = new State(true);
            } else {
                state = new State(false);
            }

            //Add the state to the list of states
            this.states.add(state);

            // Create a transition from the start state to the new state, labeled with the nonterminal
            state.setName(grammarRule.nonterminal.charAt(0));
            startState.transitions.add(new Transition(grammarRule.nonterminal.charAt(0), state));

            //Initialize the next state 
            State nextState = new State(checking); //Intialize with previous checking result as "isAccepting" attribute
            nextState.setName(grammarRule.rightHandSide.charAt(0)); //Set name
            this.states.add(nextState); //Add to the states list
            state.transitions.add(new Transition(grammarRule.input, nextState)); //Add transition to the next state
            state = nextState; //Move to the next state
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //To check if a specific transition from a state can possibly reach final state
    public boolean checkReachFinal(State state1, NondeterministicFiniteAutomaton NFA, List<LinkedList<Character>> visitedStates){

        //For checking if it reach final
        boolean goFinal = false;

        final char EPSILON = '\u03B5';

        //System.out.println("Current Checking Rule: "+ (state1.getName()));

        for (State state : NFA.states){
            
            boolean checking = true; //for checking if the state inside the visitedState list

            if (state.transitions.size() > 0) {

                //check if there is any visited states throughout the checking
                if (visitedStates.size() > 0) {
                    for (LinkedList<Character> checkState : visitedStates){
                        //Compare states and visided states each by each
                        //if false, means the current state's transition is visited, 
                        //then change to the next possible transition
                        if (((checkState.get(0) == state.getName()) )) {
                            //if true, then proceed with the following steps
                            for (Transition nextState : state.transitions){
                                if (checkState.get(1) == nextState.toState.getName()) {
                                    checking = false; //the state's transition has visited before
                                }
                            }          
                        }  
                    } 

                    //if the state's transition is not visited
                    if (checking) {
                        if (state.getName() == state1.getName()){
                            state1 = state;//temp state

                            State toState = state1.transitions.get(0).toState; //next state of the transition

                            //System.out.println("Found " + state1.getName()+" and "+toState.getName());

                            if (toState.getAcceptState() == true){ //if the next state is accept/final state
                                goFinal = true;
                                return goFinal; //return true, means current state can reach accept/final state
                            } else {
                                //if current state not able to reach accept/final state
                                //add the current state and its transition to "usedState" list,
                                //then add once again into the visitedStates, 
                                //so the algorithm won't recheck same transition again
                                LinkedList<Character> usedState = new LinkedList<>();
                                usedState.add(state.getName());
                                usedState.add(state.transitions.get(0).toState.getName());
                                //System.out.println("They added "+ state.getName() + " & " + state.transitions.get(0).toState.getName());
                                visitedStates.add(usedState);

                                //Proceed with recursion
                                //Then return true if it finds the state reachable.
                                goFinal = checkReachFinal(toState, NFA, visitedStates);

                                if (goFinal == true){
                                    return goFinal;
                                }
                            }

                        }
                    }
                } else { //if the "visitedStates" list is empty, then directly check current state if it reach final
                    if (state.getName() == state1.getName()){
                        state1 = state;//temp state

                        State toState = state1.transitions.get(0).toState; //next state of the transition

                        //System.out.println("Check " + state1.getName()+" and "+toState.getName());

                        if (toState.getName() == EPSILON){
                            goFinal = true;
                            return goFinal;
                        } else {
                            //if current state not able to reach accept/final state
                            //add the current state and its transition to "usedState" list,
                            //then add once again into the visitedStates, 
                            //so the algorithm won't recheck same transition again
                            LinkedList<Character> usedState = new LinkedList<>();
                            usedState.add(state.getName());
                            usedState.add(state.transitions.get(0).toState.getName());
                            //System.out.println("They added "+ state.getName() + " & " + state.transitions.get(0).toState.getName());
                            visitedStates.add(usedState);
                            
                            //Proceed with recursion
                            //Then return true if it finds the state reachable.
                            goFinal = checkReachFinal(toState, NFA, visitedStates);

                            if (goFinal == true){
                                return goFinal; 
                            }
                        }
                    }
                }
            }
        }
        return goFinal; //return the verify result
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //To remove Epsilon from a specific transition
    //This one has similar structure from the function "checkReachFinal()"
    public State removeEpsilon(State state1, NondeterministicFiniteAutomaton NFA, List<LinkedList<Character>> visitedStates){

        final char EPSILON = '\u03B5';

        for (State state : NFA.states){

            boolean checking = true; //for checking if the state inside the visitedState list

            if (state.transitions.size() > 0) {
                //check if there is any visited states throughout the checking
                if (visitedStates.size() > 0) {
                    for (LinkedList<Character> checkState : visitedStates){
                        //Compare states and visided states each by each
                        //if false, means the current state's transition is visited, 
                        //then change to the next possible transition
                        if (((checkState.get(0) == state.getName()) )) {
                            //if true, then proceed with the following steps
                            for (Transition nextState : state.transitions){
                                if (checkState.get(1) == nextState.toState.getName()) {
                                    checking = false; //the state's transition has visited before
                                }
                            }          
                        }  
                    } 
                    //if the state's transition is not visited
                    if (checking) {
                        if (state.getName() == state1.getName()){
                            state1 = state; //temp state

                            State toState = state1.transitions.get(0).toState; //next state of the transition

                            //System.out.println("Found " + state1.getName()+" and "+toState.getName());
                            
                            if (state1.isAccepting){ //if the current state is accept/final state
                                return toState; //return next state
                            } else if (state1.transitions.get(0).getSymbol()!=EPSILON){ //if the next state's transition has Epsilon
                                return toState; //return next state
                            } else {
                                //if current state(transition) has Epsilon
                                //add the current state and its transition to "usedState" list,
                                //then add once again into the visitedStates, 
                                //so the algorithm won't recheck same transition again
                                LinkedList<Character> usedState = new LinkedList<>();
                                usedState.add(state.getName());
                                usedState.add(state.transitions.get(0).toState.getName());
                                
                                visitedStates.add(usedState);
                                
                                //Proceed with recursion
                                //Then return true if it finds the state's transition with Epsilon
                                toState = removeEpsilon(toState, NFA, visitedStates);
                                
                                return toState;
                            }

                        }
                    }
                } else { //if the "visitedStates" list is empty, then directly check current state transition has Epsilon
                    if (state.getName() == state1.getName()){
                        state1 = state; //temp state

                        State toState = state1.transitions.get(0).toState; //next state of the transition

                        //if the current state is accept/final state OR if the next state's transition has Epsilon
                        if (state1.isAccepting && (state1.transitions.get(0).getSymbol()!=EPSILON)){

                            return toState; //return next state
                        } else {
                            //if current state(transition) has Epsilon
                            //add the current state and its transition to "usedState" list,
                            //then add once again into the visitedStates, 
                            //so the algorithm won't recheck same transition again
                            LinkedList<Character> usedState = new LinkedList<>();
                            usedState.add(state.getName());
                            usedState.add(state.transitions.get(0).toState.getName());
                            
                            visitedStates.add(usedState);
                            
                            //Proceed with recursion
                            //Then return true if it finds the state's transition with Epsilon
                            toState = removeEpsilon(toState, NFA, visitedStates);
                            
                            return toState;
                        }
                    }
                }
            }
        }
        return state1;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Remove all the Epsilon, then replace with the correct result without Epsilon
    public List<State> renewStates(NondeterministicFiniteAutomaton NFA, List<State> oldStateList) {
        
        //Keep track of which state/rule being visited
        List<LinkedList<Character>> visitedStates = new ArrayList<LinkedList<Character>>(); 

        this.startState = new State(false);
        
        // Initialize the list of states, this is the one will be return later
        List<State> states = new ArrayList<State>();
        states.add(startState); //There is a need for the first empty starting state

        final char EPSILON = '\u03B5';

        //Checking all the states each by each
        for (State state : NFA.states){

            if (state.getName() != '\u0000'){
                
                for (Transition transition : state.transitions){ //Checking all the transitions of the state each by each

                    if(transition.getSymbol() != EPSILON){ //check if current transition's input(symbol) is not Epsilon
                        
                        //Initialize a new temp state, where it return the version of its state's transition but WITHOUT EPSILON
                        //Example:
                        //If initial transition was A -> 0C, then C -> B;
                        //then the "removeEpsilon" function return A -> 0B directly
                        State state1 = new State(removeEpsilon(state, NFA, visitedStates).getAcceptState());

                        //Set name, for unknown reason, this has to be done separately, else it doesn't work
                        state1.setName(removeEpsilon(state, NFA, visitedStates).getName());

                        //Set its transition from the return state, reason same as previous line
                        state1.setTransitions(removeEpsilon(state, NFA, visitedStates).getTransition());

                         //if the state is not pre-existed from the original NFA, then add it into the states list
                        if (state != state1){
                            //Initial another temp state, with the "isAccepting" attribute set as same as from the returned state
                            State state2 = new State(state1.getAcceptState()); 

                            state2.setName(state.getName()); //Set name

                            state2.transitions.add(new Transition(transition.getSymbol(), state1)); //Set transition

                            states.add(state2); //Add to the states list

                            //System.out.println("-------------------");

                        }
                    }
                }
            }
        }

        //For some reason, the algorithm above will skip those transition 
        //Where its afterwards transition has Epsilon. In a nutshell:
        //If initial transition was A -> 0C, then C -> B;
        //then the "removeEpsilon" function return A -> 0B directly
        //But it only save A -> 0B, and ignored A-> 0C, despite it still valid

        //Another states list, this is for the purpose of storing those state
        //That did not added into the previous states list, but still valid
        List<State> tempStateList = new ArrayList<State>();

        //To check if there are states that are still valid but
        //Did not included into the previous states list, from the existing NFA states
        for (State state1 : oldStateList){
            for (Transition transition1 : state1.transitions){

                //Same 
                if ((transition1.getSymbol() != EPSILON) && (state1.getName() != '\u0000')){
                    //Make a list of Boolean, compare those transition one by one, once already included, skip to next one
                    List<Boolean> checkList = new ArrayList<>();

                    //Basically, it compare states that are valid, from original NFA, to the previous states list.
                    //If a rule being check one by one to each of transition in the previous states list,
                    //Example result Boolean list will be as such
                    //[True, False, False]
                    //If even ONE True existed, means its already inside the previous states list
                    for (int i = 0; i < states.size(); i++){
                        if (states.get(i).transitions.size() > 0){
                            if (((state1.getName() == states.get(i).getName()) && (transition1.toState.getName() == states.get(i).transitions.get(0).toState.getName()) && (transition1.getSymbol() == states.get(i).transitions.get(0).getSymbol()))){
                                    checkList.add(true);
                                } else {
                                    checkList.add(false);
                                }
                            }
                        }

                    //if the state's transitions is not found in the previous states list, add to the temp list
                    if (!(checkList.contains(true))){
                        tempStateList.add(state1);
                    }
                }
            }
        }
        //Add all states in the temp list, to the previous states list
        states.addAll(tempStateList);

        return states; //return the states list
    }

}

