package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import util.UserExceptionAllert;

public class UDPConnectionSender extends Thread {

    private final User user;
    private boolean connected;

    public UDPConnectionSender(User user) {
        this.user = user;
        connected = false;
    }

    @Override
    public void run() {
        try {
            connected = true;
            boolean firstConnect = true;
            InetAddress address = InetAddress.getByName(user.getIp());
            int port = Integer.parseInt(user.getPort());
            if (port > 100000 || port < 0) {
                Exception ex = new Exception("Port out of range:" + port);
                user.setInvalidPort(true);
                user.setAllert(true, new UserExceptionAllert("Conexão não foi estabelecida. Porta inválida: ", ex), "Port out of range:" + port, user.getKey());
                connected = false;
                this.interrupt();
            }
            while (connected) {
                byte[] msg = user.getData();
                DatagramPacket pkg = new DatagramPacket(msg, msg.length, address, port);
                DatagramSocket UDPSocket = new DatagramSocket();
                UDPSocket.send(pkg);
                user.sentAllert();
                UDPSocket.close();
                if (!firstConnect) {
                    Thread.sleep(500);
                }
                firstConnect = false;
            }
        } catch (UnknownHostException ex) {
            user.setAllert(true, new UserExceptionAllert("Conexão falhou: Sender Host não reconhecido.", ex), ex.toString(), user.getKey());
            connected = false;
            user.setInvalidPort(true);
            this.interrupt();
        } catch (SocketException ex) {
            user.setAllert(true, new UserExceptionAllert("Conexão falhou: excessão no UDP Socket sender.", ex), ex.toString(), user.getKey());
            connected = false;
            this.interrupt();
        } catch (IOException ex) {
            user.setAllert(true, new UserExceptionAllert("Conexão falhou: Falha no envio dos dados - IOException.", ex), ex.toString(), user.getKey());
            connected = false;
            this.interrupt();
        } catch (InterruptedException ex) {
            System.out.println("O fim da conexão pode ter impedido o envio do último pacote de dados. Conexão: " + this.connectionToString());
        } catch (java.lang.NumberFormatException ex) {
            user.setInvalidPort(true);
            user.setAllert(true, new UserExceptionAllert("Porta inválida. Número mal formatado: ", ex), ex.toString(), user.getKey());
            connected = false;
            this.interrupt();
        }
    }

    public void stopConnection() {
        this.connected = false;
    }

    public String connectionToString() {
        return " User: \"" + user.getKey() + "\" IP: \"" + user.getIp() + "\" Port: \"" + user.getPort();
    }

    public User getUser() {
        return user;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
