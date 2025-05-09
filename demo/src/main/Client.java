package main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException{
        DatagramSocket socket = new DatagramSocket();

        Thread sendStock = new Thread(new Runnable() {

            @Override
            public void run() {
                boolean running = true;
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter Stock Symbol: ");
                while (running) { 
                    try {
                        
                        InetAddress address = InetAddress.getLocalHost();
            
                        String stock = scanner.nextLine();
                        byte[] buffer = stock.getBytes();
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 12345);
                        
                        socket.send(packet);

                        if(scanner.next().equalsIgnoreCase("Quit")){
                            running = false;
                            String quit = "Quit";
                            byte[] bufferEnd = quit.getBytes();
                            DatagramPacket endPacket = new DatagramPacket(bufferEnd, bufferEnd.length, address, 12345);
                            socket.send(endPacket);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                scanner.close();
                socket.close();
            }
            
        });

        
        sendStock.start();

    }
}
