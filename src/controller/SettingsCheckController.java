/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ml.xml.XMLPrinter;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class SettingsCheckController implements Initializable {

    @FXML
    private TextField headerCheck;
    @FXML
    private TextField adressCheck;
    @FXML
    private TextField infoCheck;
    @FXML
    private TextField footerCheck;
    @FXML
    private Button ok;

    private XMLPrinter xmlPrinter = new XMLPrinter();
    private Stage dialogStage = new Stage();

    @FXML
    private void okButton(ActionEvent event) {

        String headerPrinter = headerCheck.getText();
        String adressPrinter = adressCheck.getText();
        String infoPrinter = infoCheck.getText();
        String footerPrinter = footerCheck.getText();
        xmlPrinter.newPrinter(headerPrinter, adressPrinter, infoPrinter, footerPrinter);
        //Закрывает окно
        Stage stage = (Stage) ok.getScene().getWindow();
        stage.close();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
