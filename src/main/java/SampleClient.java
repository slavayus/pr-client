import com.google.gson.Gson;
import data.Dictionary;
import data.Request;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;

class SampleClient {
    private static final String PROP_FILE_NAME = "server.properties";
    private static Properties prop;

    public static void main(String[] args) throws IOException {
        loadProperties();
        try (Socket socket = new Socket(InetAddress.getByName(prop.getProperty("server.address")), Integer.parseInt(prop.getProperty("server.port")));
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream())) {

            System.out.println("Client connected to socket.");

            loop:
            while (!socket.isOutputShutdown()) {
                System.out.print("Enter command: ");
                String clientCommand = br.readLine();
                Request request = new Request();
                request.setCommand(clientCommand);
                Dictionary dictionary = new Dictionary();
                switch (clientCommand) {
                    case "quit":
                        break loop;
                    case "select":
                        System.out.print("Enter searching word: ");
                        dictionary.setWord(br.readLine());
                        break;
                    case "find":
                        System.out.print("Enter searching word: ");
                        dictionary.setWord(br.readLine());
                        break;
                    case "insert":
                        System.out.print("Enter new word: ");
                        dictionary.setWord(br.readLine());
                        System.out.print("Enter new word description: ");
                        dictionary.setDescription(br.readLine());
                        break;
                    case "update":
                        System.out.print("Enter word: ");
                        dictionary.setWord(br.readLine());
                        System.out.print("Enter word new description: ");
                        dictionary.setDescription(br.readLine());
                        break;
                    case "delete":
                        System.out.print("Enter word: ");
                        dictionary.setWord(br.readLine());
                        break;
                }
                request.setDictionary(dictionary);
                out.writeUTF(new Gson().toJson(request));
                out.flush();
                System.out.println("reading...");
                System.out.println(in.readUTF());
            }
            System.out.println("Closing connections & channels on clentSide - DONE.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadProperties() throws IOException {
        prop = new Properties();
        InputStream inputStream = SampleClient.class.getClassLoader().getResourceAsStream(PROP_FILE_NAME);
        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + PROP_FILE_NAME + "' not found in the classpath");
        }
    }
}