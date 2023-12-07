package working_modes;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class CheckLoginExists {
    static BufferedReader in;
    static PrintWriter out;
    static String inputLine;
    static ServerSocket serverSocket;
    static Socket socket;
    static String log,password,email,country,name,surname,age;
    static Connection connection;
    static PreparedStatement stmt = null;
    static ResultSet rs= null;
    static String sql=null; // for sql request
    public  static  boolean checkLoginExists(String login) throws SQLException // method to find out if we already have such a login in the database
    {
        String url = "jdbc:mysql://localhost:3306/zadanie";
        String username = "root";
        String passwordD = "07022005Orl";
        connection = DriverManager.getConnection(url, username, passwordD);
        sql = "SELECT * FROM usersec WHERE login = ?";
        stmt = connection.prepareStatement(sql);
        stmt.setString(1, login);
        rs = stmt.executeQuery();

        boolean res= (rs.next())? true:false ;
        return res;
    }
}
