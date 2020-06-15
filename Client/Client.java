//David William Nartey
//Assignment 4
//Client.java
import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.Random;

public class Client {
    private static BigInteger modulus,privateKey,publicKey;
    private static Random r = new Random();
    public static byte[] encrypted,decrypted;
    static int x = (Math.random() <= 0.5) ? 1 : 2;
    public static String currentComputer;


    // Selects a Computer Based on random number generated
    public static void selectComputer(){
        switch(x) {
            case 1:
                currentComputer = "Computer1";
                break;
            case 2:
                currentComputer = "Computer2";
                break;
        }
    }

    /*
    Reads the client file that holds the client name, public and private keys
    Initializes public, private and mod according to the current randomly selected computer
     */
    public static void readFileClient(String file) throws IOException {
        String line1,line2;
        String fileContents1 = "", fileContents2 = "";
        StringBuffer buffer = new StringBuffer();
        String[] split = new String[0];
        BufferedReader br = new BufferedReader(new FileReader(file));
        while ((line1 = br.readLine()) != null && (line2 = br.readLine()) != null) {
            fileContents1 = line1;
            fileContents2  = line2;
        }
        if(currentComputer.equalsIgnoreCase("Computer1")){
            split = fileContents1.split(" ");
        }
        if(currentComputer.equalsIgnoreCase("Computer2")){
            split = fileContents2.split(" ");
        }
        String pub = split[1];
        String priv = split[2];
        String mod = split[3];
        publicKey = new BigInteger(pub);
        privateKey = new BigInteger(priv);
        modulus = new BigInteger(mod);
    }

    //reads the server file that holds the server public key
    public static void readFileServer(String file) throws IOException {
        String line = "", fileContents = "";
        StringBuffer buffer = new StringBuffer();
        BufferedReader br = new BufferedReader(new FileReader(file));
        while ((line = br.readLine()) != null) {
            fileContents = line;
        }
        String[] split = fileContents.split(" ");
        String pub = split[0];
        String mod = split[1];
        publicKey = new BigInteger(pub);
        modulus = new BigInteger(mod);
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
        encrypted = encrypt(message.getBytes());
        return encrypted;
    }

    //encrypts the message by calling above decrypt function and returns a byte array
    public static byte[] decryptX(byte[] message){
        decrypted = decrypt(message);
        //System.out.println("Decrypting Bytes: " + bytesToString(decrypted));
        System.out.println("Decrypted Message: " + new String(decrypted));
        return decrypted;
    }

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
        selectComputer();
        readFileServer("serverRSA.txt");
        byte[] encryptedMessage = encrypt(currentComputer);
        System.out.println("encrypted Message: " + bytesToString(encryptedMessage));
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = null;
        DataOutputStream oos = null;
        ObjectInputStream ois = null;
        socket = new Socket(host.getHostName(), 9876);
        oos = new DataOutputStream(socket.getOutputStream());
        System.out.println("Sending request to Socket Server");
        oos.writeInt(encryptedMessage.length);
        oos.write(encryptedMessage);
        ois = new ObjectInputStream(socket.getInputStream());
        String message = (String) ois.readObject();
        System.out.println("Message: " + message);
        DataInputStream ois1 = new DataInputStream(socket.getInputStream());
        int length = ois1.readInt();
        byte[] receivedMessage = new byte[length];
        ois1.readFully(receivedMessage, 0, receivedMessage.length);
        System.out.println("Received Message: " + bytesToString(receivedMessage));
        readFileClient("clientRSA.txt");
        byte[] decrypt = decryptX(receivedMessage);
        String computerSession = new String(decrypt);
        String comp[] = computerSession.split(" ");
        if (comp[0].equalsIgnoreCase(currentComputer)){
            System.out.println(currentComputer + " Authenticated");
            System.out.println("Session ID: " + comp[1]);
        }
        else{
            System.out.println("UNAUTHENTICATED");
        }
        System.out.println("Connection Closed");
    }
}
