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
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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


    //create a array
    String[][] dataArray = new String[5][5];

    //Symbol for EPSILON
    final char EPSILON = '\u03B5';


    TableColumn<String[], String> stateCol = new TableColumn<>("State");
    TableColumn<String[], String> inputCol1 = new TableColumn<>("0");
    TableColumn<String[], String> inputCol2 = new TableColumn<>("1");

    TableColumn<String[], String> inputCol3 = new TableColumn<>("3");






    public void initialize() throws IOException {


    }



    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    @FXML
    void onEpsilonClick(ActionEvent event) throws IOException {
        //displayDiagram();


    }

    @FXML
    void onArrowClick(ActionEvent event) throws IOException {
        //clearAndAdd();
        inputArea.appendText("→");

    }

    @FXML
    void onClearClick(ActionEvent event) {
        inputArea.clear();

    }
    //use to draw
//    void displayDiagram(){
//        Canvas canvas = new Canvas();
//        canvas.setWidth(300);
//        canvas.setHeight(300);
//        canvas.setLayoutX(40);
//        canvas.setLayoutY(40);
//        gc = canvas.getGraphicsContext2D();
//
//        //set line
//
//        gc.setStroke(Color.BLUE);
//        gc.setLineWidth(4);
//        //  gc.strokeLine(w,x,y,z);
//        // w and x represent the x and y coordinates of the start point of the line. y and z represent the x and y coordinates of the end point of the line.
//        gc.strokeLine(1,4,2, 10);
//
//        //set circle
//
//        gc.setStroke(Color.RED);
//        gc.setLineWidth(4);
//
//        gc.strokeOval(10, 20, 40, 40);
//
//
//        //text given
//        gc.setStroke(Color.BLACK);
//        gc.setLineWidth(1);
//        gc.setFont(new Font("Arial", 20));
//        gc.strokeText("Hello World!", 10, 50);
//
//
//        drawArea.getChildren().add(canvas);
//        primaryStage.show();
//    }

//    void clearAndAdd(){
//        stateTable.getItems().clear();
//        String[][] newDataArray = new String[][] {
//                {"D", "0", "E"},
//                {"E", "1", "F"},
//                {"F", "epsilon", "D"}
//        };
//        stateTable.getColumns().clear();
//// Create columns and set column titles
//        TableColumn<String[], String> stateCol = new TableColumn<>("State");
//        TableColumn<String[], String> inputCol = new TableColumn<>("Input");
//        TableColumn<String[], String> nextCol = new TableColumn<>("Next State");
//        stateTable.getColumns().addAll(stateCol, inputCol, nextCol);
//        stateCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[0]));
//        inputCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[1]));
//        nextCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[2]));
//        stateTable.setItems(FXCollections.observableArrayList(newDataArray));
//        stateTable.refresh();
//
//
//    }
    @FXML
    void onNfaButtonClick(ActionEvent event) throws IOException {
        stateTable.getItems().clear();
        stateTable.getColumns().clear();


        //Turn the content from 'regularGrammar.txt'
        //into a RegularGrammar class object
        List<RegularGrammar> grammarRules = RegularGrammar.parseGrammar(readFile());

        ////////////////////////////////////////////////////////////////////////////////////////////////
        //Convert RegularGrammar into NFA
        NondeterministicFiniteAutomaton NFA = new NondeterministicFiniteAutomaton(grammarRules);

        //add column to table
        stateTable.getColumns().addAll(stateCol, inputCol1, inputCol2,inputCol3);


    ///here are place to give data to it dynamic
        char[] states = {'A', 'B', 'C'};
        char[] inputs = {'0', '1', EPSILON};


        String[][] dataArray = new String[states.length][inputs.length+1];

        for (int i = 0; i < states.length; i++) {
            dataArray[i][0] = Character.toString(states[i]);
            for (int j = 0; j < inputs.length; j++) {
                dataArray[i][j+1] = findNFAResult(NFA, states[i], inputs[j]);
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
        List<RegularGrammar> grammarRules = RegularGrammar.parseGrammar(readFile());
        NondeterministicFiniteAutomaton NFA = new NondeterministicFiniteAutomaton(grammarRules);
        NondeterministicFiniteAutomaton n = new NondeterministicFiniteAutomaton();

        //Keep track of which state/rule being visited
        List<LinkedList<Character>> visitedStates = new ArrayList<LinkedList<Character>>();

        //Output of all state and its possible transition
        for (NondeterministicFiniteAutomaton.State state : NFA.states) {
            for (NondeterministicFiniteAutomaton.Transition transitions : state.transitions){
                System.out.println(state.getName() + " to " + transitions.toState.getName());
            }
        }

        //Testing of a function "checkReachFinal()" where it will test
        //if a state can reach final state
        //NFA.states.get(1) ---IS---> State A
        //then it will test if A can reach final state
        System.out.println(n.checkReachFinal(NFA.states.get(1), NFA, visitedStates));

        System.out.println("-----I AM A LINE-----");

        //Templist for storing the list of states of NFA without Epsilon
        List<NondeterministicFiniteAutomaton.State> StatesWithoutEpsilon = new ArrayList<NondeterministicFiniteAutomaton.State>();

        //Templist that stored the states of NFA
        List<NondeterministicFiniteAutomaton.State> oldStateList = NFA.states;

        //Convert states with EPSILON into without EPSILON, then add into the templist
        StatesWithoutEpsilon = n.renewStates(NFA, oldStateList);

        System.out.println("-----I AM A LINE-----");

        //Replace the current list of states of the NFA, which make it become NFA without EPSILON
        NFA.setStateList(StatesWithoutEpsilon);

        for (NondeterministicFiniteAutomaton.State state : NFA.states) {
            List<NondeterministicFiniteAutomaton.Transition> toStateName = state.getTransition();

            for (NondeterministicFiniteAutomaton.Transition transition : toStateName) {
                System.out.println("StartState: "+ state.getName() +", toState: "+ transition.toState.getName() +", Input: "+ transition.getSymbol());
            }
        }

        System.out.println("-----I AM A LINE-----");

        ////////////////////////////////////////////////////////////////////////////////////////////////
        //PRINT A TABLE FOR NFA WITHOUT EPSILON, and set data to java fx
        for (int i = 0; i < 3; i++) {
            System.out.print("+");
            for (int j = 0; j < 7 ; j++) {
                System.out.print("-");
            }
        }
        System.out.println("+");

        System.out.print("| ");
        System.out.printf("%-5s ", "XXX");
        System.out.print("| ");
        System.out.printf("%-5s ", "0");
        System.out.print("| ");
        System.out.printf("%-5s ", "1");
        System.out.println("| ");

        for (int i = 0; i < 3; i++) {
            System.out.print("+");
            for (int j = 0; j < 7 ; j++) {
                System.out.print("-");
            }
        }
        System.out.println("+");

        //here are place to give data dynamic

        char[] states = {'A', 'B'};
        char[] inputs = {'0', '1'};

        for (int i = 0; i < states.length; i++) {

            System.out.printf("|%-5s  ", states[i]);
            dataArray[i][0] = Character.toString(states[i]);
            for (int j = 0; j < inputs.length; j++) {
                System.out.print("| ");
                String result = "";
                for (String foundState : findNFAwithoutEpsilonResult(NFA, states[i], inputs[j])) {
                    result += foundState + " ";
                    System.out.printf("%-2s    ", foundState);
                }
                dataArray[i][j+1] = result;
            }
            System.out.println("| ");
            for (int k = 0; k < 3; k++) {
                System.out.print("+");
                for (int l = 0; l < 7 ; l++) {
                    System.out.print("-");
                }
            }
            System.out.println("+");
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////

        System.out.println("-----I AM A LINE-----");


        stateTable.getItems().clear();
        stateTable.getColumns().clear();
        stateTable.getColumns().addAll(stateCol, inputCol1, inputCol2);

        stateCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[0]));
        inputCol1.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[1]));
        inputCol2.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[2]));

        stateTable.setItems(FXCollections.observableArrayList(dataArray));





    }
    List<String> readFile() throws IOException {
        List<String> textLines = Files.readAllLines(Paths.get("regularGrammar.txt"));
        List<String> ruleStringList = new ArrayList<>();

        //Turn the the content from 'regularGrammar.txt' content into
        //Each grammar rule per line
        for (String line : textLines) {
            String ruleString = line; //.replace("->", "::=");
            System.out.println(ruleString);
            ruleStringList.add(ruleString);
        }
        return ruleStringList;


    }
    @FXML
    void onBtnImport(ActionEvent event) throws IOException {
        List<String> ruleStringList = readFile();

        for (String rule : ruleStringList) {
            inputArea.appendText(rule + "\n");
        }
    }


}


