import  working_modes.*;

import java.io.*;
import java.sql.*;
import java.net.*;

public class Server {
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

    public static void main(String[] args) throws IOException, SQLException {

        // connect to database
        String url = "jdbc:mysql://localhost:3306/zadanie";
        String username = "root";
        String passwordD = "07022005Orl";
        connection = DriverManager.getConnection(url, username, passwordD);

        //work with server connection
        serverSocket = new ServerSocket(1111);
        System.out.println("waiting...");
        socket = serverSocket.accept();
        System.out.println("successfull connection !");

        // variable for writing and reading data from the socket
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        inputLine = "";


        while (true) {
            out.println("What do you want ? (Sing up or Log in or Quit)");// start menu
            String mainResponce = in.readLine().toLowerCase();
            System.out.println(mainResponce);
            if (mainResponce.isEmpty()) {
                out.println("Please enter a valid choice.");
                continue;
            }
            char choice = mainResponce.charAt(0);

            if (choice == 'l') {
                out.println("Enter login: ");
                log = in.readLine().trim();
                System.out.println("Login is " + log);
                if (log.isEmpty()) {
                    out.println("Login must be entered");
                    continue;
                } //method "trim" deletes all spaces
                out.println("Enter password: ");
                password = in.readLine().trim();
                if (password.isEmpty()) {
                    out.println("Login must be entered");
                    continue;
                }
                boolean foundUser = false, foundAdmin = false; // variable to indicate what work the program should do (for admin or for user)
                //search login and password for user
                while (true) {
                    sql = "SELECT login, password FROM usersec";
                    stmt = connection.prepareStatement(sql);
                    rs = stmt.executeQuery(sql);
                    while (rs.next()) {
                        String dbName = rs.getString("login");
                        String dbPassword = rs.getString("password");
                        if (dbName.equals(log) && dbPassword.equals(password)) {
                            foundUser = true;
                            break;
                        }
                    }
                    //search login and password for admin
                    sql = "SELECT login, password FROM adminsec";
                    stmt = connection.prepareStatement(sql);
                    ResultSet Adminrs = stmt.executeQuery(sql);
                    while (Adminrs.next()) {
                        String dbName = Adminrs.getString("login");
                        String dbPassword = Adminrs.getString("password");
                        if (dbName.equals(log) && dbPassword.equals(password)) {
                            foundAdmin = true;
                            break;
                        }
                    }


                    if (foundUser && !foundAdmin) {
                        out.println("Welcome to application dear " + log);
                        UserWork userwork = new UserWork(in, out, connection);
                        if (userwork.userWork(log) == 1)
                            break;
                    } else if (!foundUser && foundAdmin) {
                        out.println("You are in admin mode");
                        AdminWork adminwork = new AdminWork(in,out,connection);
                        if(adminwork.adminWork(log) == 1)
                            break;
                    } else {
                        out.println("Wrong password or login");
                        break;
                    }
                }
            } else if (choice == 'q') {
                out.println("Connection lost");
                System.out.println("Con lost");

                in.close();
                out.close();
                stmt.close();
                rs.close();
                serverSocket.close();
                socket.close();
                break;
            }
            //+
            else if (choice == 's') {
                //Enter pers. data
                while(true){
                    out.println("Name: ");
                    String name = in.readLine();
                    System.out.println(name);
                    if(name == null || name.trim().isEmpty() )
                    {
                        out.println("You must enter name");
                        break;
                    }

                    out.println("Surname: ");

                    surname = in.readLine().trim();

                    if(surname == null || surname.trim().isEmpty())
                    {
                        out.println("You must enter surname");
                        break;
                    }

                    out.println("Login: ");
                    log = in.readLine();
                    if(log == null || log.trim().isEmpty() )
                    {
                        System.out.println("You must enter login");
                        break;
                    }


                    if (!CheckLoginExists.checkLoginExists(log)) {
                        out.println("Password: ");
                        password = in.readLine();
                        if(password == null || password.trim().isEmpty())
                        {
                            System.out.println("You must enter password");
                            break;
                        }


                        out.println("age: ");
                        age = in.readLine();
                        if(age == null || age.trim().isEmpty())
                        {
                            System.out.println("You must enter age");
                            break;
                        }


                        out.println("Email: ");
                        email = in.readLine();

                        out.println("Country: ");
                        country = in.readLine();

                    }
                    else
                    {
                        out.println("This user has already existed"); break;
                    }
                    out.println("Are you sure ? (Yes or No)");

                    String resp = in.readLine().toLowerCase();

                    if (!resp.isEmpty() && resp!= null && resp.charAt(0) == 'y' && password != null ) {
                        try {
                            sql = "INSERT INTO usersec (login, password,name,surname,age, email,country) VALUES (?, ?, ?, ?, ?, ?, ?)";
                            stmt = connection.prepareStatement(sql);

                            stmt.setString(1, log);
                            stmt.setString(2, password);
                            stmt.setString(3, name);
                            stmt.setString(4, surname);
                            stmt.setString(5, age);
                            stmt.setString(6, email);
                            stmt.setString(7, country);

                            stmt.executeUpdate();

                            sql = "CREATE TABLE IF NOT EXISTS " + log + "_library (" +
                                    "title VARCHAR(45) NOT NULL ," +
                                    "author VARCHAR(45) NOT NULL," +
                                    "year VARCHAR(45) NOT NULL)";

                            PreparedStatement stm;
                            stm = connection.prepareStatement(sql);
                            stm.executeUpdate();

                        } catch (SQLException se) {
                            se.printStackTrace();
                        }
                    } else {
                        out.println("information not entered");
                    }

                }
            }
            else
            {
                out.println("Please enter a valid choice");
            }

        }

    }

}


