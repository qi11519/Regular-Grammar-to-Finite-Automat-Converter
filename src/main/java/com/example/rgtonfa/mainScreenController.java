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
    void onEpsilonClick(ActionEvent event) {
        //displayDiagram();
        inputArea.appendText("ε");

    }

    @FXML
    void onArrowClick(ActionEvent event) throws IOException {
        clearAndAdd();
        inputArea.appendText("→");

    }

    @FXML
    void onClearClick(ActionEvent event) {
        inputArea.clear();

    }
    //use to draw
    void displayDiagram(){
        Canvas canvas = new Canvas();
        canvas.setWidth(300);
        canvas.setHeight(300);
        canvas.setLayoutX(40);
        canvas.setLayoutY(40);
        gc = canvas.getGraphicsContext2D();

        //set line

        gc.setStroke(Color.BLUE);
        gc.setLineWidth(4);
        //  gc.strokeLine(w,x,y,z);
        // w and x represent the x and y coordinates of the start point of the line. y and z represent the x and y coordinates of the end point of the line.
        gc.strokeLine(1,4,2, 10);

        //set circle

        gc.setStroke(Color.RED);
        gc.setLineWidth(4);

        gc.strokeOval(10, 20, 40, 40);


        //text given
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.setFont(new Font("Arial", 20));
        gc.strokeText("Hello World!", 10, 50);


        drawArea.getChildren().add(canvas);
        primaryStage.show();
    }

    void clearAndAdd(){
        stateTable.getItems().clear();
        String[][] newDataArray = new String[][] {
                {"D", "0", "E"},
                {"E", "1", "F"},
                {"F", "epsilon", "D"}
        };
        stateTable.getColumns().clear();
// Create columns and set column titles
        TableColumn<String[], String> stateCol = new TableColumn<>("State");
        TableColumn<String[], String> inputCol = new TableColumn<>("Input");
        TableColumn<String[], String> nextCol = new TableColumn<>("Next State");
        stateTable.getColumns().addAll(stateCol, inputCol, nextCol);
        stateCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[0]));
        inputCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[1]));
        nextCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[2]));
        stateTable.setItems(FXCollections.observableArrayList(newDataArray));
        stateTable.refresh();


    }
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

        dataArray[0]= new String[]{"A", findNFAResult(NFA, 'A', '0'), findNFAResult(NFA, 'A', '1'),findNFAResult(NFA, 'A', EPSILON)};
        dataArray[1]=new String[]{"B", findNFAResult(NFA, 'B', '0'), findNFAResult(NFA, 'B', '1'),findNFAResult(NFA, 'B', EPSILON)};
        dataArray[2]=new String[]{"C", findNFAResult(NFA, 'C', '0'), findNFAResult(NFA, 'C', '1'),findNFAResult(NFA, 'C', EPSILON)};


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
        //PRINT A TABLE FOR NFA WITHOUT EPSILON
        for (int i = 0; i < 3; i++) {
            System.out.print("+");
            for (int j = 0; j < 7 ; j++) {
                System.out.print("-");
            }
        }
        System.out.println("+");
        ///////////////////////////////////////////
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
        ///////////////////////////////////////////
        System.out.print("| ");
        System.out.printf("%-5s ", "A");
        System.out.print("| ");

        // create new A data
        dataArray[0][0]="A";
        //create a0 data
        String a0="";

        for (String foundState : findNFAwithoutEpsilonResult(NFA, 'A', '0')){
            if (findNFAwithoutEpsilonResult(NFA, 'A', '0').size() > 1){
                a0+=foundState+" ";


                System.out.printf("%-2s ", foundState, ",");

            } else {
                a0+=foundState+" ";
                //dataArray[0][1]=foundState;
                System.out.printf("%-5s ", foundState);
            }
        }
        dataArray[0][1]=a0;

        //create a1 data
        String a1="";
        System.out.print("| ");
        for (String foundState : findNFAwithoutEpsilonResult(NFA, 'A', '1')){
            if (findNFAwithoutEpsilonResult(NFA, 'A', '1').size() > 1){
                a1+=foundState+"    ";
                //dataArray[0][2]=foundState;
                System.out.printf("%-2s ", foundState, ",");
            } else {
               // dataArray[0][2]=foundState;
                a1+=foundState+" ";
                System.out.printf("%-5s ", foundState);
            }
        }
        dataArray[0][2]=a1;
        System.out.println("| ");

        for (int i = 0; i < 3; i++) {
            System.out.print("+");
            for (int j = 0; j < 7 ; j++) {
                System.out.print("-");
            }
        }
        System.out.println("+");
        ///////////////////////////////////////////
        System.out.print("| ");
        System.out.printf("%-5s ", "B");
        System.out.print("| ");


        //create B new data
        dataArray[1][0]="B";
        //create B0 data
        String b0="";

        for (String foundState : findNFAwithoutEpsilonResult(NFA, 'B', '0')){
            if (findNFAwithoutEpsilonResult(NFA, 'B', '0').size() > 1){
               // dataArray[1][1]=foundState;
                b0+=foundState+" ";
                System.out.printf("%-2s ", foundState);
            } else {
               // dataArray[1][1]=foundState;
                b0+=foundState+" ";
                System.out.printf("%-5s ", foundState);
            }

        }
        dataArray[1][1]=b0;


        //create b1 data
        String b1="";
        System.out.print("| ");
        for (String foundState : findNFAwithoutEpsilonResult(NFA, 'B', '1')){
            if (findNFAwithoutEpsilonResult(NFA, 'B', '1').size() > 1){
              //  dataArray[1][2]=foundState;
                b1+=foundState+" ";
                System.out.printf("%-2s ", foundState);
            } else {
                b1+=foundState+" ";
              //  dataArray[1][2]=foundState;
                System.out.printf("%-5s ", foundState);
            }
        }
        dataArray[1][2]=b1;
        System.out.println("| ");

        for (int i = 0; i < 3; i++) {
            System.out.print("+");
            for (int j = 0; j < 7 ; j++) {
                System.out.print("-");
            }
        }
        System.out.println("+");
        ///////////////////////////////////////////
        System.out.print("| ");
        System.out.printf("%-5s ", "C");
        System.out.print("| ");

        //create new C data
        dataArray[2][0]="C";
        String c0="";

        for (String foundState : findNFAwithoutEpsilonResult(NFA, 'C', '0')){
            if (findNFAwithoutEpsilonResult(NFA, 'C', '0').size() > 1){
               // dataArray[2][1]=foundState;
                c0+=foundState+" ";
                System.out.printf("%-2s ", foundState);
            } else {
               // dataArray[2][1]=foundState;
                c0+=foundState+" ";
                System.out.printf("%-5s ", foundState);
            }
        }
        dataArray[2][1]=c0;



        //create data c1
        String c1="";
        System.out.print("| ");
        for (String foundState : findNFAwithoutEpsilonResult(NFA, 'C', '1')){
            if (findNFAwithoutEpsilonResult(NFA, 'C', '1').size() > 1){
                c1+=foundState+" ";
                //dataArray[2][2]=foundState;
                System.out.printf("%-2s ", foundState);
            } else {
               // dataArray[2][2]=foundState;
                c1+=foundState+" ";
                System.out.printf("%-5s ", foundState);
            }
        }
        dataArray[2][2]=c1;
        System.out.println("| ");

        for (int i = 0; i < 3; i++) {
            System.out.print("+");
            for (int j = 0; j < 7 ; j++) {
                System.out.print("-");
            }
        }
        System.out.println("+");
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


}


