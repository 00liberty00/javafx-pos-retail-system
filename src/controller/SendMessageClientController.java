/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package ml.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import ml.dialog.DialogAlert;
import ml.dialog.DialogChoose;
import ml.model.Discount;
import ml.modelLicense.Comp;
import ml.modelLicense.Paytlgrm;
import ml.query.clientsDiscount.ListMessageClientsDiscount;
import ml.query.tlgrm.PayTLGRM;
import ml.query.tlgrm.UpdatePaytlgrm;
import ml.table.SendMessageClientTable;
import ml.telegram.BotClientCommand;
import ml.util.SendMessageClientActiveValueFactory;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class SendMessageClientController implements Initializable {

    @FXML
    private TableView<SendMessageClientTable> clientTable;
    @FXML
    private TableColumn<SendMessageClientTable, String> numberDiscountColumn;
    @FXML
    private TableColumn<SendMessageClientTable, String> clientNameColumn;
    @FXML
    private TableColumn<SendMessageClientTable, Long> idChatClientColumn;
    @FXML
    private TableColumn<SendMessageClientTable, Boolean> checkColumn;
    @FXML
    private TextArea textClientTextArea;
    @FXML
    private Text cashText;
    @FXML
    private Button okButton;
    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;
    @FXML
    private Button button4;
    @FXML
    private Button button5;
    @FXML
    private Button button6;
    @FXML
    private Button button7;
    @FXML
    private Button button8;
    @FXML
    private Button button9;

    private ObservableList<SendMessageClientTable> clientData = FXCollections.observableArrayList();
    private ListMessageClientsDiscount clientsDiscount = new ListMessageClientsDiscount();
    private List<Discount> discountClientList;
    SendMessageClientActiveValueFactory activeValueFactory = new SendMessageClientActiveValueFactory();
    private String textForClient = "";
    private DialogAlert dialogAlert = new DialogAlert();
    private Comp comp;
    private final PayTLGRM pay = new PayTLGRM();
    private Paytlgrm p = new Paytlgrm();

    /**
     * Список клиентов
     */
    private void listClients() {
        clientsDiscount.clearList();
        discountClientList = clientsDiscount.listDiscount();
        discountClientList.forEach((d) -> {
            displayClientResult(d);
        });
    }

    /**
     * Метод для просмотра результатов в "JTable".
     */
    private void displayClientResult(Discount d) {

        numberDiscountColumn.setCellValueFactory(new PropertyValueFactory<SendMessageClientTable, String>("numcard"));
        clientNameColumn.setCellValueFactory(new PropertyValueFactory<SendMessageClientTable, String>("fistName"));
        idChatClientColumn.setCellValueFactory(new PropertyValueFactory<SendMessageClientTable, Long>("idChatClient"));
        checkColumn.setCellValueFactory(activeValueFactory);

        SendMessageClientTable sendMessageClientTable = new SendMessageClientTable();
        for (int i = 0; i < 1; i++) {

            sendMessageClientTable.setNumcard(d.getNumcard());
            sendMessageClientTable.setFistName(d.getName());
            sendMessageClientTable.setIdChatClient(d.getTelegramIdChat());
            /*if ("ROLE_ADMIN".equals(auth.toString())) {
            reportsDayTable.setResidue(goods.getResidue());
            }*/
            // заполняем таблицу данными
            clientData.add(sendMessageClientTable);
        }
        clientTable.setItems(clientData);
    }

    @FXML
    private void okActionButton(ActionEvent event) {

        DialogChoose dialogChoose = new DialogChoose();

        BotClientCommand command = new BotClientCommand();

        Set<Long> l = activeValueFactory.getClientsList();

        if (comp.getTlgrm() == true) {
            if (!l.isEmpty()) {
                dialogChoose.alert("Отправка сообщения", "", "Отправить " + l.size() + " сообщение(ий)");
                boolean b = dialogChoose.display();
                if (b == true) {

                    for (Long f : l) {
                        //  Замена команды ENTER на ENTER в телеграмме
                        textForClient = textClientTextArea.getText().replaceAll("\n", "%0A");
                        //Если ДА
                        command.sendMessageForClient(textForClient, f);
                    }
                    getPay(l.size());
                }

            } else {
                dialogAlert.alert("Внимание!!!", "Не выбран клиент", "Выберите одного или более клиента");
            }

        } else {
            dialogAlert.alert("Внимание!!!", "", "У вас нет возможности отправлять сообщения");

        }
    }

    /**
     * Учет оплаты за отправку сообщения
     *
     * @param amountMessage
     */
    private void getPay(Integer amountMessage) {

        System.out.println("amount: " + amountMessage);

        UpdatePaytlgrm updatePay = new UpdatePaytlgrm();
        p = pay.displayResult();

        BigDecimal sum = p.getPrice().multiply(new BigDecimal(amountMessage));

        p.setCash(p.getCash().subtract(sum));
        p.setAmountMessage(p.getAmountMessage().add(new BigDecimal(amountMessage)));
        updatePay.update(p);

        pay.setComp(comp);
        cashText.setText(pay.displayResult().getCash().toString());
    }

    public void setSettings(Comp comp) {
        this.comp = comp;
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
                listClients();
                textClientTextArea.setWrapText(true);
                pay.setComp(comp);
                cashText.setText(pay.displayResult().getCash().toString());

                if (pay.displayResult().getCash().compareTo(BigDecimal.ZERO) < 0) {
                    textClientTextArea.setEditable(false);
                    dialogAlert.alert("Внимание!!!", "", "Не достаточно денег на счету");
                } else if (pay.displayResult().getCash().compareTo(BigDecimal.ZERO) == 0) {
                    textClientTextArea.setEditable(false);
                    dialogAlert.alert("Внимание!!!", "", "Не достаточно денег на счету");
                } else {

                }

            }
        });

        //Если нажата кнопка ENTER
//        textClientTextArea.setOnKeyPressed(event -> {
//            if (event.getCode() == KeyCode.ENTER) {
//                textForClient = textClientTextArea.getText() + "%0A";
//
//                System.out.println("_____________");
//
//                System.out.println(textForClient);
//            }
//        });
    }

    @FXML
    private void button1Action(ActionEvent event) {
        byte[] emojiByteCode = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x8D, (byte) 0x95};
        String emoji = new String(emojiByteCode, Charset.forName("UTF-8"));
        String text = textClientTextArea.getText() + emoji;
        textClientTextArea.setText(text);
    }

    @FXML
    private void button2Action(ActionEvent event) {
        byte[] emojiByteCode = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x8D, (byte) 0xA3};
        String emoji = new String(emojiByteCode, Charset.forName("UTF-8"));
        String text = textClientTextArea.getText() + emoji;
        textClientTextArea.setText(text);
    }

    @FXML
    private void button3Action(ActionEvent event) {

        byte[] emojiByteCode = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x94, (byte) 0xA5};
        String emoji = new String(emojiByteCode, Charset.forName("UTF-8"));
        String text = textClientTextArea.getText() + emoji;
        textClientTextArea.setText(text);
    }

    @FXML
    private void button4Action(ActionEvent event) {
        byte[] emojiByteCode = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x8D, (byte) 0x94};
        String emoji = new String(emojiByteCode, Charset.forName("UTF-8"));
        String text = textClientTextArea.getText() + emoji;
        textClientTextArea.setText(text);
    }

    @FXML
    private void button5Action(ActionEvent event) {
        byte[] emojiByteCode = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x84};
        String emoji = new String(emojiByteCode, Charset.forName("UTF-8"));
        String text = textClientTextArea.getText() + emoji;
        textClientTextArea.setText(text);
    }

    @FXML
    private void button6Action(ActionEvent event) {
        byte[] emojiByteCode = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x8D};
        String emoji = new String(emojiByteCode, Charset.forName("UTF-8"));
        String text = textClientTextArea.getText() + emoji;
        textClientTextArea.setText(text);
    }

    @FXML
    private void button7Action(ActionEvent event) {
        byte[] emojiByteCode = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x91, (byte) 0x8D};
        String emoji = new String(emojiByteCode, Charset.forName("UTF-8"));
        String text = textClientTextArea.getText() + emoji;
        textClientTextArea.setText(text);
    }

    @FXML
    private void button8Action(ActionEvent event) {
        byte[] emojiByteCode = new byte[]{(byte) 0xE2, (byte) 0x9C, (byte) 0x8C};
        String emoji = new String(emojiByteCode, Charset.forName("UTF-8"));
        String text = textClientTextArea.getText() + emoji;
        textClientTextArea.setText(text);
    }

    @FXML
    private void button9Action(ActionEvent event) {
        byte[] emojiByteCode = new byte[]{(byte) 0xE2, (byte) 0x98, (byte) 0x95};
        String emoji = new String(emojiByteCode, Charset.forName("UTF-8"));
        String text = textClientTextArea.getText() + emoji;
        textClientTextArea.setText(text);
    }

}
