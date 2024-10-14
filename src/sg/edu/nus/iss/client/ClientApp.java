package sg.edu.nus.iss.client;

import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientApp {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java ClientApp <server:port>");
            return;
        }

        String[] arrSplit = args[0].split(":");
        if (arrSplit.length != 2) {
            System.err.println("Invalid server address format. Use <server:port>");
            return;
        }

        String serverAddress = arrSplit[0];
        int port;
        try {
            port = Integer.parseInt(arrSplit[1]);
        } catch (NumberFormatException e) {
            System.err.println("Invalid port number.");
            return;
        }

        try (Socket sock = new Socket(serverAddress, port);
             InputStream is = sock.getInputStream();
             DataInputStream dis = new DataInputStream(is);
             OutputStream os = sock.getOutputStream();
             DataOutputStream dos = new DataOutputStream(os)) {

            Console cons = System.console();
            if (cons == null) {
                System.err.println("No console available");
                return;
            }

            while (true) {
                String input = cons.readLine("Send command to the random cookie server: ");
                if (input == null || input.trim().isEmpty()) {
                    System.out.println("No input provided. Exiting...");
                    break;
                }

                dos.writeUTF(input);
                dos.flush();

                String response = dis.readUTF();
                if (response.contains("cookie-text")) {
                    String[] cookieValArr = response.split("_");
                    if (cookieValArr.length > 1) {
                        System.out.println("Cookie from the server: " + cookieValArr[1]);
                    } else {
                        System.err.println("Invalid cookie format received from server.");
                    }
                } else {
                    System.err.println(response);
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + serverAddress);
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }
}