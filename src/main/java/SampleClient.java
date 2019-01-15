import com.google.gson.Gson;
import data.Request;

import java.io.*;
import java.net.Socket;

class SampleClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("0.0.0.0", 7007);
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream())) {

            System.out.println("Client connected to socket.");
            System.out.println();
            System.out.println("Client writing channel = out & reading channel = in initialized.");

            while (!socket.isOutputShutdown()) {
                if (br.ready()) {
                    System.out.println("Client start writing in channel...");
                    String clientCommand = br.readLine();
                    Request request = new Request();
                    request.setCommand(clientCommand);
                    out.writeUTF(new Gson().toJson(request));
                    out.flush();
                    System.out.println("Client sent message " + clientCommand + " to server.");
                    if (clientCommand.equalsIgnoreCase("quit")) {
                        break;
                    }

                    System.out.println("Client sent message & start waiting for data from server...");
                    if (in.available() > 0) {
                        System.out.println("reading...");
                        System.out.println(in.readUTF());
                    }
                }
            }
            System.out.println("Closing connections & channels on clentSide - DONE.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}