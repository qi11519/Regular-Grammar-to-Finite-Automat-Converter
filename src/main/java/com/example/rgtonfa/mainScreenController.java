package com.example.rgtonfa;


import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import com.example.rgtonfa.FiniteAutomat.State;
import com.example.rgtonfa.FiniteAutomat.Transition;

import static com.example.rgtonfa.JavaFileImport.findNFAResult;
import static com.example.rgtonfa.JavaFileImport.findNFAwithoutEpsilonResult;


public class mainScreenController {

    List<RegularGrammar> grammarRules;
    FiniteAutomat NFA;
    FiniteAutomat NFAwoEpsilon;
    FiniteAutomat DFA;
    FiniteAutomat min_DFA;

    @FXML
    private Button buttonEpsilon;

    private GraphicsContext gc;

    @FXML
    private Pane drawArea;

    private Stage primaryStage;

    @FXML
    private Button buttonArrow;

    @FXML
    private Button btnImport;

    @FXML
    private TextArea inputArea;

    @FXML
    private TableView<String[]> stateTable;

    @FXML
    private Button nfaButton;

    @FXML
    private Button nfaEpsilonButton;

    @FXML
    private Button dfaButton;

    @FXML
    private Button mindfaButton;

    @FXML
    private Button testButton;

    @FXML
    private TextArea testArea;

    @FXML
    private TextArea resultArea;

    @FXML
    private Button executeButton;

    @FXML
    private TextArea faInfo;


    //FiniteAutomat finite_automat;

    //Symbol for EPSILON
    final char EPSILON = '\u03B5';

    TableColumn<String[], String> stateCol = new TableColumn<>("State");
    TableColumn<String[], String> inputCol1 = new TableColumn<>("0");
    TableColumn<String[], String> inputCol2 = new TableColumn<>("1");

    TableColumn<String[], String> inputCol3 = new TableColumn<>(String.valueOf(EPSILON));

    List<String> NFADescription;
    List<String> NFAwoEpsilonDescription;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }



    @FXML
    void onEpsilonClick(ActionEvent event) {
        //displayDiagram();
        inputArea.appendText("ε");
    }

    @FXML
    void onArrowClick(ActionEvent event) throws IOException {
        inputArea.appendText("->");
    }

    @FXML
    void onBtnImport(ActionEvent event) throws IOException {
        
        resultArea.scrollTopProperty().bindBidirectional(testArea.scrollTopProperty());

        NFADescription = new ArrayList<>();
        NFAwoEpsilonDescription = new ArrayList<>();

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        
        List<String> ruleStringList = readFile(selectedFile);
    
        for (String rule : ruleStringList) {
            inputArea.appendText(rule + "\n");
        }

        String text = inputArea.getText();

        String[] lines = text.split("\n");
        
        ///////////////////////////////////////////////////////
        //MAKING REGULAR GRAMMAR
        this.grammarRules = RegularGrammar.parseGrammar(ruleStringList);

        ///////////////////////////////////////////////////////
        //MAKING NFA
        this.NFA = new FiniteAutomat(grammarRules);
        
        ////////////////////////////////////////
        NFADescription.add("M = ( Q , Σ , δ , P0 , F )");

        List<String> possibleStates = new ArrayList<>();

        for (State state : NFA.states){
            if (state.getName() != null && !state.getName().equals(String.valueOf(EPSILON))){
                if (!possibleStates.contains(state.getName())){
                    possibleStates.add(state.getName());
                }
            }
        }

        String Q = "Q = { ";

        for (int i = 0; i < possibleStates.size(); i++){
            if (i != 0){
                Q = Q + ", ";
            }
            Q = Q + possibleStates.get(i);
            if (i == possibleStates.size()-1){
                Q = Q + " }";
            }
        }

        NFADescription.add(Q);
        NFADescription.add("Σ = { 0, 1 }");
        NFADescription.add("δ : Q x Σε -> Pow(Q)");
        NFADescription.add("P0 = "+NFA.getStartingState());

        List<String> finalStates = new ArrayList<>();
        
        for (State state : NFA.states){
            if (state.getName() != null && !state.getName().equals(String.valueOf(EPSILON))){
                if (!finalStates.contains(state.getName())){
                    for (Transition transition : state.transitions){
                        if (state.getAcceptState() == true && transition.getSymbol() == EPSILON && transition.toState.getName().equals(String.valueOf(EPSILON))){
                            finalStates.add(state.getName());
                        }
                    }
                }
            }
        }

        String F = "F = { ";

        for (int i = 0; i < finalStates.size(); i++){
            if (i != 0){
                F = F + ", ";
            }
            F = F + finalStates.get(i);
            if (i == finalStates.size()-1){
                F = F + " }";
            }
        }

        NFADescription.add(F);
        //////////////////////////////////////

        //For modify later
        this.NFAwoEpsilon = new FiniteAutomat(grammarRules);
        this.DFA = new FiniteAutomat(grammarRules);
        this.min_DFA = new FiniteAutomat(grammarRules);

        ///////////////////////////////////////////////////////
        //MAKING NFA WITHOUT EPSILON
        FiniteAutomat fa = new FiniteAutomat();

        //Keep track of which state/rule being visited
        List<LinkedList<Character>> visitedStates = new ArrayList<LinkedList<Character>>(); 

        //Templist for storing the 
        //list of states of NFA without Epsilon
        List<State> StatesWithoutEpsilon = new ArrayList<>();
        
        //Templist that stored the states of NFA
        List<State> oldStateList = NFA.states;

        //Convert states with EPSILON into without EPSILON, 
        //then add into the templist
        StatesWithoutEpsilon = fa.renewStates(NFA, oldStateList);
        
        //Replace the current list of states of the NFA, 
        //which make it become NFA without EPSILON
        NFAwoEpsilon.setStateList(StatesWithoutEpsilon);

                
        NFAwoEpsilonDescription.add("M = ( Q , Σ , δ , P0 , F )");

        List<String> possibleStates2 = new ArrayList<>();

        for (State state : NFAwoEpsilon.states){
            if (state.getName() != null && !state.getName().equals(String.valueOf(EPSILON))){
                if (!possibleStates2.contains(state.getName())){
                    possibleStates2.add(state.getName());
                }
            }
        }

        String Q2= "Q = { ";

        for (int i = 0; i < possibleStates2.size(); i++){
            if (i != 0){
                Q2 = Q2 + ", ";
            }
            Q2 = Q2 + possibleStates2.get(i);
            if (i == possibleStates2.size()-1){
                Q2 = Q2 + " }";
            }
        }

        NFAwoEpsilonDescription.add(Q2);
        NFAwoEpsilonDescription.add("Σ = { 0, 1 }");
        NFAwoEpsilonDescription.add("δ : Q x Σε -> Pow(Q)");
        NFAwoEpsilonDescription.add("P0 = "+NFA.getStartingState());

        List<String> finalStates2 = new ArrayList<>();
        
        for (State state : NFAwoEpsilon.states){
            if (state.getName() != null && !state.getName().equals(String.valueOf(EPSILON))){
                if (!finalStates2.contains(state.getName())){
                    for (Transition transition : state.transitions){
                        if (state.getAcceptState() == true && transition.getSymbol() == 'Z' && transition.toState.getName().equals("Z")){
                            finalStates2.add(state.getName());
                        }
                    }
                }
            }
        }

        String F2 = "F = { ";

        for (int i = 0; i < finalStates2.size(); i++){
            if (i != 0){
                F2 = F2 + ", ";
            }
            F2 = F2 + finalStates2.get(i);
            if (i == finalStates2.size()-1){
                F2 = F2 + " }";
            }
        }

        NFAwoEpsilonDescription.add(F2);

        ///////////////////////////////////////////////////////
        //MAKING DFA
        List<State> dfaStates = new ArrayList<>();
        List<State> dfaStates2 = new ArrayList<>();
        
        //Templist that stored the states of NFA
        List<State> oldStateListwoEPSILON = NFAwoEpsilon.states;

        dfaStates = fa.convertToDFA(NFAwoEpsilon, oldStateListwoEPSILON);
        dfaStates2 = fa.convertToDFA(NFAwoEpsilon, oldStateListwoEPSILON); //For modify later

        //Replace the current list of states of the NFA, 
        //which make it become NFA without EPSILON
        this.DFA.setStateList(dfaStates);

        ///////////////////////////////////////////////////////
        //MAKING MINIMIZED DFA
        List<State> minDfaStates = new ArrayList<>();
        minDfaStates = fa.minimizeState(dfaStates2);

        this.min_DFA.setStateList(minDfaStates);

        nfaButton.setDisable(false);

        nfaEpsilonButton.setDisable(false);

        dfaButton.setDisable(false);

        mindfaButton.setDisable(false);

        testButton.setDisable(false);

        btnImport.setDisable(true);
    }

    @FXML
    void onClearClick(ActionEvent event) {
        inputArea.clear();
        stateTable.getItems().clear();
        stateTable.getColumns().clear();

        testArea.clear();
        testArea.setVisible(false);
        resultArea.setVisible(false);
        executeButton.setVisible(false);

        nfaButton.setDisable(true);

        nfaEpsilonButton.setDisable(true);

        dfaButton.setDisable(true);

        mindfaButton.setDisable(true);

        testButton.setDisable(true);

        btnImport.setDisable(false);
    }
    //use to draw


    @FXML
    void onNfaButtonClick(ActionEvent event) throws IOException {

        stateTable.getItems().clear();
        stateTable.getColumns().clear();

        testArea.setVisible(false);
        resultArea.setVisible(false);
        executeButton.setVisible(false);

        faInfo.setVisible(true);
        faInfo.clear();
        for (String lineString : NFADescription) {
            faInfo.appendText(lineString + "\n");
        }

        //add column to table
        stateTable.getColumns().addAll(stateCol, inputCol1, inputCol2,inputCol3);

        Set<String> statesSet = new HashSet<>();

        for (State state : this.NFA.states){
            if (state.getName() != null && !state.getName().equals(String.valueOf(EPSILON))){
                statesSet.add(state.getName());
            }
        }

        String[] uniqueStates = new String[statesSet.size()];
        int a = 0;
        for (String state : statesSet) {
            uniqueStates[a] = state;
            a++;
        }

        char[] inputs = {'0', '1', EPSILON};

        String[][] dataArray = new String[uniqueStates.length][inputs.length+1];

        for (int i = 0; i < uniqueStates.length; i++) {
            dataArray[i][0] = uniqueStates[i];
            for (int j = 0; j < inputs.length; j++) {
                dataArray[i][j+1] = findNFAResult(this.NFA, uniqueStates[i], inputs[j]);
            }
        }


        //give a target for column to read the data, 0 represent the column will read [x][0] data
        stateCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[0]));
        inputCol1.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[1]));
        inputCol2.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[2]));
        inputCol3.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[3]));

        //set data to table
        stateTable.setItems(FXCollections.observableArrayList(dataArray));

    }
    @FXML
    void onNfaEpsilon(ActionEvent event) throws IOException {

        stateTable.getItems().clear();
        stateTable.getColumns().clear();

        testArea.setVisible(false);
        resultArea.setVisible(false);
        executeButton.setVisible(false);

        faInfo.setVisible(true);
        faInfo.clear();
        for (String lineString : NFAwoEpsilonDescription) {
            faInfo.appendText(lineString + "\n");
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////
        //PRINT A TABLE FOR NFA WITHOUT EPSILON, and set data to java fx


        //here are place to give data dynamic

        Set<String> statesSet = new HashSet<>();

        for (State state : this.NFAwoEpsilon.states){
            if (state.getName() != null && !state.getName().equals(String.valueOf(EPSILON))){
                statesSet.add(state.getName());
            }
        }

        String[] uniqueStatesNfa = new String[statesSet.size()];
        int a = 0;
        for (String state : statesSet) {
            uniqueStatesNfa[a] = state;
            a++;
        }
        char[] inputs = {'0', '1'};
        String[][] dataArray = new String[10][10];

        for (int i = 0; i < uniqueStatesNfa.length; i++) {


            dataArray[i][0] = uniqueStatesNfa[i];
            for (int j = 0; j < inputs.length; j++) {

                String result = "";
                for (String foundState : findNFAwithoutEpsilonResult(NFAwoEpsilon, uniqueStatesNfa[i], inputs[j])) {
                    result += foundState + " ";
                }
                dataArray[i][j+1] = result;
            }
        }

        stateTable.getColumns().addAll(stateCol, inputCol1, inputCol2);

        stateCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[0]));
        inputCol1.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[1]));
        inputCol2.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[2]));
        stateTable.setItems(FXCollections.observableArrayList(dataArray));
    }

    @FXML
    void onDfaButton(ActionEvent event) throws IOException {

        stateTable.getItems().clear();
        stateTable.getColumns().clear();

        testArea.setVisible(false);
        resultArea.setVisible(false);
        executeButton.setVisible(false);

        faInfo.setVisible(false);

        Set<String> statesSet = new HashSet<>();

        for (State state : this.DFA.states){
            if (state.getName() != null && !state.getName().equals(String.valueOf(EPSILON))){
                if (state.getName().equals(DFA.getStartingState())){
                    String tempString = "->"+state.getName();
                    statesSet.add(tempString);
                } else if (state.getAcceptState()) {
                    String tempString = "*"+state.getName();
                    statesSet.add(tempString);
                } else {
                    statesSet.add(state.getName());
                }
            }
        }

        String[] uniqueStatesNfa = new String[statesSet.size()];
        int a = 0;
        for (String state : statesSet) {
            uniqueStatesNfa[a] = state;
            a++;
        }
        char[] inputs = {'0', '1'};

        //int index =0;
        String[][] dataArray = new String[10][10];

        for (int i = 0; i < uniqueStatesNfa.length; i++) {


            dataArray[i][0] = uniqueStatesNfa[i];
            for (int j = 0; j < inputs.length; j++) {

                String result = "";
                for (String foundState : findNFAwithoutEpsilonResult(this.DFA, uniqueStatesNfa[i], inputs[j])) {
                    result += foundState + " ";
                }
                dataArray[i][j+1] = result;
            }
        }

        stateTable.getColumns().addAll(stateCol, inputCol1, inputCol2);

        stateCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[0]));
        inputCol1.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[1]));
        inputCol2.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[2]));


        stateTable.setItems(FXCollections.observableArrayList(dataArray));
    }

    @FXML
    void onMinDfaButton(ActionEvent event) throws IOException {

        stateTable.getItems().clear();
        stateTable.getColumns().clear();

        testArea.setVisible(false);
        resultArea.setVisible(false);
        executeButton.setVisible(false);

        faInfo.setVisible(false);

        Set<String> statesSet = new HashSet<>();

        for (State state : this.min_DFA.states){
            if (state.getName() != null && !state.getName().equals(String.valueOf(EPSILON))){
                if (state.getName().equals(DFA.getStartingState())){
                    String tempString = "->"+state.getName();
                    statesSet.add(tempString);
                } else if (state.getAcceptState()) {
                    String tempString = "*"+state.getName();
                    statesSet.add(tempString);
                } else {
                    statesSet.add(state.getName());
                }
            }
        }

        String[] uniqueStatesNfa = new String[statesSet.size()];
        int a = 0;
        for (String state : statesSet) {
            uniqueStatesNfa[a] = state;
            a++;
        }
        char[] inputs = {'0', '1'};

        //int index =0;
        String[][] dataArray = new String[10][10];

        for (int i = 0; i < uniqueStatesNfa.length; i++) {


            dataArray[i][0] = uniqueStatesNfa[i];
            for (int j = 0; j < inputs.length; j++) {

                String result = "";
                for (String foundState : findNFAwithoutEpsilonResult(this.min_DFA, uniqueStatesNfa[i], inputs[j])) {
                    result += foundState + " ";
                }
                dataArray[i][j+1] = result;
            }
        }

        stateTable.getColumns().addAll(stateCol, inputCol1, inputCol2);

        stateCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[0]));
        inputCol1.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[1]));
        inputCol2.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[2]));


        stateTable.setItems(FXCollections.observableArrayList(dataArray));
    }

    @FXML
    void onTestInputButton(ActionEvent event) throws IOException {

        stateTable.getItems().clear();
        stateTable.getColumns().clear();

        testArea.setVisible(true);
        resultArea.setVisible(true);
        executeButton.setVisible(true);

        faInfo.setVisible(false);

        testArea.clear();
        resultArea.clear();
    }

    @FXML
    void executeTest(ActionEvent event) throws IOException {

        resultArea.clear();

        String testInput = testArea.getText();
    
        String[] lines = testInput.split("\n");

        FiniteAutomat fa = new FiniteAutomat();
        List<Boolean> resultList = new ArrayList<>();

        for (String line : lines) {
            boolean testResult = fa.testInput(line, this.min_DFA);
            resultList.add(testResult);
        }

        for (boolean testResult : resultList) {
            resultArea.appendText(testResult + "\n");
        }
        
    }

    List<String> readFile(File file) throws IOException {

        List<String> textLines = Files.readAllLines(file.toPath());
        List<String> ruleStringList = new ArrayList<>();

        //Symbol for EPSILON
        final char EPSILON = '\u03B5';

        //System.out.println("-----I AM A LINE-----");

        //Turn the the content from 'regularGrammar.txt' content into
        //Each grammar rule per line
        for (String line : textLines) {
            String ruleString = line; //.replace("->", "::=");
            //System.out.println(ruleString);
            ruleStringList.add(ruleString);
        }
        this.grammarRules=RegularGrammar.parseGrammar(ruleStringList);

        return ruleStringList;


    }
}


