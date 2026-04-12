/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import ml.model.ChangeOptPriceGoods;
import ml.model.Goods;
import ml.query.goods.GoodByCode;
import ml.query.goods.UpdateGood;
import ml.table.ChangePriceGoodsTable;
import ml.util.EditableChangePriceBigDecimalTableCell;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class ChangePriceController implements Initializable {

    private Stage dialogStage;
    @FXML
    private TableView<ChangePriceGoodsTable> goodsTable;
    @FXML
    private TableColumn<ChangePriceGoodsTable, String> codeColumn;
    @FXML
    private TableColumn<ChangePriceGoodsTable, String> nameColumn;
    @FXML
    private TableColumn<ChangePriceGoodsTable, BigDecimal> oldPriceOptColumn;
    @FXML
    private TableColumn<ChangePriceGoodsTable, BigDecimal> nowPriceOptColumn;
    @FXML
    private TableColumn<ChangePriceGoodsTable, BigDecimal> priceColumn;

    private ObservableList<ChangePriceGoodsTable> allGoodData = FXCollections.observableArrayList();
    private List<ChangeOptPriceGoods> goodList;

    @FXML
    private void priceGoodEdit(TableColumn.CellEditEvent<ChangePriceGoodsTable, BigDecimal> event) {
        UpdateGood updateGood = new UpdateGood();
        GoodByCode goodByCode = new GoodByCode();
        Goods g = new Goods();
        //Обновление закупочной цены
        goodByCode.setCode(event.getRowValue().getCode());
        g = goodByCode.displayResult();
        g.setPrice(event.getNewValue());
        updateGood.update(g);

        System.out.println("Изменили на: " + event.getNewValue());
    }

    @FXML
    private void nameGoodEdit(TableColumn.CellEditEvent<ChangePriceGoodsTable, String> event) {
        UpdateGood updateGood = new UpdateGood();
        GoodByCode goodByCode = new GoodByCode();
        Goods g = new Goods();
        //Обновление названия
        goodByCode.setCode(event.getRowValue().getCode());
        g = goodByCode.displayResult();
        g.setName(event.getNewValue());
        updateGood.update(g);

        System.out.println("Изменили на: " + event.getNewValue());
    }

    public void setDialogStage(Stage dialogStage, List<ChangeOptPriceGoods> goodList) {
        this.dialogStage = dialogStage;
        this.goodList = goodList;
    }

    /**
     * Метод для просмотра результатов в "JTable".
     */
    private void displayResultGoods(ChangeOptPriceGoods g) {
        codeColumn.setCellValueFactory(new PropertyValueFactory<ChangePriceGoodsTable, String>("code"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<ChangePriceGoodsTable, String>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.<ChangePriceGoodsTable>forTableColumn());
        oldPriceOptColumn.setCellValueFactory(new PropertyValueFactory<ChangePriceGoodsTable, BigDecimal>("oldPriceOpt"));
        nowPriceOptColumn.setCellValueFactory(new PropertyValueFactory<ChangePriceGoodsTable, BigDecimal>("newPriceOpt"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<ChangePriceGoodsTable, BigDecimal>("price"));
        priceColumn.setCellFactory(col -> new EditableChangePriceBigDecimalTableCell<ChangePriceGoodsTable>());

        ChangePriceGoodsTable allGoodsTable = new ChangePriceGoodsTable();
        //for (Goods g : goodsList) {
        for (int i = 0; i < 1; i++) {

            allGoodsTable.setCode(g.getCode());
            allGoodsTable.setName(g.getName());
            allGoodsTable.setOldPriceOpt(g.getOldPriceOpt());
            allGoodsTable.setNewPriceOpt(g.getNewPriceOpt());
            allGoodsTable.setPrice(g.getPrice());

            allGoodData.add(allGoodsTable);
        }

        goodsTable.setItems(allGoodData);
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

                dialogStage.setAlwaysOnTop(true);

                goodList.forEach((g) -> {
                    displayResultGoods(g);
                });
            }
        });
    }

}
