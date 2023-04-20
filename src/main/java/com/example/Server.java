package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        int port = 8000;
        ServerSocket server = new ServerSocket(port);
        System.out.println("Server open");
        while (true) {
            Socket socket = server.accept();
            System.out.println("Client is connected");
            // Receive data from client
            InputStream input = socket.getInputStream();
            ObjectInputStream objectInput = new ObjectInputStream(input);

            while (true) {
                Object object;
                try {
                    object = objectInput.readObject();
                    if (object == null)
                        break;
                    Object[] data = (Object[]) object;
                    int idScore = (int) data[0];
                    String name = (String) data[1];
                    String idStudent = (String) data[2];
                    BigDecimal averageFirst = (BigDecimal) data[3];
                    BigDecimal averageSecond = (BigDecimal) data[4];
                    BigDecimal averageThird = (BigDecimal) data[5];
                    BigDecimal midterm = averageFirst.add(averageSecond).add(averageThird)
                            .divide(BigDecimal.valueOf(3.0), 2, RoundingMode.HALF_UP);

                    // Send response to data
                    OutputStream out = socket.getOutputStream();
                    ObjectOutputStream objectOut = new ObjectOutputStream(out);
                    objectOut.writeObject(new Object[] { idScore, name, idStudent, averageFirst, averageSecond,
                            averageThird, midterm });
                    objectOut.flush();
                    System.out.println("Send data to client");
                    System.out.printf(
                            "ID_SCORE = %d, Name = %s, idStudent = %s, score 1 = %.2f, score 2 = %.2f, score 3 = %.2f, midterm = %.2f\n",
                            idScore, name, idStudent, averageFirst, averageSecond, averageThird, midterm);

                } catch (ClassNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
            // Send end of data to client
            OutputStream out = socket.getOutputStream();
            ObjectOutputStream objectOut = new ObjectOutputStream(out);
            objectOut.writeObject(null);
            objectOut.flush();
            // Close client
            objectInput.close();
            objectOut.close();
            server.close();
        }
    }
}
