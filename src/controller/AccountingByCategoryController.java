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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import ml.model.CategoryGoods;
import ml.model.Goods;
import ml.model.GoodsAccounting;
import ml.query.accounting.BackupGoodsByCategory;
import ml.query.categoryGood.CategoryGoodList;
import ml.query.goods.GoodsListByCategory;
import ml.table.AccountingGoodsTable;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class AccountingByCategoryController implements Initializable {

    @FXML
    private TextField codeTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private ComboBox<String> categoryOfGoods;
    @FXML
    private Text sumOldText;
    @FXML
    private Text sumNewText;
    @FXML
    private Text differentText;
    @FXML
    private Text totalText;
    @FXML
    private Button startButton;
    @FXML
    private Button endButton;
    @FXML
    private Button exitButton;
    @FXML
    private TableView<AccountingGoodsTable> accountingByCategoryTable;
    @FXML
    private TableColumn<AccountingGoodsTable, String> codeColumn;
    @FXML
    private TableColumn<AccountingGoodsTable, String> nameColumn;
    @FXML
    private TableColumn<AccountingGoodsTable, BigDecimal> priceColumn;
    @FXML
    private TableColumn<AccountingGoodsTable, BigDecimal> residueColumn;
    @FXML
    private TableColumn<AccountingGoodsTable, String> residueFactColumn;
    @FXML
    private TableColumn<AccountingGoodsTable, BigDecimal> residueNewColumn;
    @FXML
    private TableColumn<AccountingGoodsTable, BigDecimal> redisueDifferentColumn;

    private String textComboBox;
    private List<CategoryGoods> categoryList;
    private CategoryGoodList categoryGoodList = new CategoryGoodList();
    private ObservableList<String> options = FXCollections.observableArrayList();
    private ObservableList<AccountingGoodsTable> accGoodData = FXCollections.observableArrayList();
    private List<GoodsAccounting> goodsAccountingList;
    private List<Goods> allGoodsAccList;

    private GoodsListByCategory goodsListByCategory = new GoodsListByCategory();
    private BackupGoodsByCategory bg = new BackupGoodsByCategory();
//    private CopyGoodsToGoodsAccounting cgtga = new CopyGoodsToGoodsAccounting();

    /**
     * Берет текст из ComboBox
     */
    @FXML
    private void catogoryOfGoodsAction(ActionEvent event) {
        textComboBox = categoryOfGoods.getValue();

//codeTextField.requestFocus();
        //codeTextField.setEditable(true);
        //nameTextField.setEditable(true);
        accGoodData.clear();
        for (CategoryGoods cg : categoryList) {

//            categoryOfGoods.getValue();

            if ((categoryOfGoods.getValue()).equals(cg.getName())) {
                //удаление строк в таблице
                for (int i = -1; i < accountingByCategoryTable.getItems().size(); i++) {
                    accountingByCategoryTable.getItems().clear();
                }
                bg.backupGoodsByCategory(cg);

//                allGoodsAccList = goodsListByCategory.listGoods(cg);
//                allGoodsAccList.forEach((g) -> {
//                    displayResult(g);
//                });
            }

        }
        categoryOfGoods.setDisable(true);
        startButton.setDisable(false);
    }

    @FXML
    private void codeAction(ActionEvent event) {
    }

    @FXML
    private void getCode(KeyEvent event) {
    }

    @FXML
    private void onEnter(ActionEvent event) {
    }

    @FXML
    private void startAccounting(ActionEvent event) {
    }

    @FXML
    private void endAccounting(ActionEvent event) {
    }

    @FXML
    private void exit(ActionEvent event) {
    }

    @FXML
    private void getAmountGood(TableColumn.CellEditEvent<AccountingGoodsTable, String> event) {
    }

    @FXML
    private void getRow(MouseEvent event) {
    }

    private void displayResult(GoodsAccounting ga) {
        codeColumn.setCellValueFactory(new PropertyValueFactory<AccountingGoodsTable, String>("code"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<AccountingGoodsTable, String>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<AccountingGoodsTable, BigDecimal>("price"));
        residueColumn.setCellValueFactory(new PropertyValueFactory<AccountingGoodsTable, BigDecimal>("residue"));
        residueFactColumn.setCellValueFactory(new PropertyValueFactory<AccountingGoodsTable, String>("residueFact"));
        residueFactColumn.setCellFactory(TextFieldTableCell.<AccountingGoodsTable>forTableColumn());
        residueNewColumn.setCellValueFactory(new PropertyValueFactory<AccountingGoodsTable, BigDecimal>("residueNew"));
        redisueDifferentColumn.setCellValueFactory(new PropertyValueFactory<AccountingGoodsTable, BigDecimal>("redisueDifferent"));

        AccountingGoodsTable accGoodsTable = new AccountingGoodsTable();
        //for (Goods g : goodsList) {
        for (int i = 0; i < 1; i++) {

            accGoodsTable.setCode(ga.getGoods().getCode());
            accGoodsTable.setName(ga.getGoods().getName());
            accGoodsTable.setPrice(ga.getGoods().getPrice());
            accGoodsTable.setResidue(ga.getGoods().getResidue());
            accGoodsTable.setResidueFact("0.00");
            accGoodsTable.setResidueNew(ga.getResidueNew());
            accGoodsTable.setRedisueDifferent(ga.getResidueDiff());
            accGoodData.add(accGoodsTable);
        }

        accountingByCategoryTable.setItems(accGoodData);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Список всего товара
        categoryList = categoryGoodList.getList();
        for (CategoryGoods cg : categoryList) {
            options.add(cg.getName());
        }

        categoryOfGoods.setItems(options);

        //Копирует баз товара в таблицу для учета и в backup
        //Вывод в таблицу из GoodsAccounting
//        cgtga.copyGoodsToGoodsAccounting();
        // TODO
    }

}
