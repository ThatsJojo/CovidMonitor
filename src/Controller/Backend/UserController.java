package Controller.Backend;

import Controller.Frontend.FXMLDocumentController;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import model.UDPConnectionReceiver;
import model.UDPConnectionSender;
import model.User;
import util.DuplicatedUserException;

public class UserController {

    private static final HashMap<String, User> USERS = new HashMap();
    private static UDPConnectionReceiver RECEIVER;

    private static boolean started = false;

    public static void configReceiver(String ip, int port) {
        RECEIVER = new UDPConnectionReceiver(USERS, ip, port);

    }

    /**
     * Cria um usuário
     * @param ip Endereço para conexão com o servidor.
     * @param port Porta para conexão com o servidor.
     * @param key Chave do usuário.
     * @param name Nome do usuário.
     * @param age Idade do usuário
     * @param sex Sexo do usuário
     * @param listenner tela que escuta as alterações do usuário.
     * @return Usuário criado.
     * @throws DuplicatedUserException Exceção gerada caso o usuário já exista.
     */
    public static User createUser(String ip, String port, String key, String name, String age, String sex, FXMLDocumentController listenner) throws DuplicatedUserException {
        if (USERS.containsKey(key)) {
            throw new DuplicatedUserException("Nome: \"" + name + "\" User: \"" + key + "\" IP: \"" + ip + "\" Port: \"" + port + "\"");
        }
        User user = new User(ip, port, key, name, age, sex, listenner);
        user.setReceiver(RECEIVER);
        user.setSender(new UDPConnectionSender(user));
        if (!started) {
            RECEIVER.start();
            started = true;
        }
        USERS.put(key, user);
        return USERS.get(key);
    }

    /**
     * Finaliza as conexões de todos os usuários.
     */
    public static void killAllUsersKonnections() {
        RECEIVER.stopConnection();
        RECEIVER.interrupt();
        USERS.forEach((t, u) -> {
            u.stopConnection();
        });
    }
    
    /**
     * Remove um usuário do HashMap.
     * @param key chave para o usuário.
     * @return Usuário removido ou null caso ele não estivesse presente.
     */
    public static User removeUser(String key) {
        USERS.get(key).stopConnection();
        return USERS.remove(key);
    }
    
    @Deprecated
    private static int getPorta() {
        int porta = 0;
        boolean repeat;
        do {
            try {
                System.out.println("Digite a porta para receber as alertas:");
                porta = new Scanner(System.in).nextInt();
                repeat = porta > 100000 || porta < 0;
            } catch (InputMismatchException ex) {
                repeat = true;
            }
        } while (repeat);
        return porta;
    }

    @Deprecated
    private static String getIP() {
        String porta = "";
        boolean repeat;
        do {
            try {
                System.out.println("Digite o IP para receber as alertas:");
                porta = new Scanner(System.in).nextLine();
                repeat = false;
            } catch (InputMismatchException ex) {
                repeat = true;
            }
        } while (repeat);
        return porta;
    }
}
