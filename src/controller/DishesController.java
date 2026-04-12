/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ml.Ml_FX;
import ml.model.Goods;
import ml.model.TechMap;
import ml.query.techmap.AllTechMapList;
import ml.query.techmap.TechMapListByGood;
import ml.table.DishesTable;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class DishesController implements Initializable {

    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField searchDishTextField;
    @FXML
    private Button techMapButton;
    @FXML
    private Button deleteDishButton;
    @FXML
    private TableView<DishesTable> dishesTable;
    @FXML
    private TableColumn<DishesTable, BigDecimal> numberColumn;
    @FXML
    private TableColumn<DishesTable, String> codeColumn;
    @FXML
    private TableColumn<DishesTable, String> nameColumn;
    @FXML
    private TableColumn<DishesTable, BigDecimal> priceOptColumn;
    @FXML
    private TableColumn<DishesTable, BigDecimal> priceColumn;

    private ObservableList<String> options = FXCollections.observableArrayList();
    private ObservableList<DishesTable> dishData = FXCollections.observableArrayList();
    private AllTechMapList allTechMapList = new AllTechMapList();
    private List<TechMap> techMapList;
    private Stage primaryStage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
//        borderPane.setOnKeyPressed(
//                event -> {
//                    switch (event.getCode()) {
//
//                        case DELETE:
//                            //Удаление строки
//                            TablePosition pos = dishesTable.getSelectionModel().getSelectedCells().get(0);
//                            int row = pos.getRow();
//                            dishData.remove(row);
//                            break;
//                    }
//                });
        // TODO
        allTechMapList.clearList();
        techMapList = allTechMapList.listTechMap();

        //Убираем одинаковые занчения по Goods
        Set<Goods> set = new HashSet<>();
        for (int i = 0; i < techMapList.size(); i++) {
            set.add(techMapList.get(i).getGoods());
        }
        set.forEach((tm) -> {
            displayResultDishes(tm);
        });
    }

    @FXML
    private void techMapAction(ActionEvent event) {
        TechMapListByGood tmlbg = new TechMapListByGood();
        DishesTable table = dishesTable.getSelectionModel().getSelectedItem();
        List<TechMap> tm = tmlbg.listTechMap(table.getNumber());
//        Long number = table.getCode();
//        String name = table.getName();

        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Ml_FX.class.getResource("/ml/view/TechMap.fxml"));

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Техн.карта");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            AnchorPane page = (AnchorPane) loader.load();
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            TechMapController controller = loader.getController();

            controller.setTechMap(tm);
            //controller.setSumText("1000", "10");

//            controller.setTechMap(checkTechMap);
            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteDishAction(ActionEvent event) {
    }

    private void displayResultDishes(Goods g) {
        numberColumn.setCellValueFactory(new PropertyValueFactory<DishesTable, BigDecimal>("number"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<DishesTable, String>("code"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<DishesTable, String>("name"));
        priceOptColumn.setCellValueFactory(new PropertyValueFactory<DishesTable, BigDecimal>("priceOpt"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<DishesTable, BigDecimal>("price"));

        DishesTable dt = new DishesTable();

        for (int i = 0; i < 1; i++) {
            dt.setNumber(g.getIdGoods());
            dt.setCode(g.getCode());
            dt.setName(g.getName());
            dt.setPriceOpt(g.getPriceOpt());
            dt.setPrice(g.getPrice());

            /*if ("ROLE_ADMIN".equals(auth.toString())) {
            reportsDayTable.setResidue(goods.getResidue());
            }*/
            // заполняем таблицу данными
            dishData.add(dt);
        }
        dishesTable.setItems(dishData);
    }

}
