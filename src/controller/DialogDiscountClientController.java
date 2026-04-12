/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ml.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ml.model.Discount;
import ml.query.clientsDiscount.ListClientsDiscount;
import ml.query.discount.NumberDiscount;
import ml.util.RegexpNameGoods;
import org.apache.commons.lang3.ArrayUtils;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class DialogDiscountClientController implements Initializable {

    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField clientTextField;
    @FXML
    private Text clientText;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button createClientButton;
    @FXML
    private TextField percentText;
    @FXML
    private TextField noteText;
    @FXML
    private Label clientIdTlgrmLabel;

    private boolean okClicked = false;
    private boolean okClickedDiscount = false;

    private Stage dialogStage;
    private RegexpNameGoods regexpNameGoods = new RegexpNameGoods();
    private ListClientsDiscount listClientsDiscount = new ListClientsDiscount();
    private List<Discount> clientsList;
    private Discount discount = new Discount();
    private String oldDiscount = "0";
    private String note = "0";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                okClickedDiscount = false;
//                dialogStage.setAlwaysOnTop(true);

                //Список мастеров
                //Список Клиентов
                clientsList = listClientsDiscount.listDiscount();

                String[] sName = new String[clientsList.size()];
                String[] sCode = new String[clientsList.size()];
                for (int i = 0; i < clientsList.size(); i++) {
                    sName[i] = (String) clientsList.get(i).getNumcard() + " | " + clientsList.get(i).getName() + " | " + clientsList.get(i).getPhone();
                    // sCode[i] = (String) clientsList.get(i).getPhone();
                }
//                String[] both = (String[]) ArrayUtils.addAll(sName, sCode);
                String[] both = (String[]) ArrayUtils.addAll(sName);

                Arrays.sort(both);

                //Выводит список клиентов по имени и id
                listClientsByNameAndPhone(both);
                // TODO

                //Включение диалоговых окон по нажатию клавиш
                borderPane.setOnKeyPressed(
                        event -> {
                            switch (event.getCode()) {
                                case F2:
                                    break;
                                case F3:
//                                    if (event.isControlDown()) {
//                                    }
                                    break;
                                case F4:
                                    break;
                                case F5:
                                    break;
                                case F10:
//                                    if (event.isControlDown()) {
//                                    }
                                    break;
                                case ESCAPE:
                                    dialogStage.close();
                                    break;

                                case F:
                                    if (event.isControlDown()) {
                                        //getDialogFavofite();
                                    }
                                    break;
                                case HOME:
                                    break;
                                case DELETE:

                            }
                        });

            }
        });
    }

    public boolean displayOkClicked() {

        return okClickedDiscount;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public Discount getDiscount() {
        return this.discount;
    }

    public String oldDiscount() {
        return this.oldDiscount;
    }

    public String getNoteText() {
        return this.note;
    }

    /**
     * Поиск клиента по номеру или имени
     */
    private void listClientsByNameAndPhone(String[] both) {

        TextFields.bindAutoCompletion(clientTextField, both).setVisibleRowCount(15);

    }

    private void getClient(String numberCard) {
        NumberDiscount nd = new NumberDiscount();
        nd.numberDiscount(numberCard);
        this.discount = nd.displayResult();
    }

    /**
     * Выводит инфу о клиенте
     *
     * @param event
     */
    @FXML
    private void getClientTextField(ActionEvent event) {

        String text = clientTextField.getText();

        int index1 = text.indexOf('|'); // 2
        String newText = text.substring(0, index1 - 1); //lo

        if (!newText.equals("")) {

            getClient(newText);

            clientText.setText("Клиент: "
                    + this.discount.getName() + " | " + this.discount.getNote());
            percentText.setText(this.discount.getPercent());

            this.oldDiscount = this.discount.getPercent();

            if (this.discount.getTelegramIdChat() != null) {
//                System.out.println("ID_CHAT: " + this.discount.getTelegramIdChat());
                clientIdTlgrmLabel.setText("Клиент подписан на Телеграм-бота");
            } else {
                clientIdTlgrmLabel.setText("Клиент НЕ подписан на Телеграм-бота");

            }
//            //Проверка первого символа
//            if (regexpNameGoods.nameGoods(newText) == true) {
//                // getNameClient(newText);
//            } else {
////                getPhoneClient(newText);
//            }
        }

    }

    @FXML
    private void okButtonAction(ActionEvent event) {
        okClickedDiscount = true;
        this.note = noteText.getText();
        this.discount.setPercent(percentText.getText());
        dialogStage.close();

    }

    @FXML
    private void cancelButtonAction(ActionEvent event) {
        okClickedDiscount = false;
        dialogStage.close();
    }

    @FXML
    private void createClientButtonAction(ActionEvent event) {

        //Продажа товара
        try {

            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ml/view/LoyaltyCard.fxml"));
            AnchorPane rootLayout = (AnchorPane) loader.load();
            Stage stage = new Stage();
            stage.setTitle("Создание карты клиента");
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            //scene.getStylesheets().add("/styles/Styles.css");
            //stage.setMaximized(true);
            stage.setScene(scene);

            LoyaltyCardController controller = loader.getController();

            controller.setController(stage, clientTextField.getText());
            this.discount = controller.getDiscount();
            stage.showAndWait();

            okClicked = controller.displayOkClicked();

            if (okClicked == true) {
                clientText.setText("Клиент: "
                        + this.discount.getName() + " | " + this.discount.getNote()
                        + " | Скидка: " + this.discount.getPercent() + "%");
                percentText.setText(this.discount.getPercent());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
