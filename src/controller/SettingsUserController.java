/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ml.authentication.AuthenticationManag;
import ml.model.UserSwing;
import ml.query.user.AuthUser;
import ml.query.user.IdUserByName;
import ml.query.user.UpdateAdmin;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Dave
 */
public class SettingsUserController implements Initializable {

    @FXML
    private Label message;
    @FXML
    private TextField admin;
    @FXML
    private PasswordField pass;
    @FXML
    private PasswordField rePass;
    @FXML
    private TextField phone;
    @FXML
    private TextField mail;
    @FXML
    private TextField name;
    @FXML
    private PasswordField oldPass;
    @FXML
    private Button ok;
    @FXML
    private Button cancel;

    private final ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder();
    private static final AuthenticationManager am = new AuthenticationManag();
    private final AuthUser authUser = new AuthUser();
    private List result;
    private UpdateAdmin updateAdmin = new UpdateAdmin();

    @FXML
    private void handleOkUser(ActionEvent event) {
        String user = admin.getText();
        String password = oldPass.getText();
        authUser.authenticationUser(user);
        result = authUser.displayResult();

        for (Object o : result) {
            UserSwing u = (UserSwing) o;

            if (shaPasswordEncoder.isPasswordValid(u.getPassword(), password, null)) {
                if (this.pass.getText().equals(rePass.getText())) {
                    message.setText("Пароль изменен!");
                    u.setPassword(shaPasswordEncoder.encodePassword(pass.getText(), null));
                    updateAdmin.update(u);
                } else {
                    message.setText("Новый пароль не совпадает");

                }
            } else {
                message.setText("Старый пароль не совпадает");
            }
        }
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        // get a handle to the stage
        Stage stage = (Stage) cancel.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    private UserSwing userSwing = new UserSwing();
    private IdUserByName idUserByName = new IdUserByName();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Authentication authentication = SecurityContextHolder.getContext().
                getAuthentication();
        String login = authentication.getName();
        idUserByName.setLoginUser(login);   //метод для idUser по логину
        userSwing = idUserByName.displayResult(); //Возвращает пользователя

        name.setText(userSwing.getName());
        phone.setText(userSwing.getPhone());
        admin.setText(userSwing.getLogin());
        mail.setText(userSwing.getEmail());
    }

}
