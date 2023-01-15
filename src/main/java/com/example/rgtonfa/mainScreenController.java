package com.example.rgtonfa;


import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.rgtonfa.JavaFileImport.findNFAResult;


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

    //create a array
    String[][] dataArray = new String[5][5];


    TableColumn<String[], String> stateCol = new TableColumn<>("State");
    TableColumn<String[], String> inputCol1 = new TableColumn<>("0");
    TableColumn<String[], String> inputCol2 = new TableColumn<>("1");

    TableColumn<String[], String> inputCol3 = new TableColumn<>("3");






    public void initialize() throws IOException {
        List<String> textLines = Files.readAllLines(Paths.get("C:\\Users\\Vs\\Desktop\\tools\\java\\rgtonfa\\src\\main\\java\\com\\example\\rgtonfa\\regularGrammar.txt"));
        List<String> ruleStringList = new ArrayList<>();

        final char EPSILON = '\u03B5';
        /*
        // Print the contents of the text file
        for (String line : lines) {
            System.out.println(line);
        }*/

        System.out.println("-----I AM A LINE-----");

        //Turn the imported text file content into
        //each grammar rule per line
        for (String line : textLines) {
            String ruleString = line; //.replace("->", "::=");
            System.out.println(ruleString);
            ruleStringList.add(ruleString);
        }

        System.out.println("-----I AM A LINE-----");
        System.out.println(ruleStringList);

        List<RegularGrammar> grammarRules = RegularGrammar.parseGrammar(ruleStringList);
        //initialise data
        dataArray[0]= new String[]{"A", findNFAResult(grammarRules, 'A', "0"),  findNFAResult(grammarRules, 'A', "1"),findNFAResult(grammarRules, 'A', String.valueOf(EPSILON))};
        dataArray[1]=new String[]{"B", findNFAResult(grammarRules, 'B', "0"),  findNFAResult(grammarRules, 'B', "1"),findNFAResult(grammarRules, 'B', String.valueOf(EPSILON))};
        dataArray[2]=new String[]{"C", findNFAResult(grammarRules, 'C', "0"),  findNFAResult(grammarRules, 'C', "1"),findNFAResult(grammarRules, 'C', String.valueOf(EPSILON))};

        //add column to table
        stateTable.getColumns().addAll(stateCol, inputCol1, inputCol2,inputCol3);

        //give a target for column to read the data, 0 represent the column will read [x][0] data
        stateCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[0]));
        inputCol1.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[1]));
        inputCol2.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[2]));
        inputCol3.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue()[3]));

        //set data to table
        stateTable.setItems(FXCollections.observableArrayList(dataArray));

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

}


