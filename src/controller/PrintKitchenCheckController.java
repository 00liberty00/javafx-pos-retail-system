/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import ml.model.Check;
import ml.model.CheckList;
import ml.print.Print;
import ml.xml.XMLPrinter;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class PrintKitchenCheckController implements Initializable {

    @FXML
    private GridPane checkPane;

    private Stage dialogStage;
    private boolean okClicked = false;
    private List<Check> checkArrayList = new ArrayList<Check>();
    private List<CheckList> checkListArrayList = new ArrayList<CheckList>();
    private Print p = new Print();
    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final Date date = new Date();
    private final String nowDate = dateFormat.format(date);
    private XMLPrinter xmlPrinter = new XMLPrinter();

    public boolean displayOkClicked() {

        return okClicked;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setPrintKitchenCheck(List<CheckList> checkListArrayList, List<Check> checkArrayList) {
        // set text from another class

        Label headerLabel = new Label("Сервисный чек");
        Label underline1Label = new Label("________________");
        Label underline2Label = new Label("________________");
        Label underline3Label = new Label("________________");

        Label numberCheckLabel = new Label();
        Label sumLabel = new Label();
        Label sumCheckLabel = new Label();
        Label discountLabel = new Label();
        Label discountCheckLabel = new Label();
        Label discountCheckTextLabel = new Label();
        Label cashLabel = new Label();
        Label cashText = new Label();
        Label shortChangeLabel = new Label();
        Label shortChangeText = new Label();
        Label dateTextLabel = new Label();
        Label casier = new Label();
        int i = 0;

        headerLabel.setFont(new Font("Arial", 12));
        checkPane.add(headerLabel, 0, i);

        casier.setFont(new Font("Arial", 8));
        checkPane.add(casier, 0, ++i);

        underline1Label.setFont(new Font("Arial", 8));
        checkPane.add(underline1Label, 0, ++i);

        for (CheckList check1 : checkListArrayList) {

            Label name = new Label();
            Label aboutBuy = new Label();
            Label price = new Label();
            Label sum = new Label();

            i = ++i;

            name.setText(check1.getGoods().getName());
            name.setFont(new Font("Arial", 12));
            aboutBuy.setText(check1.getAmount().toString());
            aboutBuy.setFont(new Font("Arial", 12));
            checkPane.setHalignment(aboutBuy, HPos.RIGHT);
            checkPane.add(name, 0, i);
            checkPane.add(aboutBuy, 0, ++i);

        }

        underline2Label.setFont(new Font("Arial", 8));
        checkPane.add(underline2Label, 0, ++i);

        dateTextLabel.setText(nowDate);
        dateTextLabel.setFont(Font.font("Arial", FontWeight.BOLD, 8));
        checkPane.setHalignment(dateTextLabel, HPos.RIGHT);
        checkPane.add(dateTextLabel, 0, ++i);

        numberCheckLabel.setText("Чека №: " + checkArrayList.get(0).getIdCheck());
        numberCheckLabel.setFont(new Font("Arial", 8));
        checkPane.add(numberCheckLabel, 0, ++i);

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                p.pageSetup(checkPane, dialogStage);

                if (p.getPrint() == true) {
                    dialogStage.close();
                }
            }
        });

    }

}
