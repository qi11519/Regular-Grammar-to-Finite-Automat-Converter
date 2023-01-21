package com.example.rgtonfa;


import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.example.rgtonfa.JavaFileImport.findNFAResult;
import static com.example.rgtonfa.JavaFileImport.findNFAwithoutEpsilonResult;


public class mainScreenController {

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


    FiniteAutomat finite_automat;
    List<RegularGrammar> grammarRules;

    //Symbol for EPSILON
    final char EPSILON = '\u03B5';

    TableColumn<String[], String> stateCol = new TableColumn<>("State");
    TableColumn<String[], String> inputCol1 = new TableColumn<>("0");
    TableColumn<String[], String> inputCol2 = new TableColumn<>("1");

    TableColumn<String[], String> inputCol3 = new TableColumn<>("3");

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

        String text = inputArea.getText();
        String[] lines = text.split("\n");

        try {
            FileWriter fw = new FileWriter("regularGrammar.txt");
            BufferedWriter bw = new BufferedWriter(fw);

            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }

            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @FXML
    void onClearClick(ActionEvent event) {
        inputArea.clear();

    }
    //use to draw


    @FXML
    void onNfaButtonClick(ActionEvent event) throws IOException {
        readFile();

        stateTable.getItems().clear();
        stateTable.getColumns().clear();

        //add column to table
        stateTable.getColumns().addAll(stateCol, inputCol1, inputCol2,inputCol3);


        StringBuilder sb = new StringBuilder();
        for (RegularGrammar rule : grammarRules) {
            sb.append(rule.nonterminal);
            System.out.println(rule.nonterminal);
        }
        char[] states = sb.toString().toCharArray();
        Set<Character> statesSet = new HashSet<>();
        for (char state : states) {
            statesSet.add(state);
        }
        char[] uniqueStates = new char[statesSet.size()];
        int a = 0;
        for (char state : statesSet) {
            uniqueStates[a] = state;
            a++;
        }
        char[] inputs = {'0', '1', EPSILON};


        String[][] dataArray = new String[uniqueStates.length][inputs.length+1];

        for (int i = 0; i < uniqueStates.length; i++) {
            dataArray[i][0] = Character.toString(uniqueStates[i]);
            for (int j = 0; j < inputs.length; j++) {
                dataArray[i][j+1] = findNFAResult(finite_automat, uniqueStates[i], inputs[j]);
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
        readFile();

        stateTable.getItems().clear();
        stateTable.getColumns().clear();

        //List<RegularGrammar> grammarRules = RegularGrammar.parseGrammar(readFile());
        //For the purpose of calling the function
        //from the 'NondeterministicFiniteAutomaton' class
        FiniteAutomat fa = new FiniteAutomat();

        //Keep track of which state/rule being visited
        List<LinkedList<Character>> visitedStates = new ArrayList<LinkedList<Character>>();

        //Keep track of which state/rule being visited

        //Testing of a function "checkReachFinal()" where it will test
        //if a state can reach final state
        //NFA.states.get(1) ---IS---> State A
        //then it will test if A can reach final state


        //Templist for storing the list of states of NFA without Epsilon
        List<FiniteAutomat.State> StatesWithoutEpsilon = new ArrayList<FiniteAutomat.State>();

        //Templist that stored the states of NFA
        List<FiniteAutomat.State> oldStateList = finite_automat.states;

        //Convert states with EPSILON into without EPSILON, then add into the templist
        StatesWithoutEpsilon = fa.renewStates(finite_automat, oldStateList);


        //Replace the current list of states of the NFA, which make it become NFA without EPSILON
        finite_automat.setStateList(StatesWithoutEpsilon);

        for (FiniteAutomat.State state : finite_automat.states) {
            List<FiniteAutomat.Transition> toStateName = state.getTransition();

        }

        ////////////////////////////////////////////////////////////////////////////////////////////////
        //PRINT A TABLE FOR NFA WITHOUT EPSILON, and set data to java fx


        //here are place to give data dynamic

        StringBuilder sb = new StringBuilder();
        for (RegularGrammar rule : grammarRules) {
            sb.append(rule.nonterminal);
            System.out.println(rule.nonterminal);
        }
        char[] statesNfa = sb.toString().toCharArray();
        Set<Character> statesSet = new HashSet<>();
        for (char state : statesNfa) {
            statesSet.add(state);
        }
        char[] uniqueStatesNfa = new char[statesSet.size()];
        int a = 0;
        for (char state : statesSet) {
            uniqueStatesNfa[a] = state;
            a++;
        }
        char[] inputs = {'0', '1'};
        String[][] dataArray = new String[10][10];

        for (int i = 0; i < uniqueStatesNfa.length; i++) {


            dataArray[i][0] = Character.toString(uniqueStatesNfa[i]);
            for (int j = 0; j < inputs.length; j++) {

                String result = "";
                for (String foundState : findNFAwithoutEpsilonResult(finite_automat, uniqueStatesNfa[i], inputs[j])) {
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

        FiniteAutomat fa = new FiniteAutomat();
        //Templist for storing the list of states of NFA without Epsilon
        List<FiniteAutomat.State> dfaStates = new ArrayList<FiniteAutomat.State>();

        //Templist that stored the states of NFA
        List<FiniteAutomat.State> oldStateListwoEPSILON = finite_automat.states;

        dfaStates = fa.convertToDFA(finite_automat, oldStateListwoEPSILON);

        //Replace the current list of states of the NFA, which make it become NFA without EPSILON
        finite_automat.setStateList(dfaStates);


        int index =0;
        String[][] dataArray = new String[10][10];

        for (FiniteAutomat.State state : finite_automat.states) {
            // Create a new table row
            //TableRow<String[]> row = new TableRow<>();
           // String[] data = new String[3];
            dataArray[index][0] = state.getName();
            for (FiniteAutomat.Transition transition : state.getTransition()) {
                if (transition.getSymbol() == '0') {

                    if(transition.getToState().getName().equals("Z")){
                        System.out.println("fucker");
                        dataArray[index][1]="Z";
                    }
                    else{
                        dataArray[index][1] = transition.getToState().getName();

                    }
                } else if (transition.getSymbol() == '1') {
                    if(transition.getToState().getName()=="Z"){
                        dataArray[index][2] = "Z";
                    }
                    else {
                        dataArray[index][2] = transition.getToState().getName();
                    }
                }
            }
            index++;

        }
        stateTable.getColumns().addAll(stateCol, inputCol1, inputCol2);

        stateCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[0]));
        inputCol1.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[1]));
        inputCol2.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[2]));


        stateTable.setItems(FXCollections.observableArrayList(dataArray));


    }
    List<String> readFile() throws IOException {

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
        this.grammarRules=RegularGrammar.parseGrammar(ruleStringList);

        this.finite_automat=new FiniteAutomat(grammarRules);
        return ruleStringList;


    }


}


