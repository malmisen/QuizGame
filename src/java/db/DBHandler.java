/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class is responsible for connecting to the Database. 
 * The class utilizes the Singleton pattern to ensure that only one instance of class is used at a time.
 * 
 * 
 * @author Sébastien Malmberg
 */
public class DBHandler {
    
    private static Connection con = null;
    private static DBHandler instance;
    private static Lock lock = new ReentrantLock();
    private static final String dbUsername = "root";
    private static final String dbPassword = "regular";
    
    /**
     * A constructor which initiates the connection to the database when called.
     */
    public DBHandler(){
        if(con == null){
            //Start database connection
            connect();
        }
    }
    
    /**
     * Method which connects to the database
     */
    private void connect(){
        System.out.println("Connecting to database...");
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", dbUsername, dbPassword);
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
        
    }
    
    /**
     * Method gets instance of singleton.
     * 
     * @return an instance of this class. 
     */
    public static DBHandler getInstance(){
        if(instance == null){
            synchronized(lock){
                if(instance == null){
                    instance = new DBHandler();
                }
            }
        }
        return instance;
    }
    
    /**
     * 
     * @return the current database connection
     */
    public Connection getCon(){
        return con;
    }
    
   
    
}
