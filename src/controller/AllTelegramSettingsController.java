/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ml.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import ml.dialog.DialogAlert;
import ml.json.JSONSettingsTelegram;
import ml.json.JSONSettingsTelegramClient;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class AllTelegramSettingsController implements Initializable {

    @FXML
    private TextField tokenBotTextField;
    @FXML
    private TextField idChatTextField;
    @FXML
    private TextField botNameTextField;
    @FXML
    private TextField tokenBotClientTextField;
    @FXML
    private TextField idChatClientTextField;
    @FXML
    private TextField botNameClientTextField;
    @FXML
    private TextField phoneCompanyTextField;
    @FXML
    private TextField siteCompanyTextField;
    @FXML
    private Button okButton;
    @FXML
    private Button okClientButton;

    @FXML
    private Tab botProgTabPane;
    @FXML
    private Tab botClientTabPane;

    private DialogAlert alert = new DialogAlert();

    private String tokenBot;
    private String idChat;
    private String nameBot;
    private String tokenBotClient;
    private String idChatClient;
    private String nameBotClient;
    private String phoneCompanyClient;
    private String siteCompanyClient;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        File file = new File("src/ml/resources/settings/telegramSettings.json");
        if (file.exists()) {
            JSONSettingsTelegram jsonst = new JSONSettingsTelegram();
            jsonst.readTLGRMSettings();

            this.tokenBot = jsonst.tokenBot();
            this.idChat = jsonst.idChat();
            this.nameBot = jsonst.nameBot();

            tokenBotTextField.setText(tokenBot);
            idChatTextField.setText(idChat);
            botNameTextField.setText(nameBot);
        }

        File f = new File("src/ml/resources/settings/telegramClientSettings.json");
        if (f.exists()) {
            JSONSettingsTelegramClient jsonCl = new JSONSettingsTelegramClient();
            jsonCl.readTLGRMSettings();
            this.tokenBotClient = jsonCl.tokenBot();
            this.idChatClient = jsonCl.idChat();
            this.nameBotClient = jsonCl.nameBot();
            this.phoneCompanyClient = jsonCl.phoneCompany();
            this.siteCompanyClient = jsonCl.siteCompany();

            tokenBotClientTextField.setText(tokenBotClient);
            idChatClientTextField.setText(idChatClient);
            botNameClientTextField.setText(nameBotClient);
            phoneCompanyTextField.setText(phoneCompanyClient);
            siteCompanyTextField.setText(siteCompanyClient);
        }
    }

    @FXML
    private void okButtonAction(ActionEvent event) {

        if (botProgTabPane.isSelected()) {

            tokenBot = tokenBotTextField.getText();
            idChat = idChatTextField.getText();
            nameBot = botNameTextField.getText();
            JSONSettingsTelegram jsonst = new JSONSettingsTelegram();
            jsonst.writeTLGRMSettings(tokenBot, idChat, nameBot);

            alert.alert(null, null, "Настройки сохранены!");
        }

    }

    @FXML
    private void okClientButtonAction(ActionEvent event) {
        if (botClientTabPane.isSelected()) {

            tokenBotClient = tokenBotClientTextField.getText();
            idChatClient = idChatClientTextField.getText();
            nameBotClient = botNameClientTextField.getText();
            phoneCompanyClient = phoneCompanyTextField.getText();
            siteCompanyClient = siteCompanyTextField.getText();
            JSONSettingsTelegramClient jsonst = new JSONSettingsTelegramClient();
            jsonst.writeTLGRMSettings(tokenBotClient, idChatClient, nameBotClient, phoneCompanyClient, siteCompanyClient);

            alert.alert(null, null, "Настройки сохранены!");
        }

    }

}
