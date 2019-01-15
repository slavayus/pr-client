import com.google.gson.Gson;
import data.Dictionary;
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

            loop:
            while (!socket.isOutputShutdown()) {
                System.out.print("Enter command: ");
                String clientCommand = br.readLine();
                switch (clientCommand) {
                    case "quit":
                        break loop;
                    case "select":
                        System.out.print("Enter searching word: ");
                        Request request = new Request();
                        request.setCommand(clientCommand);
                        Dictionary dictionary = new Dictionary();
                        dictionary.setWord(br.readLine());
                        request.setDictionary(dictionary);
                        out.writeUTF(new Gson().toJson(request));
                        out.flush();
                        break;
                }
                System.out.println("reading...");
                System.out.println(in.readUTF());
            }
            System.out.println("Closing connections & channels on clentSide - DONE.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}