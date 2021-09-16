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

    public UDPConnectionReceiver(HashMap<String, User> USERS, String IP, int porta) {
        this.USERS = USERS;
        this.porta = porta;
        this.IP = IP;
    }

    @Override
    public void run() {
        try {
            connected = true;
            DatagramSocket ds;
            byte[] msg = new byte[1000];
            DatagramPacket pkg = new DatagramPacket(msg, msg.length);
            while (connected) {
                ds = new DatagramSocket(porta, InetAddress.getByName(IP));
                try{
                    ds.receive(pkg);
                }catch(SocketException ex){
                    if(!ex.getMessage().equals("Receive timed out")){
                        throw ex;
                    }
                }
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

    public void stopConnection() {
        this.connected = false;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void addUser(User user) {

    }
}
