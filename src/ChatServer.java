import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * chatServer
 */
public class ChatServer {

    public static final int PORT = 4000;
    private ServerSocket serverSocket;
    private final List<ClientSocket> clients = new LinkedList<>();

    public void start() throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("servidor iniciado na porta: " + PORT);
        clientConnectionLoop();
    }

    private void clientConnectionLoop() throws IOException {
        while (true) {
            ClientSocket clientSocket = new ClientSocket(serverSocket.accept());
            clients.add(clientSocket);
            new Thread(() -> clientMessageLoop(clientSocket)).start();
        }
    }

    private void clientMessageLoop(ClientSocket clientSocket) {
        String msg;
        try {
            while ((msg = clientSocket.getMessage()) != null) {
                if ("sair".equalsIgnoreCase(msg)) {
                    return;
                }
                System.out.printf("mensagem recebida do cliente %s: %s\n", clientSocket.getRemoteSocketAddress(), msg);
                sendMsgToAll(clientSocket, msg);
            }
        } finally {
            clientSocket.close();
        }
    }

    private void sendMsgToAll(ClientSocket sender, String msg) {
        Iterator<ClientSocket> iterator = clients.iterator();
        while (iterator.hasNext()) {
            ClientSocket clientSocket = iterator.next();
            if (!sender.equals(clientSocket)) {
                if(!clientSocket.sendMsg(
                    "Cliente: " + sender.getRemoteSocketAddress() + ": " + msg)) {
                    iterator.remove();
                };
            }
        }
    }

    public static void main(String[] args) {
        try {
            ChatServer server = new ChatServer();
            server.start();
        } catch (IOException e) {
            System.out.println("erro ao inicial o servidor: " + e.getMessage());
        }
        System.out.println("servidor finalizado");
    }
}