/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Dave
 */
public class ViewCovid19CertController implements Initializable {

    @FXML
    private WebView webView;
    @FXML
    private VBox vBox;

    private Stage primaryStage;
    private String urlText;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
//          WebView webView = new WebView();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                webView.getEngine().load(urlText);

            }
        });
    }

    public void setURL(String text) {
        this.urlText = text;
    }

    public void setStage(Stage stage) {
        this.primaryStage = stage;
    }

}
