package main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sever {
    private static Map<String, List<DatagramSocket>> clientStocks = new HashMap<>();


    public static void subToStock(String stockSymbol, DatagramSocket clientSocket, InetAddress clientAddress, int clientPort){
        clientStocks.putIfAbsent(stockSymbol, new ArrayList<>());

        List<DatagramSocket> subs = clientStocks.get(stockSymbol);
        subs.add(clientSocket);

        String confirmation = "Subscribed to: " + stockSymbol;
        DatagramPacket confirmPacket = new DatagramPacket(confirmation.getBytes(), confirmation.length(), clientAddress, clientPort);


        try {
            clientSocket.send(confirmPacket);
        } catch (IOException e) {
        }
    }


    public static void main(String[] args) throws IOException{
        DatagramSocket socket = new DatagramSocket(12345);

        while (true) {
            byte[] bytes = new byte[1024];
            DatagramPacket packet = new DatagramPacket(bytes, 0, 1024);
            socket.receive(packet);
            String request = new String(packet.getData(), 0 , packet.getLength());
            System.out.println("Making a sub request for: " + request);

            String message = new String(packet.getData(), 0, packet.getLength());
            String symbol = message.split(" ")[0];

            InetAddress clientAddress = packet.getAddress();
            int clientPort = packet.getPort(); 

            subToStock(symbol, socket, clientAddress, clientPort);
        }
    }

    
}
