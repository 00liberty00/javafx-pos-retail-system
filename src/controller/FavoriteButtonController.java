/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ml.model.CategoryFavorite;
import ml.model.Favorite;
import ml.query.favorite.FavoriteList;
import org.springframework.transaction.annotation.Transactional;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class FavoriteButtonController implements Initializable {

    @FXML
    private Button favButt_1;
    @FXML
    private Button favButt_2;
    @FXML
    private Button favButt_3;
    @FXML
    private Button favButt_4;
    @FXML
    private Button favButt_5;
    @FXML
    private Button favButt_6;
    @FXML
    private Button favButt_7;
    @FXML
    private Button favButt_8;
    @FXML
    private Button favButt_9;
    @FXML
    private Button favButt_10;
    @FXML
    private Button favButt_11;
    @FXML
    private Button favButt_12;
    @FXML
    private Button favButt_13;
    @FXML
    private Button favButt_14;
    @FXML
    private Button favButt_15;
    @FXML
    private Button favButt_16;
    @FXML
    private Button favButt_17;
    @FXML
    private Button favButt_18;
    @FXML
    private Button favButt_19;
    @FXML
    private Button favButt_20;
    @FXML
    private Button favButt_21;
    @FXML
    private Button favButt_22;
    @FXML
    private Button favButt_23;
    @FXML
    private Button favButt_24;
    @FXML
    private Button favButt_25;

    private Stage dialogStage;
    private List<Favorite> favList = new ArrayList<>();
    private FavoriteList allFavorList = new FavoriteList();
    private String buttonText = "";

    /**
     * Initializes the controller class.
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setFavList(List<Favorite> favList) {
        this.favList = favList;
        System.out.println("lllll^^^^ " + favList.size());
        System.out.println("lllll^^^^^ " + this.favList.size());

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                dialogStage.setAlwaysOnTop(true);

            }
        });

        //favList = allFavorList.listFavoriteCategory();
    }

    /**
     *
     *
     * @param cf
     */
    public void getButton(CategoryFavorite cf) {
        listFavCateg(cf);
    }

    @Transactional
    @FXML
    private void buttonFav(Event evt) {
        Object o = evt.getSource();
        Button b = null;
        String buttonText = "";

        if (o instanceof Button) {
            b = (Button) o;
            buttonText = b.getText();
        }
        dialogStage.close();
        this.buttonText = buttonText;

    }

    /**
     * ���������� �������� ��������� ������
     *
     * @return
     */
    public String getTextButtonFav() {

        return buttonText;
    }

    /**
     *
     *
     * @param cf
     */
    private void listFavCateg(CategoryFavorite cf) {


        for (Favorite f : favList) {
            if (Objects.equals(f.getCategoryFavorite().getIdCategoryFavorite(), cf.getIdCategoryFavorite())) {
                if (f.getButtonName().equals("favButt_1")) {

                    favButt_1.setText(f.getGoods().getName());
                }
                if (f.getButtonName().equals("favButt_2")) {

                    favButt_2.setText(f.getGoods().getName());
                }
                if (f.getButtonName().equals("favButt_3")) {

                    favButt_3.setText(f.getGoods().getName());
                }
                if (f.getButtonName().equals("favButt_4")) {

                    favButt_4.setText(f.getGoods().getName());
                }
                if (f.getButtonName().equals("favButt_5")) {

                    favButt_5.setText(f.getGoods().getName());
                }
                if (f.getButtonName().equals("favButt_6")) {

                    favButt_6.setText(f.getGoods().getName());
                }
                if (f.getButtonName().equals("favButt_7")) {

                    favButt_7.setText(f.getGoods().getName());
                }
                if (f.getButtonName().equals("favButt_8")) {

                    favButt_8.setText(f.getGoods().getName());
                }
                if (f.getButtonName().equals("favButt_9")) {

                    favButt_9.setText(f.getGoods().getName());
                }
                if (f.getButtonName().equals("favButt_10")) {

                    favButt_10.setText(f.getGoods().getName());
                }
                if (f.getButtonName().equals("favButt_11")) {

                    favButt_11.setText(f.getGoods().getName());
                }
                if (f.getButtonName().equals("favButt_12")) {

                    favButt_12.setText(f.getGoods().getName());
                }
                if (f.getButtonName().equals("favButt_13")) {

                    favButt_13.setText(f.getGoods().getName());
                }
                if (f.getButtonName().equals("favButt_14")) {

                    favButt_14.setText(f.getGoods().getName());
                }
                if (f.getButtonName().equals("favButt_15")) {

                    favButt_15.setText(f.getGoods().getName());
                }
                if (f.getButtonName().equals("favButt_16")) {

                    favButt_16.setText(f.getGoods().getName());
                }
                if (f.getButtonName().equals("favButt_17")) {

                    favButt_17.setText(f.getGoods().getName());
                }
                if (f.getButtonName().equals("favButt_18")) {

                    favButt_18.setText(f.getGoods().getName());
                }
                if (f.getButtonName().equals("favButt_19")) {

                    favButt_19.setText(f.getGoods().getName());
                }
                if (f.getButtonName().equals("favButt_20")) {

                    favButt_20.setText(f.getGoods().getName());
                }
                if (f.getButtonName().equals("favButt_21")) {

                    favButt_21.setText(f.getGoods().getName());
                }
                if (f.getButtonName().equals("favButt_22")) {

                    favButt_22.setText(f.getGoods().getName());
                }
                if (f.getButtonName().equals("favButt_23")) {

                    favButt_23.setText(f.getGoods().getName());
                }
                if (f.getButtonName().equals("favButt_24")) {

                    favButt_24.setText(f.getGoods().getName());
                }
                if (f.getButtonName().equals("favButt_25")) {

                    favButt_25.setText(f.getGoods().getName());
                }

            }
        }
    }

}
