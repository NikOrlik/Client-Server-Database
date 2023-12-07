package working_modes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
//++
public class AdminWork {
    static BufferedReader in;
    static PrintWriter out;
    static String inputLine;
    static String log,password,email,country,name,surname,age;
    static Connection connection;
    static PreparedStatement stmt = null;
    static ResultSet rs= null;
    static String sql=null; // for sql request
    public AdminWork(BufferedReader inputReader, PrintWriter outputWriter, Connection dbConnection) {
        this.in = inputReader;
        this.out = outputWriter;
        this.connection = dbConnection;
    }
    public static int adminWork(String log) throws SQLException, IOException {
        while (true) {
            out.println("\nWhat you want to do? \n (1)-See all available books \n (2)-Add new book \n (3)-open users book \n (4)-add book to users library  \n (5)-Delete user \n (6)-Add user \n (7)-Display information about users \n (8)-Delete book \n (.) back to main menu");
            inputLine = in.readLine();
            if (inputLine.equals("1")) {
                out.println("List of all available books: \n title  | author  | year");
                sql = "SELECT * FROM library";
                stmt = connection.prepareStatement(sql);
                int i =1;
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    int year = rs.getInt("year");
                    out.println("-----------------------------------");
                    out.print(i+") ");
                    out.println(title + " | " + author + " | " + year);
                    i++;
                }
            }       //+
            else if (inputLine.equals("2")) {
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
                }
                catch (SQLException | IOException se) {
                    se.printStackTrace();
                }
            } //+
            else if(inputLine.equals("3")) {
                out.println("Write users login: ");
                String usersLogin= in.readLine();if(!CheckLoginExists.checkLoginExists(usersLogin)){out.println("This user does not exist"); continue;}
                out.println("List of "+log+" books \n title  | author  | year");
                sql = "SELECT * FROM "+usersLogin+"_library";
                stmt = connection.prepareStatement(sql);
                int i =1;
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    int year = rs.getInt("year");
                    out.println("-----------------------------------");
                    out.print(i+") ");
                    out.println(title + " | " + author + " | " + year);
                    i++;
                }

            }
            else if(inputLine.equals("4")) {
                out.println("Enter users login: ");
                String usersLogin = in.readLine();if(!CheckLoginExists.checkLoginExists(usersLogin)){out.println("This user does not exist"); continue;}

                sql = "INSERT INTO "+usersLogin+"_library (title, author, year) VALUES (?, ?, ?)";
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

                if(stmt.executeUpdate()>0)
                    out.println("Successfully executed");else out.println("Error");
            } //+
            else if(inputLine.equals("5")) {
                out.println("Enter user login: ");
                String userlogin = in.readLine();
                if (CheckLoginExists.checkLoginExists(userlogin)) {
                    out.println("Are you sure? (Yes or no)");
                    if (in.readLine().toLowerCase().charAt(0) == 'y') {
                        //delete information about user in 'usersec' table (where we saved all information about users)
                        String sql = "DELETE FROM usersec WHERE login = ?";
                        stmt = connection.prepareStatement(sql);
                        stmt.setString(1, userlogin);
                        int r1=stmt.executeUpdate();
                        //Turn off safe mode for droping (deleting) users 'library'
                        String sql1 = "SET sql_safe_updates = 1";
                        stmt=connection.prepareStatement(sql1);
                        stmt.executeUpdate();
                        //Drop the users library
                        String sql2 = "Drop table "+userlogin+"_library";
                        stmt=connection.prepareStatement(sql2);
                        int r2= stmt.executeUpdate();

                        if (r1 > 0 && r2==0) out.println("Succesfull deletion ");else out.println("Error");
                    } else out.println("No records deleted");
                } else {
                    out.println("Login does not exist");
                }
            } //+
            else if(inputLine.equals("6")) {

                out.println("Write information about user : ");

                out.println("Name: ");
                name = in.readLine(); if(name.isEmpty()) {out.println("Name must be entered"); continue;}
                out.println("Surname: ");
                surname = in.readLine(); if(surname.isEmpty()) {out.println("Surname must be entered"); continue;}
                out.println("Login: ");
                log = in.readLine(); if(log.isEmpty()) {out.println("Login must be entered"); continue;}
                if(!CheckLoginExists.checkLoginExists(log))
                {
                    out.println("Password: ");
                    password = in.readLine(); if (password.isEmpty()){out.println("Password is must be entered");continue;}

                    out.println("age: ");
                    age = in.readLine();

                    out.println("Email: ");
                    email = in.readLine();

                    out.println("Country: ");
                    country = in.readLine();
                    out.println("Are you sure ? (Yes or No)");
                    String resp=in.readLine().toLowerCase();

                    if (resp.charAt(0) == 'y' && password!=null) {
                        try{
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

                            sql = "CREATE TABLE IF NOT EXISTS "+log+"_library (" +
                                    "title VARCHAR(45) NOT NULL ," +
                                    "author VARCHAR(45) NOT NULL," +
                                    "year VARCHAR(45) NOT NULL)";

                            PreparedStatement stm;
                            stm=connection.prepareStatement(sql);
                            stm.executeUpdate();

                        }
                        catch(SQLException se)
                        {
                            se.printStackTrace();
                        }
                    }

                    else{
                        out.println("information not entered");
                    }

                }
                else
                {
                    out.println("This login is already existed");
                }} //+
            else if(inputLine.equals("7")){
                out.println("Information about users :");
                sql = "SELECT * FROM usersec";
                stmt = connection.prepareStatement(sql);
                int i =1;
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    String login = rs.getString("login");
                    String password = rs.getString("password");
                    String name= rs.getString("name");
                    String surname= rs.getString("Surname");
                    String age= rs.getString("age");
                    String email= rs.getString("email");
                    String country= rs.getString("country");
                    out.println("-----------------------------------");
                    out.print(i+") ");
                    out.println("Login: "+ login+"\nPassword: "+password+"\nName: "+name+ "\nSurname: "+surname+"\nAge: "+age+"\nEmail: "+email+"\nCountry: "+country );
                    i++;
                }
            }//+
            else if(inputLine.equals("8"))
            {
                out.println("Delete book from (1)-userLibrary \nor \n(2)from common library");
                String resp=in.readLine().toLowerCase();

                Statement statement = connection.createStatement();
                statement.executeUpdate("SET SQL_SAFE_UPDATES=0");

                if(!resp.isEmpty() && resp.charAt(0)=='1')
                {
                    out.println("Enter user log: ");
                    String userlogin = in.readLine();if(!CheckLoginExists.checkLoginExists(userlogin)){out.println("This user does not exist"); continue;}
                    out.println("Enter books name: ");
                    String booksName = in.readLine(); if(booksName == null || booksName.isBlank()){out.println("Enter books name");continue;}
                    String sql2 = "DELETE FROM "+userlogin+"_library WHERE title = '"+booksName+"'";
                    stmt=connection.prepareStatement(sql2);
                    int r= stmt.executeUpdate();

                    if (r > 0) out.println("Succesfull deletion ");else out.println("Error");
                }
                if(!resp.isEmpty() && resp.charAt(0)=='2')
                {

                    out.println("Enter books name: ");
                    String booksName = in.readLine(); if(booksName == null || booksName.isBlank()){out.println("Enter books name");continue;}
                    String sql2 = "DELETE FROM library WHERE title = '"+booksName+"'";
                    stmt=connection.prepareStatement(sql2);
                    int r= stmt.executeUpdate();

                    if (r > 0) out.println("Succesfull deletion ");else out.println("Error");
                }
                else if(resp.isEmpty() || (resp.charAt(0)!='1' && resp.charAt(0)!='2')){
                    out.println("Wrong command");
                }

            }
            else if (inputLine.equals(".")) {
                return 1;
            }
            else {
                out.println("Wrong command");
            }
        }
    }

}
