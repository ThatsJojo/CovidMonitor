package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.HashMap;
import util.UserExceptionAllert;

public class UDPConnectionReceiver extends Thread {

    private final HashMap<String, User> USERS;
    private boolean connected;
    private final int porta;
    private final String IP;

    /**
     * Conexão UDP para recebimento das Alertas.
     * @param USERS HashMap dos usuários cadastrados.
     * @param IP Endereço IP para Receber as Alertas.
     * @param porta Porta para receber as Alertas.
     */
    public UDPConnectionReceiver(HashMap<String, User> USERS, String IP, int porta) {
        this.USERS = USERS;
        this.porta = porta;
        this.IP = IP;
    }

    /**
     * Enquanto conectado, recebe pacotes UDP e envia aos usuários as mensagens
     * recebidas que sejam a eles endereçadas.
     */
    @Override
    public void run() {
        try {
            connected = true;
            DatagramSocket ds;
            while (connected) {
                byte[] msg = new byte[1000];
                DatagramPacket pkg = new DatagramPacket(msg, msg.length);
                ds = new DatagramSocket(porta, InetAddress.getByName(IP));
                ds.receive(pkg);
                String recievedMessage[] = new String(pkg.getData()).trim().split("\n");
                if (recievedMessage != null && recievedMessage.length > 1) {
                    User user = USERS.get(recievedMessage[0]);
                    if (user != null) {
                        user.setAllert(false, new UserExceptionAllert("TEM MENSAGEM PRA VOCÊ", new Exception(Arrays.toString(recievedMessage))), recievedMessage[1], user.getKey());
                    }
                }
                ds.close();
            }
        } catch (SocketException ex) {
            System.out.println("Erro no recebimento de alertas: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Erro no recebimento de alertas: " + ex.getMessage());
        }
    }

    /**
     * Para a conexão.
     */
    public void stopConnection() {
        this.connected = false;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
