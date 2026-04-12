/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ml.config.ConfigDB;
import ml.query.created.CreateUserDB;
import ml.trial.TestTrial;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class CreateDBController implements Initializable {

    @FXML
    private ComboBox<String> chooseDBHost;
    @FXML
    private TextField url;
    @FXML
    private TextField port;
    @FXML
    private TextField userServer;
    @FXML
    private PasswordField passServer;
    @FXML
    private TextField user;
    @FXML
    private PasswordField pass;
    @FXML
    private Label message1;
    @FXML
    private Button ok;
    @FXML
    private Button cancel;

    private Stage dialogStage;
    private boolean chooseDB = true;

    @FXML
    private void handleOkAdmin(ActionEvent event) {
        String urlText = url.getText();
        String portText = port.getText();
        String userText = user.getText();
        String passText = pass.getText();
        String userServerText = userServer.getText();
        String passServerText = passServer.getText();

        ConfigDB configDB = new ConfigDB();
        try {
            configDB.conf(urlText, userText, passText, portText);
        } catch (Exception ex) {
            // Logger.getLogger(SettingsDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        CreateUserDB createUserDB = new CreateUserDB();
        //Выбор локал или удаленной базы
        if (chooseDB == true) {
            createUserDB.addLocalUser(urlText, portText, userServerText, passServerText, userText, passText);
        } else {
            createUserDB.addRemoteUser(urlText, portText, userText, passText);

        }

        TestTrial testTrial = new TestTrial();

        System.exit(0);
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void getTextComboBox() {
        // TODO add your handling code here:
        if ("Локальная база данных".equals(chooseDBHost.getValue())) {
            chooseDB = true;
            url.setEditable(false);
            url.setText("localhost");
            port.setText("3306");
            port.setEditable(false);
            userServer.setEditable(true);
            passServer.setEditable(true);
            userServer.setPromptText("root");
            passServer.setPromptText("Пароль");
            user.setPromptText("user");
            pass.setPromptText("Пароль");
        } else if ("Удаленная база данных".equals(chooseDBHost.getValue())) {
            chooseDB = false;
            url.setEditable(true);
            port.setEditable(true);
            url.setText("192.168.0.3");
            port.setText("3306");
            userServer.setText("");
            passServer.setText("");
            userServer.setPromptText("");
            passServer.setPromptText("");
            userServer.setEditable(false);
            passServer.setEditable(false);
            user.setPromptText("user");
            pass.setPromptText("Пароль");
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                ObservableList<String> options = FXCollections.observableArrayList(
                        "Локальная база данных",
                        "Удаленная база данных"
                );
                chooseDBHost.setItems(options);
                chooseDBHost.getSelectionModel().selectFirst();
            }
        });
    }

}
