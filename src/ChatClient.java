import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient implements Runnable {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private ClientSocket clientSocket;
    private Scanner scanner;

    public ChatClient() {
        scanner = new Scanner(System.in);
    }

    public void start() throws UnknownHostException, IOException {
        try {
            clientSocket = new ClientSocket (new Socket(SERVER_ADDRESS, ChatServer.PORT));
            System.out.println("cliente conectado no servidor: " + SERVER_ADDRESS + ":" + ChatServer.PORT);
            new Thread(this).start();
            messageLoop();
            
        } finally {
            clientSocket.close();
        }
    }

    private void messageLoop() throws IOException {
        String msg;
        do {
            System.out.print("digite uma mensagem (ou SAIR para encerrar o programa): ");
            msg = scanner.nextLine();
            clientSocket.sendMsg(msg);            
        } while(!msg.equalsIgnoreCase("sair"));
    }
    
    @Override
    public void run() {
        String msg;
        while ((msg = clientSocket.getMessage()) != null) {
            System.out.printf("mensagem recebida do servidor: %s \n",
            msg);
        }
    } 

    public static void main(String[] args) {
        try {
            ChatClient client = new ChatClient();
            client.start();
        } catch (IOException e) {
            System.out.println("erro ao iniciar cliente: "+ e.getMessage());
        }
        System.out.println("cliente finalizado");
    }
}
