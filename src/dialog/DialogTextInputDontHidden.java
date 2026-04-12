/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.dialog;

import java.util.Optional;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Dave
 */
public class DialogTextInputDontHidden extends Application {

    private String textTitle;
    private String textHeader;
    private String textContext;
    private String textInput;
    private String result = "";

    /**
     * Ввод текстовых данных в диалоговое окно.
     *
     * @param textTitle
     * @param textHeader
     * @param textContext
     * @param textInput
     */
    public void dialog(String textTitle, String textHeader, String textContext, String textInput) {
        this.textTitle = textTitle;
        this.textHeader = textHeader;
        this.textContext = textContext;
        this.textInput = textInput;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        TextInputDialog text = new TextInputDialog(textInput);

        DialogPane root = text.getDialogPane();
        text.setTitle(textTitle);
        text.setHeaderText(textHeader);
        text.setContentText(textContext);
        Stage dialogStage = new Stage(StageStyle.UTILITY);

        for (ButtonType buttonType : root.getButtonTypes()) {
            ButtonBase button = (ButtonBase) root.lookupButton(buttonType);
            button.setOnAction(evt -> {
                root.setUserData(buttonType);
                dialogStage.close();
            });
        }

// replace old scene root with placeholder to allow using root in other Scene
        root.getScene().setRoot(new Group());

        root.setPadding(new Insets(10, 0, 10, 0));
        Scene scene = new Scene(root);

        dialogStage.setScene(scene);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setAlwaysOnTop(true);
        dialogStage.setResizable(false);
        dialogStage.showAndWait();
        Optional<ButtonType> res = Optional.ofNullable((ButtonType) root.getUserData());
        System.out.println("result: " + res.orElse(null));

        //result = "" + res.orElse(null);
        if (res.orElse(null) == ButtonType.OK) {
            this.result = text.getResult();
// 4601751015518
        } else {
            System.out.println("КНОПКА CANCEL");
            this.result = "";

        }
    }

    /**
     * Возвращает значение, введенное в дилоговое окно.
     *
     * @return
     */
    public String display() {
        return this.result;
    }

}
