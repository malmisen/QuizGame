/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;

import beans.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class User Data Access Object (UserDAO).
 * This class is responsible for querying data regarding Users from the database.
 * It can fetch existing Users and Create new Users.
 * 
 * @author Sébastien Malmberg
 */
public class UserDAO {
    
    //USER TABLE
    private static final String USER_TABLE_NAME             = "users";              //TABLE NAME
    private static final String USER_COLUMN_ID_NAME         = "id";                 //PRIMARY KEY
    private static final String USER_COLUMN_USERNAME_NAME   = "username";
    private static final String USER_COLUMN_PASSWORD_NAME   = "password";
    private static final String USER_COLUMN_FIRSTNAME_NAME  = "firstName";
    private static final String USER_COLUMN_LASTNAME_NAME   = "lastName";
    private static final String USER_COLUMN_EMAIL_NAME      = "email";
    
    private DBHandler db;
    private PreparedStatement createNewUserStmt;
    private PreparedStatement searchExistingUserByUsernameStmt;
    private PreparedStatement searchExistingUserByEmailStmt;

    /**
     * Constructor.
     * Instantiates this class.
     * Gets instance of DBHandler.java
     * Initiates the prepared statements (queries).
     */
    public UserDAO(){
        try{
            db = DBHandler.getInstance();
            prepareStatements();
        }catch(SQLException e){
            System.out.println("Something went wrong when preparing statement!");
            e.printStackTrace();
        }
    }
    
    /**
     * This method queries the user table in the database for an existing user.
     * It searches by Username.
     * 
     * @param username, the username to be found
     * @return the found User, if none was found returns User object with Attributes set to null.
     */
    public User getUserByUsername(String username) {
        ResultSet result = null;
        User dbUser = new User();
        try{
            searchExistingUserByUsernameStmt.setString(1, username);
            result = searchExistingUserByUsernameStmt.executeQuery();
            while(result.next()){
                dbUser.setId(result.getInt(USER_COLUMN_ID_NAME));
                dbUser.setFirstName(result.getString(USER_COLUMN_FIRSTNAME_NAME));
                dbUser.setLastName(result.getString(USER_COLUMN_LASTNAME_NAME));
                dbUser.setEmail(result.getString(USER_COLUMN_EMAIL_NAME));
                dbUser.setUsername(result.getString(USER_COLUMN_USERNAME_NAME));
                dbUser.setPassword(result.getString(USER_COLUMN_PASSWORD_NAME));
            }
        }catch(SQLException e){
            System.out.println("Something went wrong when executing query");
            e.getStackTrace();
        }
        return dbUser;
    }
    
    /**
     * This method queries the user table in the database for an existing user.
     * It searches by Email.
     * 
     * @param email, the email to be found
     * @return the found User, if none was found returns User object with Attributes set to null.
     */
    public User getUserByEmail(String email) {
        ResultSet result = null;
        User dbUser = new User();
        try{
            System.out.println("Executing query");
            searchExistingUserByEmailStmt.setString(1, email);
            result = searchExistingUserByEmailStmt.executeQuery();
            System.out.println("Query executed!");
            while(result.next()){
                dbUser.setId(result.getInt(USER_COLUMN_ID_NAME));
                dbUser.setFirstName(result.getString(USER_COLUMN_FIRSTNAME_NAME));
                dbUser.setLastName(result.getString(USER_COLUMN_LASTNAME_NAME));
                dbUser.setEmail(result.getString(USER_COLUMN_EMAIL_NAME));
                dbUser.setUsername(result.getString(USER_COLUMN_USERNAME_NAME));
                dbUser.setPassword(result.getString(USER_COLUMN_PASSWORD_NAME));
               
            }
        }catch(SQLException e){
            System.out.println("Something went wrong when executing query");
            e.getStackTrace();
        }
        return dbUser;
    }
   
    /**
     * This method attempts to create a new user IF no user with the given username and or email already exists.
     * 
     * @param user, the user to be created.
     * @return True if the user was successfully created, else false.
     */
    public String createNewUser(User user) {
        //check if User with this email already exists
        User existingUser = getUserByEmail(user.getEmail());
        if(existingUser.getEmail() != null){
            return "This email already exists";
        }
        existingUser = getUserByUsername(user.getUsername());
        if(existingUser.getUsername() != null){
            return "This username already exists";
        }
       
        int updatedRows = 0;//keep count of the number of updated rows
        try{
            createNewUserStmt.setString(1, user.getFirstName());
            createNewUserStmt.setString(2, user.getLastName());
            createNewUserStmt.setString(3, user.getEmail());
            createNewUserStmt.setString(4, user.getUsername());
            createNewUserStmt.setString(5, user.getPassword());
            updatedRows = createNewUserStmt.executeUpdate();
            if(updatedRows != 1){
                System.out.println("COULD NOT UPDATE ROW");
            }
        }catch(SQLException e){
            System.out.println("Woops, something went wrong when creating new user in DB");
            e.getStackTrace();
        }
        if(updatedRows == 1){
            return "Successfully created new user!";
        }
        
        return "Could not create new user";   
    }  
    
    private void prepareStatements() throws SQLException{
        searchExistingUserByUsernameStmt = db.getCon().prepareStatement("SELECT * FROM users WHERE username = ?");
        searchExistingUserByEmailStmt = db.getCon().prepareStatement("SELECT * FROM users WHERE email = ?");
        createNewUserStmt = db.getCon().prepareStatement("INSERT INTO users (firstName,lastName,email,username,password) VALUES (?,?,?,?,?)");
    }    
}
