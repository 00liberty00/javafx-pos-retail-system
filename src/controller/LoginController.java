/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ml.controller;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ml.authentication.AuthenticationManag;
import ml.dialog.DialogChoose;
import ml.ipmac.IpMac;
import ml.kkt.KKT;
import ml.model.UserSwing;
import ml.modelLicense.Comp;
import ml.modelLicense.License;
import ml.query.compCard.CompCard;
import ml.query.license.AddMac;
import ml.query.user.AuthUser;
import ml.telegram.Bot;
import ml.trial.TestTrial;
import ml.window.RootWindow;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * FXML Controller class
 *
 * @author dave
 */
public class LoginController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label loginLabel;
    @FXML
    private TextField loginTextField;
    @FXML
    private Label authLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Button okButton;
    @FXML
    private Label message;

    private final ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder();
    private static final AuthenticationManager am = new AuthenticationManag();
    private final AuthUser authUser = new AuthUser();
    private Stage dialogStage;
    private List res;
    private Comp comp = new Comp();
    private KKT kkt;
    private static final Logger log = Logger.getLogger(CheckLayoutController.class);
    private static Bot b = new Bot();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
//        loginTextField.setText("admin");
//        passwordTextField.setText("qqq");

//        loginTextField.setText("admin");
//        passwordTextField.setText("vfhbr2012");

    }

    public void setDialogStage(Stage dialogStage, KKT kkt) {
        this.dialogStage = dialogStage;
        this.kkt = kkt;
    }

    @FXML
    private void okClicked(ActionEvent event) {
        enterInApp();
    }

    @FXML
    public void onEnter(ActionEvent ae) {
        enterInApp();
    }

    /**
     * Вход в приложение.
     */
    private void enterInApp() {
        okButton.setDisable(true);
        String user = loginTextField.getText();
        String pass = passwordTextField.getText();
        authUser.authenticationUser(user);
        res = authUser.displayResult();
        result(res, pass, user);

        log.info("Пользователь: " + user);

        File f = new File("src/ml/resources/settings/telegramSettings.json");
        if (f.exists()) {
            b.startBot(user);
//            System.out.println("файл создан");
        } else {
//            System.out.println("Файл не создан");
        }
    }

    /**
     * Проверка на наличии связки логина и пароля.
     *
     * @param resultList
     * @param pass
     * @param user
     */
    private void result(List resultList, String pass, String user) {

        //Ml_FX ml = new Ml_FX();
        TestTrial testTrial = new TestTrial();
        // Created c = new Created();
        License license = testTrial.license();

        if (!resultList.isEmpty()) {
            for (Object o : resultList) {
                UserSwing u = (UserSwing) o;

                if (shaPasswordEncoder.isPasswordValid(u.getPassword(), pass, null)) {
                    Authentication request = new UsernamePasswordAuthenticationToken(user, pass);
                    Authentication result = am.authenticate(request);
                    SecurityContextHolder.getContext().setAuthentication(result);
                    try {

                        CompCard compCard = new CompCard();
                        IpMac ipMac = new IpMac();
                        compCard.setNum(ipMac.getName(), license);
                        comp = compCard.displayResult();
                        //если этого компа нет в БД(новый комп)
                        if (comp != null) {
                            if (comp.getBlocking() == false) {
                                // +1 пользователь(ПК) в БД в поле ключа 
                                //addUserLicense.update(license);
                                new RootWindow(comp, kkt, b);
                                //Закрыть окно авторизации
                                dialogStage.close();
                            } else {
                                message.setText("Компьютер заблокирован ");

                                //Задержка на выполенние закрытия приложения
                                Timer t = new Timer();
                                t.scheduleAtFixedRate(new TimerTask() {
                                    public void run() {
                                        System.exit(0);
                                    }
                                }, 2000, 3000);
                            }

                        } else {
                            message.setText("Этот компьютер не связан" + " \n " + "с лицензией : № " + license.getLicense());

                            DialogChoose dialog = new DialogChoose();
                            dialog.alert("Внимание!", "Этот компьютер не связан"
                                    + "\n" + "с лицензией : № " + license.getLicense()
                                    + "\n" + "Эта лицензия распространяеся на "
                                    + license.getCountPc() + " компьютер(а)", "Добавить этот компьютер");
                            //Добавление нового компа к лицензии
                            if (dialog.display() == true) {
                                AddMac addMac = new AddMac();
                                Date date = new Date();
                                //ipMac.getIp();
                                //String macString = ipMac.getMac().replaceAll("-", "");
                                String[] macAddressParts = ipMac.getMac().split("-");

                                byte[] macAddressBytes = new byte[6];
                                for (int i = 0; i < 6; i++) {
                                    Integer hex = Integer.parseInt(macAddressParts[i], 16);
                                    macAddressBytes[i] = hex.byteValue();
                                }
                                Comp newComp = new Comp();
                                newComp.setMac(macAddressBytes);
                                newComp.setBlocking(false);
                                newComp.setDateCreate(date);
                                newComp.setName(ipMac.getName());
                                newComp.setLicense(license);
                                //Запись MAC-адреса в БД
                                addMac.add(newComp);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    message.setText("Пользователя с таким логином \n" + "или паролем не существует");
                    okButton.setDisable(false);
                }
            }
        } else {
            message.setText("Пользователя с таким логином \n" + "или паролем не существует");
            okButton.setDisable(false);
        }

    }

}
