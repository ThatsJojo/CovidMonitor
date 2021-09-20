package model;

import Controller.Frontend.FXMLDocumentController;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.UserExceptionAllert;

public class User {

    private final String ip;
    private final String port;
    private final String key;
    private final String name;
    private final String age;
    private String sex;
    private String temperature;
    private String breathingRate;
    private String heartRate;
    private String oxygenSaturation;
    private String systolicBloodPressure;
    private String diastolicBloodPressure;
    private final FXMLDocumentController listenner;
    private UDPConnectionSender sender;
    private UDPConnectionReceiver receiver;
    private String allertException;
    private String allertString;
    private boolean hasUnseenAllert;
    private boolean invalidPort;

    /**
     * Cria um usuário.
     * @param ip Endereço IP para conexão do Usuário.
     * @param port Porta para conexão do Usuário.
     * @param key Chave do usuário.
     * @param name Nome do usuário.
     * @param age Idade do usuário.
     * @param sex Sexo do usuário.
     * @param listenner Tela que atualiza a partidas das informações do usuário.
     */
    public User(String ip, String port, String key, String name, String age, String sex, FXMLDocumentController listenner) {
        hasUnseenAllert = false;
        this.ip = ip;
        this.port = port;
        this.key = key;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.listenner = listenner;
        temperature = "36 ºC";
        oxygenSaturation = "96";
        breathingRate = "12 ipm";
        heartRate = "60 bpm";
        systolicBloodPressure = "12 mmHg";
        diastolicBloodPressure = "8 mmHg";
        invalidPort = false;
    }

    /**
     * Retorna a classe de conexão do usuário com o servidor.
     * @return 
     */
    public UDPConnectionSender getSender() {
        return sender;
    }

    public boolean isConnected() {
        return sender.isConnected();
    }

    /**
     * Para as conexões do usuário.
     */
    public void stopConnection() {
        sender.stopConnection();
        receiver.stopConnection();
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        sender.interrupt();
        receiver.interrupt();
    }

    /**
     * Inicializa a conexão com o servidor.
     */
    public void startConnection() {
        sender = new UDPConnectionSender(this);
        sender.start();
    }

    public void setSender(UDPConnectionSender sender) {
        this.sender = sender;
    }

    public UDPConnectionReceiver getReceiver() {
        return receiver;
    }

    public void setReceiver(UDPConnectionReceiver receiver) {
        this.receiver = receiver;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getUser() {
        return ip;
    }

    public String getKey() {
        return key;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getBreathingRate() {
        return breathingRate;
    }

    public void setBreathingRate(String breathingRate) {
        this.breathingRate = breathingRate;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public String getOxygenSaturation() {
        return oxygenSaturation;
    }

    public void setOxygenSaturation(String oxygenSaturation) {
        this.oxygenSaturation = oxygenSaturation;
    }

    public String getSystolicBloodPressure() {
        return systolicBloodPressure;
    }

    public void setSystolicBloodPressure(String systolicBloodPressure) {
        this.systolicBloodPressure = systolicBloodPressure;
    }

    public String getDiastolicBloodPressure() {
        return diastolicBloodPressure;
    }

    public void setDiastolicBloodPressure(String diastolicBloodPressure) {
        this.diastolicBloodPressure = diastolicBloodPressure;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.key);
        return hash;
    }

    public String getAllertException() {
        return allertException;
    }

    public void setAllertException(String allertException) {
        this.allertException = allertException;
    }

    public String getAllertString() {
        return allertString;
    }

    public void setAllertString(String allertString) {
        this.allertString = allertString;
    }

    public boolean isHasUnseenAllert() {
        return hasUnseenAllert;
    }

    public void setHasUnseenAllert(boolean hasUnseenAllert) {
        this.hasUnseenAllert = hasUnseenAllert;
    }

    public boolean isInvalidPort() {
        return invalidPort;
    }

    public void setInvalidPort(boolean invalidPort) {
        this.invalidPort = invalidPort;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (!Objects.equals(this.key, other.key)) {
            return false;
        }
        return true;
    }

    byte[] getData() {
        Date d = new Date();
        String data = key + "\n"
                + name + "\n"
                + age + "\n"
                + sex + "\n"
                + temperature + "\n"
                + breathingRate + "\n"
                + heartRate + "\n"
                + oxygenSaturation + "\n"
                + systolicBloodPressure + "\n"
                + diastolicBloodPressure + "\n"
                + d.getDay() + "/" + d.getMonth() + "/" + (Calendar.getInstance().get(Calendar.YEAR)) + "\n"
                + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds() + "\n";
        return data.getBytes();
    }

    @Deprecated
    byte[] getDataWithField() {
        String data = "key: " + key + "\n"
                + "name: " + name + "\n"
                + "age: " + age + "\n"
                + "sex: " + sex + "\n"
                + "temperature: " + temperature + "\n"
                + "xygenSaturation: " + oxygenSaturation + "\n"
                + "oxygenSaturation: " + breathingRate + "\n"
                + "heartRate: " + heartRate + "\n"
                + "systolicBloodPressure: " + systolicBloodPressure + "\n"
                + "diastolicBloodPressure: " + diastolicBloodPressure + "   \n";
        return data.getBytes();
    }
    
    /**
     * Muda a Alerta do usuário.
     * @param flag Caso seja ou não realizada uma mudança na conexão.
     * @param exception A exeção que causou a alerta.
     * @param string A alerta recebida.
     * @param userKey Chave do usuário.
     */

    public void setAllert(boolean flag, UserExceptionAllert exception, String string, String userKey) {
        setAllert(flag, exception, string);
        this.hasUnseenAllert = true;
    }

    /**
     * Atualiza a tela com a alerta recebida.
     * @param flag Caso seja ou não realizada uma mudança na conexão.
     * @param exception A exeção que causou a alerta.
     * @param string A alerta recebida.
     */
    private void setAllert(boolean flag, UserExceptionAllert exception, String string) {
        this.hasUnseenAllert = true;
        this.allertException = exception.getMessage();
        this.allertString = string;
            this.listenner.AllertUser(flag, this, exception.getMessage(), string);
    }

    /**
     * "Pisca" em verde a label de conexão. Indicando recebimento de pacote.
     */
    public void sentAllert() {
        this.listenner.pisca(this);
    }

    @Override
    public String toString() {
        return "User{" + "ip=" + ip + ", port=" + port + ", key=" + key + ", name=" + name + '}';
    }

}
