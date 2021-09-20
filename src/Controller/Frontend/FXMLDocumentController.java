/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller.Frontend;

import Controller.Backend.UserController;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.User;
import util.DuplicatedUserException;
import util.nullUserInfoException;

/**
 *
 * @author Cleyton
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private TabPane TabPaneUsers;
    @FXML
    private Button btnStart1;
    @FXML
    private Tab tabLogin;
    @FXML
    private Button btnStartSend;
    @FXML
    private TextField userKey;
    @FXML
    private TextField userIP;
    @FXML
    private TextField userName;
    @FXML
    private TextField userPort;
    @FXML
    private ComboBox<String> userSex;
    @FXML
    private ComboBox<String> userAge;
    @FXML
    private ComboBox<String> userTemperature;
    @FXML
    private ComboBox<String> userBreathingRate;
    @FXML
    private ComboBox<String> userSystolicBloodPressure;
    @FXML
    private ComboBox<String> userDiastolicBloodPressure;
    @FXML
    private ComboBox<String> userHeartRate;
    @FXML
    private ComboBox<String> userOxygenSaturation;
    @FXML
    private Label lblUserName;
    @FXML
    private Label lblUserAge;
    @FXML
    private Label lblUserSex;
    @FXML
    private Label lblConectado;
    @FXML
    private DialogPane AllertUser;
    private Tab connectedUser;
    private ObservableList<Tab> tabs;
    private HashMap<Tab, User> tabsXusers;
    private Tab currentTab;
    private User currentUser;
    private static Stage myStage;
    private Node content;
    @FXML
    private Label lblUserIP;
    @FXML
    private Label lblUserPort;
    @FXML
    private CheckBox tabFix;
    @FXML
    private Label lblUserKey;
    @FXML
    private Label lblAllerts;
    @FXML
    private Pane AllertConfigPane;
    @FXML
    private TextField allertIP;
    @FXML
    private TextField AllertPort;
    @FXML
    private Label lblAllertError;
    @FXML
    private Button btnAllertSave;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        myStage.onCloseRequestProperty().set((EventHandler) (Event event) -> {
            UserController.killAllUsersKonnections();
        });
        tabs = TabPaneUsers.getTabs();
        connectedUser = tabs.get(0);
        content = connectedUser.getContent();
        ArrayList<String> sexs = new ArrayList();
        sexs.add("Masculinho");
        sexs.add("Feminino");
        sexs.add("Outro");
        ArrayList<String> ages = new ArrayList();
        for (int i = 0; i < 120; i++) {
            ages.add("" + i + " anos");
        }
        ArrayList<String> temperature = new ArrayList();
        for (int i = 33; i < 48; i++) {
            temperature.add("" + i + " ºC");
        }
        ArrayList<String> percent = new ArrayList();
        for (int i = 0; i < 100; i++) {
            percent.add("" + i);
        }

        ArrayList<String> heartRate = new ArrayList();
        for (int i = 0; i < 300; i++) {
            heartRate.add("" + i + " bpm");
        }
        ArrayList<String> breathRate = new ArrayList();
        for (int i = 0; i < 30; i++) {
            breathRate.add("" + i + " ipm");
        }
        ArrayList<String> pressure = new ArrayList();
        for (int i = 0; i < 20; i++) {
            pressure.add("" + i + " mmHg");
        }
        userAge.setItems(FXCollections.observableArrayList(ages));
        userAge.getSelectionModel().select(30);
        userSex.setItems(FXCollections.observableArrayList(sexs));
        userSex.getSelectionModel().select(2);
        userTemperature.setItems(FXCollections.observableArrayList(temperature));
        userOxygenSaturation.setItems(FXCollections.observableArrayList(percent));
        userBreathingRate.setItems(FXCollections.observableArrayList(breathRate));
        userHeartRate.setItems(FXCollections.observableArrayList(heartRate));
        userSystolicBloodPressure.setItems(FXCollections.observableArrayList(pressure));
        userDiastolicBloodPressure.setItems(FXCollections.observableArrayList(pressure));
        tabs.remove(0);
        tabsXusers = new HashMap<>();
        myStage.getIcons().add(new Image("/resources/icons/coronavirus.png"));
        AllertUser.lookupButton(ButtonType.CLOSE).addEventHandler(ActionEvent.ACTION, (event2) -> {
            TabPaneUsers.setDisable(false);
            AllertUser.setVisible(false);
            AllertUser.setDisable(true);
        });
        AllertUser.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        AllertConfigPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        TabPaneUsers.setDisable(true);
        AllertConfigPane.setVisible(true);
    }

    /**
     * Evento ocasionado pelo início do envio das mensagens.
     * @param event Evento capturado.
     */
    @FXML
    private void handleBtnStartSend(ActionEvent event) {
        setConnectedVisualStatus(true);
    }

    /**
     * Atualiza as informações de conexão (botão de enviar e label de conexão).
     * @param flag Identifica o motivo da atualização: true -> click no botão de cancelar envio.
     *                                                false ->Alteração de tab de usuário.
     */
    private void setConnectedVisualStatus(boolean flag) {
        if (currentUser.isInvalidPort()) {
            disableSend();
            return;
        }
        btnStartSend.setDisable(false);
        btnStartSend.setVisible(true);
        if (flag) {
            if (currentUser.isConnected()) {
                currentUser.stopConnection();
                lblConectado.setText("Aguardando");
                lblConectado.setTextFill(Color.BLACK);
                btnStartSend.setText("Iniciar Envio");
            } else {
                btnStartSend.setText("Parar Envio");
                currentUser.startConnection();
                lblConectado.setText("Enviando");
            }
        } else {
            if (currentUser.isConnected()) {
                btnStartSend.setText("Parar Envio");
                lblConectado.setText("Enviando");
            } else {
                lblConectado.setText("Aguardando");
                lblConectado.setTextFill(Color.BLACK);
                btnStartSend.setText("Iniciar Envio");
            }
        }
    }

    /**
     * Caso as informações de conexão sejam inválidas, impede o estabelecimento de conexão.
     */
    private void disableSend() {
        lblConectado.setText("Host inválido. Impossível Conectar.");
        btnStartSend.setDisable(true);
        btnStartSend.setVisible(false);
    }

    /**
     * Gerancia as ações após clique no botão de início de conexão.
     * @param event Evento capturado.
     */
    @FXML
    private void handleBtnStartConnection(ActionEvent event) {
        User user;
        try {
            String ip = userIP.getText();
            String port = userPort.getText();
            String key = userKey.getText().replaceAll(" ", "");
            String name = userName.getText();
            if (ip.equals("") || key.equals("") || port.equals("")||name.equals("")) {
                throw new nullUserInfoException(" User: \"" + key +" Nome: \"" + name + "\" IP: \"" + ip + "\" Port: \"" + port + "\"");
            }
            user = UserController.createUser(ip, port, key, userName.getText(), userAge.getValue(), userSex.getValue(), this);
            System.out.println("Criando nova Conexão - " + " User: \"" + key + "\" IP: \"" + ip + "\" Port: \"" + port + "\".");
            Tab tab = new Tab();
            tabsXusers.put(tab, user);
            tab.setText(user.getKey());
            tab.setOnSelectionChanged((Event event1) -> {
                userTabChanged(event1);
            });
            tab.setOnClosed((Event event1) -> {
                stopConnection(event1);
            });
            tabs.add(tabs.size() - 1, tab);
            if (!tabFix.selectedProperty().getValue()) {
                TabPaneUsers.getSelectionModel().select(tabs.size() - 2);
            }
        } catch (DuplicatedUserException ex) {
            TabPaneUsers.setDisable(true);
            AllertUser.setVisible(true);
            AllertUser.setDisable(false);
            AllertUser.setHeaderText("Usuário Já conectado");
            AllertUser.setContentText(ex.getMessage());
        } catch (nullUserInfoException ex) {
            TabPaneUsers.setDisable(true);
            AllertUser.setVisible(true);
            AllertUser.setDisable(false);
            AllertUser.setHeaderText("Dados de usuário inválidos");
            AllertUser.setContentText(ex.getMessage());
        }

    }

    /**
     * Gerencia as ações após mudança de tab de usuário.
     * @param event Evento capturado.
     */
    @FXML
    private void userTabChanged(Event event) {
        Tab t = (Tab) event.getTarget();
        currentTab = t;
        if (t.getText().equals("V^Ja3?UGc*qp<D&Nky=9!n4Hr]MV,)/n&s$hxd@SMhJ}6X9qPjUd?Tb/mS$B/7kuD,K,!mN3C,j{*F~A8U*nXmD:*XWE%~nEPfqdYK#k*pT$(!b<&nzfS$rjz-tKP?^.")) {
            return;
        }
        String name = t.getText();
        if (t.isSelected()) {//Caso esteja abrindo a Tab, atualiza as informações da tela com o User referente à Tab em questão.
            t.setContent(content);
            User user = tabsXusers.get(t);
            currentUser = user;
            if (user.isHasUnseenAllert()) {
                this.AllertUser(false, user, user.getAllertException(),user.getAllertString());
            }
            lblUserName.setText(user.getName());
            lblUserAge.setText(user.getAge());
            lblUserSex.setText(user.getSex());
            lblUserKey.setText(user.getKey());
            lblUserIP.setText(user.getIp());
            lblUserPort.setText(user.getPort());
            setConnectedVisualStatus(false);
            userBreathingRate.getSelectionModel().select(user.getBreathingRate());
            userSystolicBloodPressure.getSelectionModel().select(user.getSystolicBloodPressure());
            userDiastolicBloodPressure.getSelectionModel().select(user.getDiastolicBloodPressure());
            userHeartRate.getSelectionModel().select(user.getHeartRate());
            userOxygenSaturation.getSelectionModel().select(user.getOxygenSaturation());
            userTemperature.getSelectionModel().select(user.getTemperature());

        } else {//Caso esteja fechando a Tab de usuário.
            t.setContent(null);
            currentTab = tabLogin;
        }
    }

    /**
     * Finaliza a conexão de um usuário.
     * @param event Evento recebido.
     */
    @FXML
    private void stopConnection(Event event) {
        User user = tabsXusers.get((Tab) event.getTarget());
        user.stopConnection();
        Controller.Backend.UserController.removeUser(user.getKey());

    }

    public static Stage getMyStage() {
        return myStage;
    }

    public static void setMyStage(Stage myStage) {
        FXMLDocumentController.myStage = myStage;
    }

    /**
     * Caso a tela tenha alteração na informação de Temperatura, atualiza o usuário atual.
     * @param event 
     */
    @FXML
    private void userTemperatureOnChange(ActionEvent event) {
        currentUser.setTemperature(userTemperature.getValue());
    }

    @FXML
    private void userBreathingRateOnChange(ActionEvent event) {
        currentUser.setBreathingRate(userBreathingRate.getValue());
    }

    @FXML
    private void userHeartRateOnChange(ActionEvent event) {
        currentUser.setHeartRate(userHeartRate.getValue());
    }

    @FXML
    private void userOxygenSaturationOnChange(ActionEvent event) {
        currentUser.setOxygenSaturation(userOxygenSaturation.getValue());
    }

    @FXML
    private void userSystolicBloodPressureOnChange(ActionEvent event) {
        currentUser.setSystolicBloodPressure(userSystolicBloodPressure.getValue());
    }

    @FXML
    private void userDiastolicBloodPressureOnChange(ActionEvent event) {
        currentUser.setDiastolicBloodPressure(userDiastolicBloodPressure.getValue());
    }

    /**
     * Altera a cor do label de conexão para verde e em sequência preto.
     * @param user 
     */
    public void pisca(User user) {
        if (!currentUser.equals(user)) {
            return;
        }
        lblConectado.setTextFill(Color.SPRINGGREEN);
        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            lblConectado.setTextFill(Color.BLACK);
        }
        lblConectado.setTextFill(Color.BLACK);
    }

    /**
     * Envia uma mensagem de alerta ao usuário.
     * @param flag Informa se devem ou não ser atualizadas as informações de conexão da tela.
     * @param user Usuário para o qual a mensagem deve ser enviada.
     * @param message Mensagem a ser exibida.
     * @param ex Exceção gerada para a exibição da alerta.
     */
    public void AllertUser(boolean flag, User user, String message, String ex) {
        if (currentTab == tabLogin) {
            return;
        }
        if (user.equals(currentUser)) {
            TabPaneUsers.setDisable(true);
            AllertUser.setVisible(true);
            AllertUser.setDisable(false);
            Platform.runLater(() -> {
                AllertUser.setHeaderText(message);
                AllertUser.setContentText(ex);
                if (flag) {
                    setConnectedVisualStatus(true);
                }
                user.setHasUnseenAllert(false);
            });

        }
    }

    /**
     * Configura o endereço (IP e Porta) de recebimento das alertas.
     * @param event 
     */
    @FXML
    private void saveAllertAddress(ActionEvent event) {
        String ipTxt = allertIP.getText();
        try {
            int portTxt = Integer.parseInt(AllertPort.getText());
            if (portTxt > 100000 || portTxt < 0) {
                throw new NumberFormatException();
            }
            if (ipTxt.equals("")) {
                lblAllertError.setVisible(true);
                lblAllertError.setText("Insira um endereço IP.");
                lblAllertError.setLayoutX(113);
                return;
            }
            AllertConfigPane.setVisible(false);
            TabPaneUsers.setDisable(false);
            UserController.configReceiver(ipTxt, portTxt);
            lblAllerts.setText(ipTxt + ":" + portTxt);
        } catch (NumberFormatException ex) {
            lblAllertError.setVisible(true);
            lblAllertError.setText("Insira uma porta válida.");
            lblAllertError.setLayoutX(109);
        }

    }
};
