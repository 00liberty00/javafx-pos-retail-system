/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ml.Ml_FX;
import ml.dialog.DialogAlert;
import ml.kkt.KKT;
import ml.kkt.KKTComPort;
import ml.model.UserSwing;
import ml.query.user.AdminUser;
import ml.query.user.PhoneUser;
import ml.query.user.UpdateAdmin;
import ml.util.UpdateApp;
import ml.xml.XMLSettings;
import ml.xml.model.Settings;

/**
 * Контроллер Настройки приложения
 *
 * @author Dave
 */
public class SettingsAppController implements Initializable {

    @FXML
    private ComboBox<BigDecimal> combo;
    @FXML
    private Button okButton;
    @FXML
    private TextField phoneTextField;
    @FXML
    private CheckBox smsCheck;
    @FXML
    private CheckBox tabletCheck;
    @FXML
    private CheckBox kitchenCheckBox;
    @FXML
    private CheckBox remoteKKTCheckBox;
    @FXML
    private Button updateButton;
    @FXML
    private ComboBox<String> comboComKKT;
    @FXML
    private Label nameKKT;
    @FXML
    private ComboBox<String> comboPrint;
    @FXML
    private Text ipAddressKKT;
    @FXML
    private Text portKKT;

    private XMLSettings xmls = new XMLSettings();
    private Settings settings = new Settings();
    private BigDecimal rounding;
    private DialogAlert alert = new DialogAlert();
    private PhoneUser phoneUser = new PhoneUser();
    private UpdateAdmin updatePhoneUser = new UpdateAdmin();
    private AdminUser adminUser = new AdminUser();
    private UserSwing userSwing = new UserSwing();
    private boolean selectCheck = false;
    private Stage primaryStage;

    @FXML
    private void okAction(ActionEvent event) {

        roundingPrice();
        selectCheck = smsCheck.isSelected();
        //если выбрана отправка смс
        if (selectCheck == true) {
            smsCheck();
        } else {
            settings.setSmsCheck(false);
        }
        comPort();
        ipAdressPort();
        printerCheck();
        xmls.newRecord(settings);

        alert.alert(null, null, "Настройки сохранены!");
    }

    @FXML
    private void update(ActionEvent event) {
        UpdateApp updateApp = new UpdateApp();
        updateApp.downloadApp();
    }

    /**
     * Выводит на экран название ККТ
     *
     * @param event
     */
    @FXML
    private void getComKKTCombo(ActionEvent event) {
        if (comboComKKT.getValue() != null) {
            KKT kkt = new KKT(comboComKKT.getValue());
            nameKKT.setText(kkt.getNameModel());
        }
    }

    @FXML
    private void getComboPrintAction(ActionEvent event) {
        String cp = comboPrint.getValue();
        if (cp.equals("Без печати")) {
            comboPrint.getSelectionModel().select("Без печати");
            comboComKKT.setDisable(true);
            comboComKKT.getItems().clear();
            remoteKKTCheckBox.setDisable(true);

        }

        if (cp.equals("ККТ (USB)")) {
            comboPrint.getSelectionModel().select("ККТ (USB)");
            getKKTCheck();
            comboComKKT.setDisable(false);
            remoteKKTCheckBox.setDisable(true);

        }
        if (cp.equals("ККТ (WI-FI)")) {
            comboPrint.getSelectionModel().select("ККТ (WI-FI)");
            dialogIPAddressKKTSettings();
            comboComKKT.setDisable(true);
            comboComKKT.getItems().clear();
            remoteKKTCheckBox.setDisable(false);
        }
        if (cp.equals("Принтер чеков")) {
            comboPrint.getSelectionModel().select("Принтер чеков");
            comboComKKT.setDisable(true);
            comboComKKT.getItems().clear();
            remoteKKTCheckBox.setDisable(true);

        }
    }

    /**
     * Диалоговое окно для выбора оплаты суммы накладной
     */
    private void dialogIPAddressKKTSettings() {

        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Ml_FX.class.getResource("/ml/view/KKTSettings.fxml"));
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Настройки ККТ");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            AnchorPane page = (AnchorPane) loader.load();
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            KKTSettingsController sumInvoiceController = loader.getController();
//
////            sumInvoiceController.setChoose(crList, bg2, kkt);
////
            sumInvoiceController.setDialogStage(dialogStage);

            dialogStage.showAndWait();
            ipAddressKKT.setText(sumInvoiceController.getIpAddress());
            portKKT.setText(sumInvoiceController.getPort());
//
//            KKT kkt = new KKT(comboComKKT.getValue());
//            nameKKT.setText(kkt.getNameModel());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Обработка округления цены товара
    private Settings roundingPrice() {
        int i0 = combo.getValue().compareTo(new BigDecimal("100.00"));
        int i1 = combo.getValue().compareTo(new BigDecimal("100.10"));
        int i2 = combo.getValue().compareTo(new BigDecimal("100.11"));
        if (i0 == 0) {
            rounding = new BigDecimal("0");
        }
        if (i1 == 0) {
            rounding = new BigDecimal("1");
        }
        if (i2 == 0) {
            rounding = new BigDecimal("2");
        }
        settings.setRounding(rounding);
        return settings;
    }

    //Обработка смс информирования
    private Settings smsCheck() {
        //Возвращает значение админа
        adminUser.get();
        userSwing = adminUser.displayResult();

        boolean selectCheck = smsCheck.isSelected();
        settings.setSmsCheck(selectCheck);

        userSwing.setPhone(phoneTextField.getText());
        updatePhoneUser.update(userSwing);
        return settings;
    }

    //Обработка ip адреса и порта
    private Settings ipAdressPort() {
        String cp = comboPrint.getValue();
        if (cp != null) {

            if (cp.equals("ККТ (WI-FI)")) {
                settings.setIpAddress(ipAddressKKT.getText());
                settings.setPort(portKKT.getText());
//                settings.setRemoteKKTCheck(remoteKKTCheckBox.isSelected());
            } else {
                settings.setIpAddress("");
                settings.setPort("");
//                settings.setRemoteKKTCheck(remoteKKTCheckBox.isSelected());

            }
        }
        return settings;

    }

    //Обработка значения com-порта
    private Settings comPort() {
        String cp = comboPrint.getValue();
        if (cp != null) {

            if (cp.equals("ККТ (USB)")) {
                String com = comboComKKT.getValue();
                settings.setKktCheck(com);
            } else {
                settings.setKktCheck("");
            }

            KKT kkt = new KKT(comboComKKT.getValue());
            nameKKT.setText(kkt.getNameModel());
        }
        return settings;
    }

    //Обработка значения печати принтера
    private Settings printerCheck() {
        String cp = comboPrint.getValue();
        if (cp != null) {

            settings.setPrinterCheck(cp);
            settings.setKitchenCheck(kitchenCheckBox.isSelected());
        }
        return settings;
    }

    //Выводит на экран выбранное раннее значение
    private void getComboValue() {

        BigDecimal defaultValue = xmls.getRounding();

        int i0 = defaultValue.compareTo(new BigDecimal("0"));
        int i1 = defaultValue.compareTo(new BigDecimal("1"));
        int i2 = defaultValue.compareTo(new BigDecimal("2"));
        if (i0 == 0) {
            combo.getSelectionModel().select(new BigDecimal("100.00"));

        }
        if (i1 == 0) {
            combo.getSelectionModel().select(new BigDecimal("100.10"));
        }
        if (i2 == 0) {
            combo.getSelectionModel().select(new BigDecimal("100.11"));

        }
    }

    //Выводит на экран выбранное раннее значение
    private void getSMSCheck() {

        boolean defaultValue = xmls.getSms();

        if (defaultValue) {
            smsCheck.setSelected(true);
            phoneUser.phoneUser();
            phoneTextField.setText(phoneUser.displayResult().toString());
            phoneTextField.setDisable(false);
        } else {
            smsCheck.setSelected(false);
            phoneTextField.setDisable(true);

        }
    }

    //Выводит на экран выбранное раннее значение
    private void getKitchenCheck() {

        boolean defaultValue = xmls.getPrinterKitchenCheck();

        if (defaultValue) {
            kitchenCheckBox.setSelected(true);

        } else {
            kitchenCheckBox.setSelected(false);

        }
    }

    //Выводит на экран выбранное раннее значение
    private void getKKTCheck() {

        KKTComPort kkt = new KKTComPort();
        String[] com = kkt.comPort();

        String defaultValue = xmls.getKKTCom();
        comboComKKT.setValue(defaultValue);
        for (int i = 0; i < com.length; i++) {
            comboComKKT.getItems().add(com[i]);
        }
    }

    //Выводит на экран выбранное раннее значение
    private void getRemoteKKTCheck() {

        boolean defaultValue = xmls.getRemoteKKTCheck();

        if (defaultValue) {
            remoteKKTCheckBox.setSelected(true);

        } else {
            remoteKKTCheckBox.setSelected(false);

        }
    }

    //Выводит на экран выбранное раннее значение печати принтера
    private void getComboPrintValue() {

        String defaultValue = xmls.getPrinter();

        int i0 = defaultValue.compareTo("Без печати");
        int i1 = defaultValue.compareTo("ККТ (WI-FI)");
        int i2 = defaultValue.compareTo("ККТ (USB)");
        int i3 = defaultValue.compareTo("Принтер чеков");
        if (i0 == 0) {
            comboPrint.getSelectionModel().select("Без печати");
        }
        if (i1 == 0) {
            comboPrint.getSelectionModel().select("ККТ (WI-FI)");
        }
        if (i2 == 0) {
            comboPrint.getSelectionModel().select("ККТ (USB)");
        }
        if (i3 == 0) {
            comboPrint.getSelectionModel().select("Принтер чеков");
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb
    ) {
        // TODO

        getComboValue();
        getSMSCheck();
        getKitchenCheck();
        getComboPrintValue();
        getKKTCheck();
        rounding = new BigDecimal("2");
        combo.getItems().add(new BigDecimal("100.00"));
        combo.getItems().add(new BigDecimal("100.10"));
        combo.getItems().add(new BigDecimal("100.11"));

        comboPrint.getItems().add("Без печати");
        comboPrint.getItems().add("ККТ (WI-FI)");
        comboPrint.getItems().add("ККТ (USB)");
        comboPrint.getItems().add("Принтер чеков");

        //phoneTextField.setDisable(true);
        smsCheck.setOnAction((event) -> {
            selectCheck = smsCheck.isSelected();
            if (selectCheck == true) {
                phoneTextField.setDisable(false);

            } else {
                phoneTextField.setDisable(true);

            }
        });
    }

}
