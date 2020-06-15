//David William Nartey
//Assignment 4
//Server.java
//Reference: https://www.geeksforgeeks.org/generate-random-string-of-given-size-in-java/
import java.io.*;
import java.lang.ClassNotFoundException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Server {
    //variable declaration
    private static ServerSocket server;
    private static int port = 9876;
    private static BigInteger modulus,privateKey,publicKey;
    private static Random r = new Random();
    public static byte[] encrypted,decrypted;
    public static String decryptedComputer;

    //generates random word for session ID
    public static String randomWord(int n){
        String word = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        StringBuilder combine = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index = (int)(word.length() * Math.random());
            combine.append(word.charAt(index));
        }
        return combine.toString();
    }

    /*
    Reads the client file that holds the client name and public keys
    Initializes public and modulus according to the current randomly selected computer
     */
    public static void readFileClient(String file) throws IOException {
        String line1 = "", line2 = "", fileContents1 = "", fileContents2 = "";
        StringBuffer buffer = new StringBuffer();
        String[] split = new String[0];
        BufferedReader br = new BufferedReader(new FileReader("clientRSA.txt"));
        while ((line1 = br.readLine()) != null && (line2 = br.readLine()) != null) {
            fileContents1 = line1;
            fileContents2  = line2;
        }
        if(decryptedComputer.equalsIgnoreCase("Computer1")){
            split = fileContents1.split(" ");
        }
        if(decryptedComputer.equalsIgnoreCase("Computer2")){
            split = fileContents2.split(" ");
        }
        String pub = split[1];
        String mod = split[2];
        publicKey = new BigInteger(pub);
        modulus = new BigInteger(mod);
    }

    //Reads the Server file and initializes modulus, public and private keys
    public static void readFileServer(String file) throws IOException {
        String line = "", fileContents = "";
        StringBuffer buffer = new StringBuffer();
        BufferedReader br = new BufferedReader(new FileReader(file));
        while ((line = br.readLine()) != null) {
            fileContents = line;
        }
        String[] split = fileContents.split(" ");
        String pub = split[0];
        String priv = split[1];
        String mod = split[2];
        publicKey = new BigInteger(pub);
        modulus = new BigInteger(mod);
        privateKey = new BigInteger(priv);
    }

    //converts bytes to String and returns a String
    private static String bytesToString(byte[] encrypted){
        String test = "";
        for (byte b : encrypted){
            test += Byte.toString(b);
        }
        return test;
    }

    /*
    Encrypts the Message using the RSA algorithm incorporating a
    public key and modulus previously calculated
     */
    public static byte[] encrypt(byte[] message){
        return (new BigInteger(message)).modPow(publicKey, modulus).toByteArray();
    }

    /*
    Decrypts the Message using the RSA algorithm incorporating a
    private key and modulus previously calculated
    */
    public static byte[] decrypt(byte[] message) {
        return (new BigInteger(message)).modPow(privateKey, modulus).toByteArray();
    }

    //encrypts the message by calling above encrypt function and returns a byte array
    public static byte[] encrypt(String message){
        System.out.println("Encrypting String: " + message);
        //System.out.println("String in Bytes: " + bytesToString(message.getBytes()));
        // encrypt
        encrypted = encrypt(message.getBytes());
        return encrypted;
    }

    //encrypts the message by calling above decrypt function and returns a byte array
    public static byte[] decryptX(byte[] message){
        // decrypt
        decrypted = decrypt(message);
        //System.out.println("Decrypting Bytes: " + bytesToString(decrypted));
        System.out.println("Decrypted Message: " + new String(decrypted));
        return decrypted;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        readFileServer("serverRSA.txt");
        server = new ServerSocket(port);
        while(true){
            System.out.println("Waiting for the client request");
            Socket socket = server.accept();
            System.out.println("Connection accepted");
            DataInputStream ois = new DataInputStream(socket.getInputStream());
            int length = ois.readInt();
            byte[] message = new byte[length];
            ois.readFully(message, 0, message.length);
            System.out.println("Received Message: " + bytesToString(message));
            byte[] decrypt = decryptX(message);
            decryptedComputer = new String(decrypt);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject("Connection Made to " + decryptedComputer);
            //System.out.println(publicKey);
            readFileClient("clientRSA");
            //System.out.println(publicKey);
            String sessionKey = randomWord(8);
            System.out.println("Generated Session ID: " + sessionKey);
            byte[] encryptedResponse = encrypt(decryptedComputer + " " + sessionKey);
            System.out.println("encrypted Message: " + bytesToString(encryptedResponse));
            DataOutputStream oos1 = new DataOutputStream(socket.getOutputStream());
            oos1.writeInt(encryptedResponse.length);
            oos1.write(encryptedResponse);
            oos1.close();
            ois.close();
            oos.close();
            socket.close();
            break;
        }
        server.close();
        System.out.println("Terminated Server Socket");
    }
}
