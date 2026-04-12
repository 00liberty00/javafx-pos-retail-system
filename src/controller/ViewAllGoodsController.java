/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import ml.model.CategoryGoods;
import ml.model.Dish;
import ml.model.Goods;
import ml.query.categoryGood.AddCategoryGood;
import ml.query.categoryGood.CategoryGoodList;
import ml.query.dish.AllDishList;
import ml.query.dish.DishByCode;
import ml.query.dish.UpdateDish;
import ml.query.goods.DeleteGood;
import ml.query.goods.GoodByCode;
import ml.query.goods.GoodsListByCategory;
import ml.query.goods.QueryAllGoodsList;
import ml.query.goods.UpdateGood;
import ml.table.AllGoodsTable;
import ml.util.EditableBigDecimalTableCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class ViewAllGoodsController implements Initializable {

    @FXML
    private Button okButton;

    @FXML
    private TableView<AllGoodsTable> allGoodTable;
    @FXML
    private TableColumn<AllGoodsTable, String> codeGoodColumn;
    @FXML
    private TableColumn<AllGoodsTable, String> nameGoodColumn;
    @FXML
    private TableColumn<AllGoodsTable, BigDecimal> purchasePriceColumn;
    @FXML
    private TableColumn<AllGoodsTable, BigDecimal> sellingPriceColumn;
    @FXML
    private TableColumn<AllGoodsTable, BigDecimal> residueColumn;
    @FXML
    private ComboBox<String> categoryGood;
    @FXML
    private TextField searchGoodTextField;
    @FXML
    private MenuItem deleteGood;
    @FXML
    private CheckBox viewDishCheckBox;
    @FXML
    private Button unloadExcelButton;
    @FXML
    private Label sumGoodsLabel;

    private ObservableList<AllGoodsTable> allGoodData = FXCollections.observableArrayList();
    private QueryAllGoodsList allGoodsList = new QueryAllGoodsList();
    private AllDishList allDishList = new AllDishList();
    private List<Goods> goodsList;

    private List<Dish> dishList;

    private CategoryGoodList categoryGoodList = new CategoryGoodList();
    private List<CategoryGoods> categoryList;
    private String textComboBox;
    private CategoryGoods category = new CategoryGoods();
    private ObservableList<String> options = FXCollections.observableArrayList();
    private GoodsListByCategory goodsListByCategory = new GoodsListByCategory();
    private BigDecimal sumGoods = new BigDecimal("0.00");

    // private ObservableList<AllGoodsTable> goodsListData = FXCollections.observableArrayList();
    /**
     * Обновление кода товара
     *
     * @param event
     */
    @FXML
    private void codeGoodEdit(TableColumn.CellEditEvent<AllGoodsTable, String> event) {
        if (viewDishCheckBox.isSelected()) {
            // Здесь прописать код на обновление кода продукта
        } else {
            UpdateGood updateGood = new UpdateGood();
            GoodByCode goodByCode = new GoodByCode();
            Goods g = new Goods();
            //Обновление кода
            goodByCode.setCode(event.getOldValue());
            g = goodByCode.displayResult();
            g.setCode(event.getNewValue());
            updateGood.update(g);
        }
    }

    /**
     * Обновление названия товара
     *
     * @param event
     */
    @FXML
    private void nameGoodEdit(TableColumn.CellEditEvent<AllGoodsTable, String> event) {

        if (viewDishCheckBox.isSelected()) {
            // Здесь прописать код на обновление названия продукта
            UpdateDish updateDish = new UpdateDish();
            DishByCode dishByCode = new DishByCode();
            Dish d = new Dish();
            //Обновление закупочной цены
            dishByCode.setCode(event.getRowValue().getCode());
            d = dishByCode.displayResult();
            d.setName(event.getNewValue());
            updateDish.update(d);

            System.out.println("Изменили на: " + event.getNewValue());
        } else {
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

    }

    /**
     * Возвращает категорию товара
     *
     * @param event
     */
    @FXML
    private void categoryGoodAction(ActionEvent event) {

        allGoodData.clear();
        sumGoods = new BigDecimal(0.00);
        for (CategoryGoods cg : categoryList) {
            categoryGood.getValue();

            if ((categoryGood.getValue()).equals(cg.getName())) {
                //удаление строк в таблице
                for (int i = -1; i < allGoodTable.getItems().size(); i++) {
                    allGoodTable.getItems().clear();
                }

                goodsListByCategory.clearList();
                goodsList = goodsListByCategory.listGoods(cg);
                goodsList.forEach((g) -> {
                    displayResultGoods(g);
                });

                goodsList.forEach((g) -> {
                    displaySumGoods(g);
                });
                sumGoodsLabel.setText(sumGoods.toString());

            }

        }
        if (("Все категории").equals(categoryGood.getValue())) {
            //удаление строк в таблице
            for (int i = -1; i < allGoodTable.getItems().size(); i++) {
                allGoodTable.getItems().clear();
            }

            //allGoodsList.clearList();
            goodsList = allGoodsList.listGoods();
            goodsList.forEach((g) -> {
                displayResultGoods(g);
            });

            sumGoodsLabel.setText(sumGoods.toString());
        }
    }

    @FXML
    private void okButtonAction(ActionEvent event) {
    }

    /**
     * Поиск по коду или по названию товара
     */
    private void getNameCodeGood() {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<AllGoodsTable> filteredData = new FilteredList<>(allGoodData, p -> true);
        // 2. Set the filter Predicate whenever the filter changes.
        searchGoodTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (person.getCode().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (person.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<AllGoodsTable> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(allGoodTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        allGoodTable.setItems(sortedData);

    }

    @FXML
    private void getViewDishCheckBox(ActionEvent event) {

        if (viewDishCheckBox.isSelected()) {
            System.out.println("Выбран продукты");

            for (int i = -1; i < allGoodTable.getItems().size(); i++) {
                allGoodData.clear();
            }

            dishList = allDishList.listDish();

            dishList.forEach((d) -> {
                displayResultDish(d);
            });
        } else {
            System.out.println("Выбран товар");
            //удаление строк в таблице
            for (int i = -1; i < allGoodTable.getItems().size(); i++) {
                allGoodTable.getItems().clear();
            }

            goodsList = allGoodsList.listGoods();
            goodsList.forEach((gg) -> {
                displayResultGoods(gg);
            });
        }

    }

    /**
     * Обновление закупочной цены
     *
     * @param event
     */
    @FXML
    private void purchasePriceGoodEdit(TableColumn.CellEditEvent<AllGoodsTable, BigDecimal> event) {

        if (viewDishCheckBox.isSelected()) {
            // Здесь прописать код на обновление  продукта

        } else {
            UpdateGood updateGood = new UpdateGood();
            GoodByCode goodByCode = new GoodByCode();
            Goods g = new Goods();
            //Обновление закупочной цены
            goodByCode.setCode(event.getRowValue().getCode());
            g = goodByCode.displayResult();
            g.setPriceOpt(event.getNewValue());
            updateGood.update(g);

            System.out.println("Изменили на: " + event.getNewValue());
        }
    }

    /**
     * Обновление продажной цены
     *
     * @param event
     */
    @FXML
    private void sellPriceGoodEdit(TableColumn.CellEditEvent<AllGoodsTable, BigDecimal> event) {

        if (viewDishCheckBox.isSelected()) {
            // Здесь прописать код на обновление  продукта
        } else {
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
    }

    /**
     * Обновление остатков
     *
     * @param event
     */
    @FXML
    private void residueGoodEdit(TableColumn.CellEditEvent<AllGoodsTable, BigDecimal> event) {

        if (viewDishCheckBox.isSelected()) {
            // Здесь прописать код на обновление  продукта

            UpdateDish updateDish = new UpdateDish();
            DishByCode dishByCode = new DishByCode();
            Dish d = new Dish();
            //Обновление закупочной цены
            dishByCode.setCode(event.getRowValue().getCode());
            d = dishByCode.displayResult();
            d.setWeight(event.getNewValue());
            updateDish.update(d);

            System.out.println("Изменили на: " + event.getNewValue());
        } else {
            UpdateGood updateGood = new UpdateGood();
            GoodByCode goodByCode = new GoodByCode();
            Goods g = new Goods();
            //Обновление закупочной цены
            goodByCode.setCode(event.getRowValue().getCode());
            g = goodByCode.displayResult();
            g.setResidue(event.getNewValue());
            updateGood.update(g);

            System.out.println("Изменили на: " + event.getNewValue());
        }
    }

    private void displaySumGoods(Goods g) {
        sumGoods = sumGoods.add(g.getPrice().multiply(g.getResidue()));
    }

    /**
     * Метод для просмотра результатов в "JTable".
     */
    private void displayResultGoods(Goods g) {
        codeGoodColumn.setCellValueFactory(new PropertyValueFactory<AllGoodsTable, String>("code"));
        codeGoodColumn.setCellFactory(TextFieldTableCell.<AllGoodsTable>forTableColumn());
        nameGoodColumn.setCellValueFactory(new PropertyValueFactory<AllGoodsTable, String>("name"));
        nameGoodColumn.setCellFactory(TextFieldTableCell.<AllGoodsTable>forTableColumn());

        purchasePriceColumn.setCellValueFactory(new PropertyValueFactory<AllGoodsTable, BigDecimal>("purchasePrice"));
//        purchasePriceColumn.setCellFactory(TextFieldTableCell.<AllGoodsTable>forTableColumn());
        purchasePriceColumn.setCellFactory(col -> new EditableBigDecimalTableCell<AllGoodsTable>());

        sellingPriceColumn.setCellValueFactory(new PropertyValueFactory<AllGoodsTable, BigDecimal>("sellingPrice"));
        sellingPriceColumn.setCellFactory(col -> new EditableBigDecimalTableCell<AllGoodsTable>());

        residueColumn.setCellValueFactory(new PropertyValueFactory<AllGoodsTable, BigDecimal>("residue"));
        residueColumn.setCellFactory(col -> new EditableBigDecimalTableCell<AllGoodsTable>());

        AllGoodsTable allGoodsTable = new AllGoodsTable();
        //for (Goods g : goodsList) {
        for (int i = 0; i < 1; i++) {

            allGoodsTable.setId(g.getIdGoods());
            allGoodsTable.setCode(g.getCode());
            allGoodsTable.setName(g.getName());
            allGoodsTable.setPurchasePrice(g.getPriceOpt());
            allGoodsTable.setSellingPrice(g.getPrice());
            allGoodsTable.setResidue(g.getResidue());

            allGoodData.add(allGoodsTable);
        }

        allGoodTable.setItems(allGoodData);
    }

    private void displayResultDish(Dish d) {
        codeGoodColumn.setCellValueFactory(new PropertyValueFactory<AllGoodsTable, String>("code"));
        codeGoodColumn.setCellFactory(TextFieldTableCell.<AllGoodsTable>forTableColumn());
        nameGoodColumn.setCellValueFactory(new PropertyValueFactory<AllGoodsTable, String>("name"));
        nameGoodColumn.setCellFactory(TextFieldTableCell.<AllGoodsTable>forTableColumn());

        purchasePriceColumn.setCellValueFactory(new PropertyValueFactory<AllGoodsTable, BigDecimal>("purchasePrice"));
        sellingPriceColumn.setCellValueFactory(new PropertyValueFactory<AllGoodsTable, BigDecimal>("sellingPrice"));
        residueColumn.setCellValueFactory(new PropertyValueFactory<AllGoodsTable, BigDecimal>("residue"));

        AllGoodsTable allGoodsTable = new AllGoodsTable();
        //for (Goods g : goodsList) {
        for (int i = 0; i < 1; i++) {

            allGoodsTable.setId(d.getIdDish());
            allGoodsTable.setCode(d.getCode());
            allGoodsTable.setName(d.getName());
            allGoodsTable.setPurchasePrice(d.getPriceOpt());
            allGoodsTable.setResidue(d.getWeight());

            allGoodData.add(allGoodsTable);
        }

        allGoodTable.setItems(allGoodData);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Запуск отдельного потока
        //threadListGoods.run();

        // TODO
        allGoodsList.clearList();

        goodsList = allGoodsList.listGoods();

        goodsList.forEach((g) -> {
            displayResultGoods(g);
        });

        //Список всего товара
        categoryList = categoryGoodList.getList();
        for (CategoryGoods cg : categoryList) {
            options.add(cg.getName());
        }
        options.add("Все категории");

        categoryGood.setItems(options);

        getNameCodeGood();
    }

    /**
     * Удаление товара
     */
    @FXML
    private void deletePosition(ActionEvent event) {
        AllGoodsTable table = allGoodTable.getSelectionModel().getSelectedItem();
        String code = table.getCode();
        String name = table.getName();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Удаление товара!");
        alert.setHeaderText("Удалить данный товар?");
        alert.setContentText(code + " | " + name);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            // ... user chose OK
            DeleteGood deleteGood = new DeleteGood();
            GoodByCode goodByCode = new GoodByCode();
            CategoryGoodList cGList = new CategoryGoodList();
            CategoryGoods idCategGoods = new CategoryGoods();
//            CategoryGoodList cGListAfterCheckCategory = new CategoryGoodList();

            Goods g = new Goods();
            boolean checkCategoryGoods = false;
            //удаление позиции
            goodByCode.setCode(code);
            g = goodByCode.displayResult();

            //            deleteGood.delete(g);
            //Проверка, есть ли категория "Удаленные товары"
            List<CategoryGoods> listCat = cGList.getList();
            for (int i = 0; i < listCat.size(); i++) {
                if (listCat.get(i).getName().equals("Удаленные товары")) {
                    UpdateGood updateGood = new UpdateGood();
                    checkCategoryGoods = true;
                    idCategGoods = listCat.get(i);
                    g.setName("Удаленный товар - " + g.getName());
                    g.setCategoryGoods(idCategGoods);
                    updateGood.update(g);
                    break;
                }
            }
            //Если нет, то создать категорию "Удаленные товары"
            if (checkCategoryGoods == false) {
                AddCategoryGood addCategory = new AddCategoryGood();
                CategoryGoods cg = new CategoryGoods();
                cg.setName("Удаленные товары");
                cg.setNote("Удаленные товары");
                addCategory.add(cg);
                System.out.println("Категория добавлена");
                List<CategoryGoods> lCat = cGList.getList();
                for (int i = 0; i < lCat.size(); i++) {
                    if (lCat.get(i).getName().equals("Удаленные товары")) {
                        UpdateGood updateGood = new UpdateGood();
                        checkCategoryGoods = true;
                        idCategGoods = lCat.get(i);
                        g.setName("Удаленный товар - " + g.getName());
                        g.setCategoryGoods(idCategGoods);
                        updateGood.update(g);
                        break;
                    }
                }
            }

            //удаление строк в таблице
            /*for (int i = -1; i < allGoodTable.getItems().size(); i++) {
            allGoodTable.getItems().clear();
            }*/
            allGoodData.clear();
            //allGoodsList.clearList();
            goodsList = allGoodsList.listGoods();
            goodsList.forEach((gg) -> {
                displayResultGoods(gg);
            });
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    /**
     * Выгрузка в Excel
     *
     * @param event
     */
    @FXML
    private void unloadExcelAction(ActionEvent event) {

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Persons");
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Name");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Age");
        headerCell.setCellStyle(headerStyle);

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        Row row = sheet.createRow(2);
        Cell cell = row.createCell(0);
        cell.setCellValue("John Smith");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue(20);
        cell.setCellStyle(style);

        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";

        try {
            FileOutputStream outputStream;

            outputStream = new FileOutputStream(fileLocation);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException ex) {
//            Logger.getLogger(ViewAllGoodsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
//            Logger.getLogger(ViewAllGoodsController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
