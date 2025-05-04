package main.java.com.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException{
        //Sending stocks to sub to
        Runnable outgoingPacket = new Runnable() {
            DatagramSocket socket = new DatagramSocket();
            @Override
            public void run() {
                while (true) {
                    try(Scanner scanner = new Scanner(System.in)){
                        System.out.print("Enter stock: ");
                        
                        String stock = scanner.nextLine();
                        DatagramPacket outgoing = new DatagramPacket(stock.getBytes(), stock.length(), InetAddress.getLocalHost(), 12345);
                        socket.send(outgoing);
                        if(scanner.nextLine() == "Quit"){
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            
        };

        //To receive/listen for confimation form sever
        Runnable incomingPacket = new Runnable() {
            DatagramSocket socket2 = new DatagramSocket();
            @Override
            public void run() {
                byte[] buffer = new byte[1024];
                DatagramPacket incoming = new DatagramPacket(buffer, 0, 1024);
                try {
                    socket2.receive(incoming);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String incomingMessage = new String(incoming.getData(), 0 , incoming.getLength());
                System.out.println(incomingMessage);
                    
            }
            
            
        };


        Thread sending = new Thread(outgoingPacket);
        Thread recieving = new Thread(incomingPacket);

        sending.start();
        recieving.start();

    }
}
