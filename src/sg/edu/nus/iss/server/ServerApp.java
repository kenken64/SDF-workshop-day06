package sg.edu.nus.iss.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static sg.edu.nus.iss.server.Cookie.*;

public class ServerApp {
    private static final String ARG_MESSAGE = "usage: java sg.edu.nus.iss.app.server.ServerApp <port no> <cookie file>";

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(ARG_MESSAGE);
            System.exit(1);
        }

        String serverPort = args[0];
        String cookieFile = args[1];
        
        try (ServerSocket server = new ServerSocket(Integer.parseInt(serverPort))) {
            System.out.printf("Cookie Server started at port %s%n", serverPort);
            Cookie.initAllCookies(cookieFile);
            while (true) {
                try (Socket sock = server.accept();
                        InputStream is = sock.getInputStream();
                        DataInputStream dis = new DataInputStream(is);
                        OutputStream os = sock.getOutputStream();
                        DataOutputStream dos = new DataOutputStream(os)) {

                    System.out.println("Client connected");

                    while (true) {
                        String dataFromClient;
                        try {
                            dataFromClient = dis.readUTF();
                        } catch (EOFException e) {
                            System.out.println("Client disconnected");
                            break;
                        }

                        if ("get-cookie".equals(dataFromClient)) {
                            String randomCookie = getRandomCookie();
                            dos.writeUTF("cookie-text_" + randomCookie);
                        } else {
                            dos.writeUTF("Invalid command!");
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Connection error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}