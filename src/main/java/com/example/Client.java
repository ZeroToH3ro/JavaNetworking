package com.example;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Hello world!
 *
 */
public class Client {
    public static void main(String[] args) {
        final int port = 8000;
        final String host = "localhost";

        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            // Create socket and connect client to server
            Socket socket = new Socket(host, port);
            System.out.println("Connected to server");
            // Connect database
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/managestudent";
            String user = "postgres";
            String password = "minh21052002@";
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
            // Create sql querry
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM student");
            // Send data to server
            OutputStream out = socket.getOutputStream();
            ObjectOutputStream objectOut = new ObjectOutputStream(out);
            // Read data in database and store in Object
            System.out.println("My Data:");
            while (rs.next()) {
                int idScore = rs.getInt("id_score");
                String name = rs.getString("fullname");
                String idStudent = rs.getString("id_student");
                BigDecimal averageFirst = rs.getBigDecimal("average_1");
                BigDecimal averageSecond = rs.getBigDecimal("average_2");
                BigDecimal averageThird = rs.getBigDecimal("average_3");

                Object[] data = { idScore, name, idStudent, averageFirst, averageSecond, averageThird };
                objectOut.writeObject(data);
                objectOut.flush();
                System.out.printf(
                        "ID_SCORE = %d, Name = %s, idStudent = %s, score 1 = %.2f, score 2 = %.2f, score 3 = %.2f\n",
                        idScore, name, idStudent, averageFirst, averageSecond, averageThird);
            }
            objectOut.writeObject(null); // End of data
            objectOut.flush();
            System.out.println("All data is sent");
            System.out.println("\nData Receive From Server");
            // Client get data from server
            while (true) {
                InputStream input = socket.getInputStream();
                ObjectInputStream objectIn = new ObjectInputStream(input);
                Object obj = objectIn.readObject();
                if (obj == null)
                    break;
                Object[] data = (Object[]) obj;
                System.out.printf(
                        "ID_SCORE = %d, Name = %s, idStudent = %s, score 1 = %.2f, score 2 = %.2f, score 3 = %.2f, Mid Term = %.2f\n",
                        (int) data[0], (String) data[1], (String) data[2], ((BigDecimal) data[3]),
                        ((BigDecimal) data[4]), ((BigDecimal) data[5]), ((BigDecimal) data[6]));
            }
            // Close server
            rs.close();
            stmt.close();
            conn.close();
            // objectIn.close();
            objectOut.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
