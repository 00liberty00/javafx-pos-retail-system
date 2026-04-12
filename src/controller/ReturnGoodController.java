/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ml.dialog.DialogAlert;
import ml.model.Check;
import ml.model.CheckList;
import ml.model.Goods;
import ml.model.UserSwing;
import ml.query.check.AddCheck;
import ml.query.check.AddCheckList;
import ml.query.check.LastCheck;
import ml.query.goods.GoodByCode;
import ml.query.goods.UpdateResidueGood;
import ml.query.user.IdUserByName;
import ml.table.CheckTable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Возврат товара FXML Controller class
 *
 * @author Dave
 */
public class ReturnGoodController implements Initializable {

    @FXML
    private TextField sumCheck;
    @FXML
    private TextField amount;
    @FXML
    private TextField note;
    @FXML
    private Label checkLabel;
    @FXML
    private Label amountLabel;
    @FXML
    private Label numCheckLabel;
    @FXML
    private Button okButton;

    private Stage dialogStage;
    private ObservableList<CheckTable> checkData = FXCollections.observableArrayList();
    private CheckList cl = new CheckList();
    private AddCheck addCheck = new AddCheck();
    private DialogAlert alert = new DialogAlert();
    private BigDecimal discount;
    private LastCheck lastCheck = new LastCheck();
    private AddCheckList addCheckList = new AddCheckList();
    private BigDecimal price;
    private Boolean checkBank;

    @FXML
    private void okReturn(ActionEvent event) {
        UpdateResidueGood updateResidueGood = new UpdateResidueGood();
        Check check = new Check();
        CheckList checkList = new CheckList();
        Goods goods = new Goods();
        UserSwing userSwing = new UserSwing();
        IdUserByName idUserByName = new IdUserByName();
        Authentication authentication = SecurityContextHolder.getContext().
                getAuthentication();
        String login = authentication.getName();
        GoodByCode goodByCode = new GoodByCode();
        Date date = new Date();

        if (!"".equals(note.getText())) {
            BigDecimal sum = new BigDecimal("0.00").subtract(new BigDecimal(sumCheck.getText()));
            idUserByName.setLoginUser(login);   //метод для idUser по логину
            userSwing = idUserByName.displayResult(); //Возвращает пользователя

            //Товар по коду из таблицы
            //goodByCode.setCode(cl.getGoods().getCode());
            //Прибыль с одной еденицы товара по коду
            BigDecimal profit = price.subtract(cl.getGoods().getPriceOpt());
            //Прибыль с одного еденицы товара * кол-во
            profit = new BigDecimal("0.00").subtract(profit.multiply(new BigDecimal(amount.getText())));

            check.setDate(date);
            check.setNote("Возврат товара: " + note.getText());
            userSwing.getChecks().add(check);
            check.setUserSwing(userSwing);
            check.setMoney(new BigDecimal("0.00"));
            check.setSum(sum);
            check.setBank(this.checkBank);
            addCheck.add(check);

            lastCheck.get();                    //метод для номера последней записи в Check (Последний номер чека)
            //Товар по коду из таблицы
            goodByCode.setCode(cl.getGoods().getCode());
            checkList.setGoods(goodByCode.displayResult());
            goods.getCheckLists().add(checkList);
            checkList.setAmount(new BigDecimal("0.00").subtract(new BigDecimal(amount.getText().toString())));
            checkList.setProfit(profit);
            checkList.setCheck(lastCheck.displayResult());
            checkList.setNewPrice(false);
            check.getCheckLists().add(checkList);
            addCheckList.add(checkList);

            updateResidueGood.update(checkList, null, null, false);

            dialogStage.close();
        } else {
            alert.alert("Внимание!", "Поле описание пустое", "Введите описание");
        }

    }

    @FXML
    private void getSum() {
        //BigDecimal sumReturn = cl.getGoods().getPrice().multiply(new BigDecimal(amount.getText()));
        BigDecimal sumReturn;
        int i = discount.compareTo(new BigDecimal("0.00"));
        if (i == 1) {
            price = (cl.getGoods().getPrice().subtract(cl.getGoods().getPrice().multiply(discount.divide(new BigDecimal(100)))));
            sumReturn = price.multiply(new BigDecimal(amount.getText()));
        } else {
            price = cl.getGoods().getPrice();
            sumReturn = price.multiply(new BigDecimal(amount.getText()));
        }
        sumCheck.setText(sumReturn.toString());
    }

    public void getValue(List<CheckList> checkList, Integer numberCheck, String name, BigDecimal discount, Boolean checkBank) {

        this.checkBank = checkBank;
        this.discount = discount;

        numCheckLabel.setText(numberCheck.toString());
        checkLabel.setText(name);
        for (int i = 0; i < checkList.size(); i++) {
            if (checkList.get(i).getGoods().getName().equals(name)) {
                cl = checkList.get(i);
                amount.setText(cl.getAmount().toString());
            }
        }
        // BigDecimal sumReturn = cl.getGoods().getPrice().multiply(cl.getAmount());
        //BigDecimal priceWithsPercent = cr.getGoods().getPrice().subtract(cr.getGoods().getPrice().multiply(discount.divide(new BigDecimal(100))));
        BigDecimal sumReturn = new BigDecimal("0.00");
        int i = discount.compareTo(new BigDecimal("0.00"));
        if (i == 1) {
            price = (cl.getGoods().getPrice().subtract(cl.getGoods().getPrice().multiply(discount.divide(new BigDecimal(100)))));
            sumReturn = price.multiply(new BigDecimal(amount.getText()));
        } else {
            price = cl.getGoods().getPrice();
            sumReturn = price.multiply(new BigDecimal(amount.getText()));
        }

        //BigDecimal sumReturn = cl.getCheck().get;
        sumCheck.setText(sumReturn.toString());

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

                amount.requestFocus();

            }
        });
    }
}
