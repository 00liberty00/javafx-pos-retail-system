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
import ml.dialog.DialogAlert;
import ml.json.JSONSettingsTelegram;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class TelegramSettingsController implements Initializable {

    @FXML
    private Button okButton;
    @FXML
    private TextField botTextField;
    @FXML
    private TextField idChatTextField;
    @FXML
    private TextField botNameTextField;

    private DialogAlert alert = new DialogAlert();

    private String tokenBot;
    private String idChat;
    private String nameBot;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        JSONSettingsTelegram jsonst = new JSONSettingsTelegram();
        jsonst.readTLGRMSettings();

        this.tokenBot = jsonst.tokenBot();
        this.idChat = jsonst.idChat();
        this.nameBot = jsonst.nameBot();
    }

    @FXML
    private void okButtonAction(ActionEvent event) {
        tokenBot = botTextField.getText();
        idChat = idChatTextField.getText();
        nameBot = botNameTextField.getText();
        JSONSettingsTelegram jsonst = new JSONSettingsTelegram();
        jsonst.writeTLGRMSettings(tokenBot, idChat, nameBot);

        alert.alert(null, null, "Настройки сохранены!");

    }

    public String tokenBot() {
        return tokenBot;
    }

    public String idChat() {
        return idChat;
    }
    
    public String nameBot() {
        return nameBot;
    }

}
