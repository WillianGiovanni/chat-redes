import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private Socket clientSocket;
    private Scanner scanner;
    private BufferedWriter out;

    public ChatClient() {
        scanner = new Scanner(System.in);
    }

    public void start() throws UnknownHostException, IOException {
        clientSocket = new Socket(SERVER_ADDRESS, ChatServer.PORT);

        this.out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        
        System.out.println("cliente conectado no servidor: " + SERVER_ADDRESS + ":" + ChatServer.PORT);
        messageLoop();
    }

    private void messageLoop() {
        String msg;
        do {
            System.out.print("digite uma mensagem (ou sair para finalizar): ");
            msg = scanner.nextLine();
        } while(!msg.equalsIgnoreCase("sair"));
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
