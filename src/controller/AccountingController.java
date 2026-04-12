/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ml.dialog.DialogAlert;
import ml.json.JsonGoodsAccounting;
import ml.model.Accounting;
import ml.model.Goods;
import ml.model.GoodsAccounting;
import ml.query.accounting.AccountingQuery;
import ml.query.accounting.AddFirstSumGoodsInAccounting;
import ml.query.accounting.AllListGoodsAccounting;
import ml.query.accounting.BackupGoods;
import ml.query.accounting.CheckAccountingTable;
import ml.query.accounting.CopyGoodsToGoodsAccounting;
import ml.query.accounting.GoodAccountingByCode;
import ml.query.accounting.LastDateAccounting;
import ml.query.accounting.ListGoodsForAccounting;
import ml.query.accounting.NewSumGoods;
import ml.query.accounting.SpareMoneyDateByDate;
import ml.query.accounting.UpdateAccounting;
import ml.query.accounting.UpdateGoodsByNewResidue;
import ml.query.accounting.UpdateResidueGoodAccounting;
import ml.query.goods.GoodByCode;
import ml.query.goods.QueryAllGoodsList;
import ml.query.goods.SumGoods;
import ml.table.AccountingGoodsTable;
import org.apache.log4j.Logger;

/**
 * Класс учета товара
 *
 * @author Dave
 */
public class AccountingController implements Initializable {

    @FXML
    private TextField codeTextField;
    @FXML
    private TextField nameTextField;
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
    private TableView<AccountingGoodsTable> accountingTable;
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
    @FXML
    private CheckBox checkMode;

    private ObservableList<AccountingGoodsTable> accountingGoodsData = FXCollections.observableArrayList();
    private AccountingQuery accountingQuery = new AccountingQuery();
    private ListGoodsForAccounting listGoodsForAccounting = new ListGoodsForAccounting();
    private boolean startAccounting = false;
    private List<Goods> goodsForAccList;
    private List<GoodsAccounting> allGoodsAccList;
    private int selectRow = -1;
    private AllListGoodsAccounting allListGoodsAccounting = new AllListGoodsAccounting();
    private static final Logger log = Logger.getLogger(AccountingController.class);
    private DialogAlert dialogAlert = new DialogAlert();
    private BackupGoods bg = new BackupGoods();
    private CopyGoodsToGoodsAccounting cgtga = new CopyGoodsToGoodsAccounting();
    private Date lastDate;
    private JsonGoodsAccounting jg = new JsonGoodsAccounting();
    private QueryAllGoodsList allGoodsList = new QueryAllGoodsList();
    private HashMap<Integer, String> hash_map = new HashMap<Integer, String>();
    @FXML
    private Button loadGoodButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        log.info("Открыт учет");

        // Дата последнего учета
        LastDateAccounting lda = new LastDateAccounting();
        lda.setDate();
        lastDate = lda.getLastDateAccounting();
        String lastDateString = convertDate(lastDate);
        String nowDateString = convertDate(new Date());
        if (lastDateString.equals(nowDateString)) {
            lda.setPenultimateDate();
            lastDate = lda.getPenultimateLastDateAccounting();
        }

        startButton.setDisable(false);
        endButton.setDisable(true);
        exitButton.setDisable(false);
        //Таблица неактивна
        accountingTable.setDisable(true);
        codeTextField.setDisable(true);
        nameTextField.setDisable(true);
        codeTextField.requestFocus();
        /*        } else {
            log.info("Учет уже идет");
            
            checkMode.setDisable(false);
            startButton.setDisable(false);
            endButton.setDisable(false);
            exitButton.setDisable(true);
            //Таблица активна
            accountingTable.setDisable(false);
            startAccounting = true;
            codeTextField.setDisable(false);
            nameTextField.setDisable(false);
            codeTextField.selectAll();
            codeTextField.requestFocus();
            }*/

        //Копирует баз товара в таблицу для учета и в backup
        //Вывод в таблицу из GoodsAccounting
        cgtga.copyGoodsToGoodsAccounting();
        bg.backupGoods();

        allGoodsAccList = allListGoodsAccounting.allListGoodsForAccounting();
        allGoodsAccList.forEach((ga) -> {
            displayResult(ga);
        });

        // РАБОТА С ФАЙЛОМ JSON
//        jg.inJsonFileForAccounting(allGoodsList.listGoods());

        /*jg.getJsonFileForAllGoodsAccounting();
        allGoodsAccList = jg.displayResultAllGoodsForAccounting();
        allGoodsAccList.forEach((ga) -> {
        displayResult(ga);
        });*/
        sortAndFilter();
    }

    /**
     * Поиск по коду товара
     *
     * @param event
     */
    @FXML
    private void getCode() {
        /* FilteredList<AccountingGoodsTable> filteredData = new FilteredList<>(accountingGoodsData, p -> true);
        codeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
        filteredData.setPredicate(person -> {
        if (newValue == null || newValue.isEmpty()) {
        return true;
        }
        
        String lowerCaseFilter = newValue.toLowerCase();
        
        if (person.getCode().toLowerCase().contains(lowerCaseFilter)) {
        return true; // Filter matches first name.
        }
        
        return false; // Does not match.
        });
        });
        
        SortedList<AccountingGoodsTable> sortedData = new SortedList<>(filteredData);
        
        sortedData.comparatorProperty().bind(accountingTable.comparatorProperty());
        
        accountingTable.setItems(sortedData);*/

    }

    /**
     * Работа с полем Код Товара
     *
     * @param event
     */
    @FXML
    private void codeAction(ActionEvent event) {
        //подсчет кол-ва строк в таблице
        if (checkMode.isSelected()) {

//            JsonGoodsAccounting jga = new JsonGoodsAccounting();
            hash_map.put(hash_map.size() + 1, codeTextField.getText());
//            jga.setCodeForAccounting(codeTextField.getText());
            codeTextField.requestFocus();
            codeTextField.selectAll();

        } else {
            int countRow = accountingTable.getItems().size();
            int numberRow = 0;
            String codeFromTable = "";
            String code = codeTextField.getText();
            boolean b = false;

            for (int i = 0; i < countRow; i++) {
                codeFromTable = (String) accountingGoodsData.get(i).getCode();
                if (code.equals(codeFromTable)) {
                    b = true;
                    numberRow = i;
                }
            }

            //если код в таблице существует, то добавить к кол-ву +1 или +вес
            if (b == true) {
                //weight
                BigDecimal residueNewFromTable = accountingGoodsData.get(numberRow).getResidueNew();
                BigDecimal residueOldFromTable = accountingGoodsData.get(numberRow).getResidue();
                BigDecimal residueDiff;

                // +1 к числу в колонке ИТОГО
                BigDecimal residueNew = accountingGoodsData.get(numberRow).getResidueNew().add(new BigDecimal(1));

                accountingGoodsData.get(numberRow).setResidueNew(residueNew);

                // расчет разницы между "Остаток" и "Итого" и ввод в "Разница"
                residueDiff = residueOldFromTable.subtract(accountingGoodsData.get(numberRow).getResidueNew());
                accountingGoodsData.get(numberRow).setRedisueDifferent(residueDiff);
                //arrivalData.get(row).setNewPrice(allowance.multiply(invoice));s
                //accountingTable.getItems().set(numberRow, accountingGoodsData.get(numberRow));
                accountingTable.refresh();
                //метод +1 к сумме Итого

                getCodePlusOne(code, residueNew, residueDiff);

                codeTextField.selectAll();
                codeTextField.setText("");
                codeTextField.requestFocus();
            } else {
                log.info("Такой позиции нет: " + code);
                dialogAlert.alert("Внимание!!!", "", "Такой позиции нет");

                codeTextField.selectAll();
                codeTextField.requestFocus();
            }

            //показывает разницу в учете
            diffGoods();
        }
    }

    /**
     * Сортировка по коду и по названию товара
     *
     * @param event
     */
    /*@FXML
    private void getName() {
    // 1. Wrap the ObservableList in a FilteredList (initially display all data).
    FilteredList<AccountingGoodsTable> filteredData = new FilteredList<>(accountingGoodsData, p -> true);
    // 2. Set the filter Predicate whenever the filter changes.
    nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
    filteredData.setPredicate(good -> {
    // If filter text is empty, display all persons.
    if (newValue == null || newValue.isEmpty()) {
    return true;
    }
    
    // Compare first name and last name of every person with filter text.
    String lowerCaseFilter = newValue.toLowerCase();
    
    if (good.getCode().toLowerCase().contains(lowerCaseFilter)) {
    return true; // Filter matches first name.
    } else if (good.getName().toLowerCase().contains(lowerCaseFilter)) {
    return true; // Filter matches last name.
    }
    return false; // Does not match.
    });
    });
    
    // 3. Wrap the FilteredList in a SortedList.
    SortedList<AccountingGoodsTable> sortedData = new SortedList<>(filteredData);
    
    // 4. Bind the SortedList comparator to the TableView comparator.
    sortedData.comparatorProperty().bind(accountingTable.comparatorProperty());
    
    // 5. Add sorted (and filtered) data to the table.
    accountingTable.setItems(sortedData);
    
    }*/
    /**
     * При нажатии ENTER в поле НАЗВАНИЕ ИЛИ КОД ТОВАРА переход в ячейку ПО
     * ФАКТУ
     *
     * @param event
     */
    @FXML
    private void onEnter(ActionEvent ae) {

//4620032592104
        accountingTable.getFocusModel().focus(0, residueFactColumn);
        accountingTable.requestFocus();

    }

    /**
     * Начало учета
     *
     * @param event
     */
    @FXML
    private void startAccounting(ActionEvent event) {

        checkMode.setDisable(false);
        accountingTable.setDisable(false);
        startButton.setDisable(true);
        endButton.setDisable(false);
        exitButton.setDisable(false);
        startAccounting = true;
        codeTextField.setDisable(false);
        nameTextField.setDisable(false);
        codeTextField.requestFocus();

        setAccountingRecords();

        log.info("Учет начат");

    }

    @FXML
    private void checkMode(ActionEvent event) {

        if (checkMode.isSelected()) {
            hash_map.clear();
            startButton.setDisable(true);
            endButton.setDisable(true);
            exitButton.setDisable(false);
            //Таблица неактивна
            accountingTable.setDisable(true);
            codeTextField.setDisable(false);
            codeTextField.requestFocus();
            nameTextField.setDisable(true);

        } else {
            accountingTable.setDisable(false);
            startButton.setDisable(true);
            endButton.setDisable(false);
            exitButton.setDisable(false);
            codeTextField.requestFocus();
            codeTextField.setDisable(false);
            nameTextField.setDisable(false);

        }
    }

    /**
     * Кол-во товара по факту
     *
     * @param event
     */
    @FXML
    private void getAmountGood(TableColumn.CellEditEvent<AccountingGoodsTable, String> event) {

        String amountString = event.getNewValue();

        //Замена , на .
        String inv = amountString.replace(',', '.');
        BigDecimal residueDiff;
        BigDecimal amount = new BigDecimal(inv.trim());

        String code = event.getRowValue().getCode();

        nameTextField.setText("");
        nameTextField.requestFocus();

        int countRow = accountingTable.getItems().size();
        int numberRow = 0;

        for (int i = 0; i < countRow; i++) {
            if (code.equals(accountingGoodsData.get(i).getCode())) {
                numberRow = i;
            }
        }

        BigDecimal residueOld = accountingGoodsData.get(numberRow).getResidue();

        // + кол-во к числу в колонке ИТОГО
        BigDecimal residueNew = accountingGoodsData.get(numberRow).getResidueNew().add(amount);
        accountingGoodsData.get(numberRow).setResidueNew(residueNew);

        // расчет разницы между "Остаток" и "Итого" и ввод в "Разница"
        residueDiff = residueOld.subtract(residueNew);
        accountingGoodsData.get(numberRow).setRedisueDifferent(residueDiff);

        //System.out.println("selectRow ^ " + selectRow);
        /* System.out.println("numberRow ^ " + numberRow);
        System.out.println("residueNew ^ " + residueNew);
        System.out.println("residueOld ^ " + residueOld);
        System.out.println("amountFact ^ " + amount);
        System.out.println("residueDiff ^ " + residueDiff);
        System.out.println("code ^ " + code);*/
        accountingTable.refresh();
        //       accountingTable.getItems().set(numberRow, accountingGoodsData.get(numberRow));
        //selectRow = -1;

        getCodePlusOne(code, residueNew, residueDiff);
        //показывает разницу в учете
        diffGoods();
    }

    /**
     * Значение номера выбранной строки
     */
    @FXML
    private void getRow() {
        int i = accountingTable.getItems().size();   // кол-во строк в таблице
        if (i > 0) {
            //TablePosition pos = accountingTable.getSelectionModel().getSelectedCells().get(0);
            //selectRow = pos.getRow();
        }
    }

    /**
     * Запись в таблицу Accounting результатов учета
     *
     * @param event
     */
    @FXML
    private void endAccounting(ActionEvent event) {

        /* if (differenceSum <= 0) {
        Double differenceFromSumGoods = Double.parseDouble(sumNewGoodLabel.getText()) - Double.parseDouble(sumOldGoodLabel.getText()) + freeMoney;
        totalLabel.setText(differenceFromSumGoods.toString());
        } else if (freeMoney < 0) {
        {
        Double differenceFromSumGoods = -(Double.parseDouble(sumNewGoodLabel.getText()) - Double.parseDouble(sumOldGoodLabel.getText())) + freeMoney;
        totalLabel.setText(differenceFromSumGoods.toString());
        }
        } else {
        Double differenceFromSumGoods = -(Double.parseDouble(sumNewGoodLabel.getText()) - Double.parseDouble(sumOldGoodLabel.getText()));
        totalLabel.setText(differenceFromSumGoods.toString());
        }
         */
        Accounting accounting = new Accounting();

        CheckAccountingTable checkTable = new CheckAccountingTable();
        checkTable.test();

        if (checkTable.displayCheckLastSumGoods() == true) {
            dialogAlert.alert("Внимание!!!", "", "Вы учет не проводили!");

        } else {

            Alert closeConfirmation = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Вы уверены, что закончить учет?"
            );
            Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(
                    ButtonType.OK
            );
            Button cancelButton = (Button) closeConfirmation.getDialogPane().lookupButton(
                    ButtonType.CANCEL
            );
            exitButton.setText("Да");
            cancelButton.setText("Нет");
            closeConfirmation.setHeaderText("Конец учета");

            closeConfirmation.initModality(Modality.APPLICATION_MODAL);
            Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
            if (!ButtonType.OK.equals(closeResponse.get())) {
            } else {
                SumGoods sg = new SumGoods();
                NewSumGoods nsg = new NewSumGoods();
                BigDecimal fMoney = getFreeMoney();
                sg.sumGoods();
                nsg.sumNewGoods();

                BigDecimal diffSum = nsg.displayNewSum().subtract(sg.getSumGoods());

                accounting.setFreeMoney(fMoney);

                accounting.setDifferenceFromSumGoods(diffSum);
                accounting.setFirstSumGoods(sg.getSumGoods());
                accounting.setLastSumGoods(nsg.displayNewSum());

                updateAccontingRecord(accounting);
                //обнвление остатков
                updateResidue();
                //Закрывает окно
                Stage stage = (Stage) endButton.getScene().getWindow();
                stage.close();

            }

        }

    }

    @FXML
    private void exit(ActionEvent event) {
        //Закрывает окно
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Выгрузка из hashMap в БД
     *
     * @param event
     */
    @FXML
    private void loadGoodAction(ActionEvent event) {
        checkMode.setDisable(false);
        HashMap newHm = new HashMap();
        JsonGoodsAccounting jga = new JsonGoodsAccounting();

        for (int i = 1; i < hash_map.size() + 1; i++) {

            int counter = 0;
            String countingFor = hash_map.get(i);
            for (Integer key : hash_map.keySet()) {            // iterate through all the keys in this HashMap
                if (hash_map.get(key).equals(countingFor)) {  // if a key maps to the string you need, increment the counter
                    counter++;
                }
            }
            if (newHm.containsKey(countingFor)) {
                newHm.put(countingFor, counter);
            } else {
                newHm.put(countingFor, 1);
            }
//            System.out.println(countingFor + ":" + counter);
        }

        //Запись в json
        jga.inJsonFileLoad(newHm);

        for (Object name : newHm.keySet()) {
            String code = name.toString();
            String value = newHm.get(name).toString();
            System.out.println(code + " : " + value);

            int countRow = accountingTable.getItems().size();
            int numberRow = 0;
            String codeFromTable = "";
            boolean b = false;

            for (int i = 0; i < countRow; i++) {
                codeFromTable = (String) accountingGoodsData.get(i).getCode();
                if (code.equals(codeFromTable)) {
                    b = true;
                    numberRow = i;
                }
            }

            //если код в таблице существует, то добавить к кол-ву +1 или +вес
            if (b == true) {
                //weight
                BigDecimal residueNewFromTable = accountingGoodsData.get(numberRow).getResidueNew();
                BigDecimal residueOldFromTable = accountingGoodsData.get(numberRow).getResidue();
                BigDecimal residueDiff;

                // +value к числу в колонке ИТОГО
                BigDecimal residueNew = accountingGoodsData.get(numberRow).getResidueNew().add(new BigDecimal(value));

                accountingGoodsData.get(numberRow).setResidueNew(residueNew);

                // расчет разницы между "Остаток" и "Итого" и ввод в "Разница"
                residueDiff = residueOldFromTable.subtract(accountingGoodsData.get(numberRow).getResidueNew());
                accountingGoodsData.get(numberRow).setRedisueDifferent(residueDiff);
                //arrivalData.get(row).setNewPrice(allowance.multiply(invoice));s
                //accountingTable.getItems().set(numberRow, accountingGoodsData.get(numberRow));
                accountingTable.refresh();
                //метод +1 к сумме Итого

                getCodePlusOne(code, residueNew, residueDiff);

                codeTextField.selectAll();
                codeTextField.setText("");
                codeTextField.requestFocus();
            } else {
                log.info("Такой позиции нет: " + code);
                dialogAlert.alert("Внимание!!!", "", "Такой позиции нет");

                codeTextField.selectAll();
                codeTextField.requestFocus();
            }

            //показывает разницу в учете
            diffGoods();
        }
//        Iterator it = hash_map.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry) it.next();
//            System.out.println(pair.getKey() + " = " + pair.getValue());
//            if (newHm.containsKey(pair.getValue())) {
//                newHm.put(pair.getValue(), Integer.parseInt(newHm.get(pair.getValue()).toString()) + 1);
//            } else {
//                newHm.put(pair.getValue(), 1);
//            }
//            it.remove(); // avoids a ConcurrentModificationException
//        }
        hash_map.clear();

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
            accountingGoodsData.add(accGoodsTable);
        }

        accountingTable.setItems(accountingGoodsData);
    }

    /**
     * Сортировка по коду и по названию товара
     *
     * @param event
     */
    private void sortAndFilter() {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<AccountingGoodsTable> filteredData = new FilteredList<>(accountingGoodsData, p -> true);
        // 2. Set the filter Predicate whenever the filter changes.
        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(good -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (good.getCode().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (good.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<AccountingGoodsTable> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(accountingTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        accountingTable.setItems(sortedData);
    }

    /**
     * +1 к товару по коду и изменение в таблице SQL разницы
     */
    private void getCodePlusOne(String code, BigDecimal residueNew, BigDecimal residueDiff) {
        GoodByCode goodByCode = new GoodByCode();
        goodByCode.setCode(code);

        GoodAccountingByCode gabc = new GoodAccountingByCode();
        gabc.setCode(goodByCode.displayResult());
        UpdateResidueGoodAccounting updateResidueGoodAccounting = new UpdateResidueGoodAccounting();
        updateResidueGoodAccounting.update(gabc.displayResult(), residueNew, residueDiff);
    }

    /**
     * Запись данных в таблицу Accoutning
     */
    private void setAccountingRecords() {
        Accounting accounting = new Accounting();

        CheckAccountingTable checkTable = new CheckAccountingTable();
        checkTable.test();
        if (checkTable.displayResult() == true) {
            if (checkTable.displayCheckLastSumGoods() == true) {
                newAccontingRecord(accounting);

            } else {
                dialogAlert.alert("Внимание!!!", "", "Учет уже идет");

            }
            //accounting.setLastSumGoods(new BigDecimal(100001));
        } else {
            newAccontingRecord(accounting);
        }
    }

    /**
     * Метод на создание новой записи в таблице Accounting
     */
    private void newAccontingRecord(Accounting accounting) {
        AddFirstSumGoodsInAccounting firstSumGoodsInAccounting = new AddFirstSumGoodsInAccounting();
        SumGoods sg = new SumGoods();

        sg.sumGoods();

        accounting.setFirstSumGoods(sg.getSumGoods());
        accounting.setDate(new Date());

        firstSumGoodsInAccounting.add(accounting);
    }

    /**
     * Метод на обновление последней записи в таблице Accounting
     */
    private void updateAccontingRecord(Accounting accounting) {
        UpdateAccounting updateAccounting = new UpdateAccounting();

        //accounting.setDate(new Date());
        updateAccounting.update(accounting);
    }

    /**
     * Показывает разницу в товаре между началом и концом учета
     */
    private void diffGoods() {
        SumGoods sg = new SumGoods();
        NewSumGoods nsg = new NewSumGoods();

        sg.sumGoods();
        nsg.sumNewGoods();

        BigDecimal diffSum = nsg.displayNewSum().subtract(sg.getSumGoods());

        sumOldText.setText(sg.getSumGoods().toString());
        sumNewText.setText(nsg.displayNewSum().toString());
        differentText.setText(diffSum.toString());
    }

    /**
     * Конвертирование даты
     */
    private String convertDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    /**
     * Вывод лишних денег с момента последнего учета +1 день
     */
    private BigDecimal getFreeMoney() {

        SpareMoneyDateByDate smdbd = new SpareMoneyDateByDate();
        Calendar c = Calendar.getInstance();

        Date nowDate = new Date();

        c.setTime(lastDate);
        c.add(Calendar.DATE, 1); //same with c.add(Calendar.DAY_OF_MONTH, 1);
        lastDate = c.getTime();

        String lastDateString = convertDate(lastDate);
        String nowDateString = convertDate(nowDate);
        System.out.println("Первая дата: " + lastDateString);
        System.out.println("Ыторая дата: " + nowDateString);

        smdbd.setDates(lastDateString, nowDateString);
        System.out.println("Ответттттт: " + smdbd.displayResult());

        return smdbd.displayResult();
    }

    /**
     * Обновление остатков в таблице товаров после учета
     */
    private void updateResidue() {
//////////////////////////////////////////

        AllListGoodsAccounting lga = new AllListGoodsAccounting();
        List<GoodsAccounting> list = lga.allListGoodsForAccounting();
        UpdateGoodsByNewResidue ugbnr = new UpdateGoodsByNewResidue();

        for (int i = 0; i < list.size(); i++) {
            //System.out.print("Товар: " + list.get(i).getGoods().getName());
            //System.out.println(" : "+ list.get(i).getResidueNew());

            ugbnr.update(list.get(i));
        }

//////////////////////////////////////////
    }

}
