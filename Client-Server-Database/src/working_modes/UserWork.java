package working_modes;

import java.io.IOException;
import java.sql.SQLException;
import java.io.*;
import java.sql.*;


public class UserWork {
    static BufferedReader in;
    static PrintWriter out;
    static String inputLine;
    static String log,password,email,country,name,surname,age;
    static Connection connection;
    static PreparedStatement stmt = null;
    static ResultSet rs= null;
    static String sql=null; // for sql request
    public UserWork(BufferedReader inputReader, PrintWriter outputWriter, Connection dbConnection) {
        this.in = inputReader;
        this.out = outputWriter;
        this.connection = dbConnection;
    }

    public int userWork(String log) throws IOException, SQLException {
        while (true) {
            out.println("\nWhat you want to do? \n (1)-See all available books \n (2)-Add new book \n (3)-open your books \n (4)-add book to your library  \n (.) back to main menu");

            inputLine = in.readLine();
            if (inputLine.equals("1")) {
                out.println("List of all available books: \n title  | author  | year");
                sql = "SELECT * FROM library";
                stmt = connection.prepareStatement(sql);
                int i = 1;
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    int year = rs.getInt("year");
                    out.println("-----------------------------------");
                    out.print(i + ") ");
                    out.println(title + " | " + author + " | " + year);
                    i++;
                }


            } else if (inputLine.equals("2")) {
                try {
                    sql = "INSERT INTO library (title, author, year) VALUES (?, ?, ?)";
                    stmt = connection.prepareStatement(sql);


                    String title;
                    String author;
                    String year;

                    out.println("Enter books name: ");
                    title = in.readLine();
                    out.println("Enter books author: ");
                    author = in.readLine();
                    out.println("Enter books year: ");
                    year = in.readLine();

                    stmt.setString(1, title);
                    stmt.setString(2, author);
                    stmt.setString(3, year);

                    stmt.executeUpdate();
                } catch (SQLException se) {
                    se.printStackTrace();
                }

                continue;
            } else if (inputLine.equals("3")) {
                out.println("List of your books \n title  | author  | year");
                sql = "SELECT * FROM " + log + "_library";
                stmt = connection.prepareStatement(sql);
                int i = 1;
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    int year = rs.getInt("year");
                    out.println("-----------------------------------");
                    out.print(i + ") ");
                    out.println(title + " | " + author + " | " + year);
                    i++;
                }

            } else if (inputLine.equals("4")) {
                sql = "INSERT INTO " + log + "_library (title, author, year) VALUES (?, ?, ?)";
                stmt = connection.prepareStatement(sql);


                String title;
                String author;
                String year;

                out.println("Enter books name: ");
                title = in.readLine();
                out.println("Enter books author: ");
                author = in.readLine();
                out.println("Enter books year: ");
                year = in.readLine();

                stmt.setString(1, title);
                stmt.setString(2, author);
                stmt.setString(3, year);

                stmt.executeUpdate();
            } else if (inputLine.equals(".")) {
                return 1;
            } else {
                out.println("Wrong command");
            }
        }
    }
}
