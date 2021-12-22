/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;

import beans.Leaderboard;
import beans.LeaderboardResult;
import beans.User;
import beans.UserResult;
import beans.UserResults;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class User Data Access Object (UserDAO).
 * This class is responsible for querying data regarding Users from the database.
 * It can fetch existing Users and Create new Users.
 * 
 * @author Sébastien Malmberg
 */
public class UserDAO {
    
    //USER TABLE
    private static final String USER_TABLE_NAME                 = "users";              //TABLE NAME
    private static final String USER_COLUMN_ID_NAME             = "id";                 //PRIMARY KEY
    private static final String USER_COLUMN_USERNAME_NAME       = "username";
    private static final String USER_COLUMN_PASSWORD_NAME       = "password";
    private static final String USER_COLUMN_FIRSTNAME_NAME      = "firstName";
    private static final String USER_COLUMN_LASTNAME_NAME       = "lastName";
    private static final String USER_COLUMN_EMAIL_NAME          = "email";
    private static final String USER_COLUMN_SCORE_NAME          = "score";
    
    //QUIZZES TABLE
    private static final String QUIZZES_COLUMN_SUBJECT_NAME     = "subject";
    private static final String QUIZZES_COLUMN_ID_NAME          = "id";
    
    //RESULTS TABLE    
    private static final String RESULTS_COLUMN_SCORE_NAME       = "score";
    private static final String RESULTS_COLUMN_TOTAL_SCORE_NAME = "totalScore";
    
    private DBHandler db;
    private PreparedStatement getUserQuizResults;
    private PreparedStatement updateUserQuizResultsStatement;   
    private PreparedStatement createNewUserStmt;
    private PreparedStatement searchExistingUserByUsernameStmt;
    private PreparedStatement searchExistingUserByEmailStmt;
    private PreparedStatement updateUserTotalScoreStatement;
    private PreparedStatement getUserTotalScoreStatement;
    private PreparedStatement getUserResultByUserAndQuizIdStmt;

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
            searchExistingUserByEmailStmt.setString(1, email);
            result = searchExistingUserByEmailStmt.executeQuery();
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
    
    
    public UserResult getResultByUserAndQuizId(int userId, int quizId){
        ResultSet set = null;
        UserResult result = new UserResult();
        try{
            getUserResultByUserAndQuizIdStmt.setInt(1, quizId);
            getUserResultByUserAndQuizIdStmt.setInt(2, userId);
            set = getUserResultByUserAndQuizIdStmt.executeQuery();
            while(set.next()){
                result.setQuizId(set.getInt("id"));
                result.setScore(set.getInt("score"));
                result.setCategory(set.getString("category"));
                result.setDifficulty(set.getString("difficulty"));
            }
        } catch (SQLException e){
            System.out.println("Could not fetch results from result table with userId: " + userId + " and quizId: " + quizId);
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * 
     * @param user the user who played
     * @param result the result of the quiz
     * @return UserResult, and updated result which also contains category and difficulty
     */
    public UserResult updateUserResults(User user, UserResult result) {

        System.out.println("INSIDE UPDATE FUNCTION: ");
        System.out.println("quizId: " + result.getQuizId());
        int updatedRows = 0;
        try {
            updateUserQuizResultsStatement.setInt(1, result.getScore());
            updateUserQuizResultsStatement.setInt(2, user.getId());
            updateUserQuizResultsStatement.setInt(3, result.getQuizId());
            updatedRows = updateUserQuizResultsStatement.executeUpdate();
            if (updatedRows != 1) {
                System.out.println("Could not update results table");
            }
        } catch (SQLException ex) {
            System.out.println("Something went wrong when updating the results table");
            ex.printStackTrace();
        }
        
        UserResult res = getResultByUserAndQuizId(user.getId(), result.getQuizId());
        return res;
    }
    
    public boolean updateUserTotalScore(User user, LeaderboardResult result) {
        int updatedRows = 0;
        try {
            updateUserTotalScoreStatement.setInt(1, user.getId());
            updateUserTotalScoreStatement.setInt(2, result.getTotalScore());
            updatedRows = updateUserQuizResultsStatement.executeUpdate();
            if (updatedRows == 1) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
//    public LeaderboardResult getLeaderboardResult (LeaderboardResult result) {
//        ResultSet resultSet = null;
//        LeaderboardResult results = new LeaderboardResult();
//        LeaderboardResult r = null;
//        try {
//            getUserTotalScoreStatement.setInt(1, result.getTotalScore());
//            resultSet = getUserQuizResults.executeQuery();
//            int count = 0;
//            while (resultSet.next()) {
//                result = new LeaderboardResult();
//                result.setTotalScore(resultSet.getInt(RESULTS_COLUMN_TOTAL_SCORE_NAME));
//                results.setTotalScore(result);
//                count++;
//            }
//            
//        }catch(SQLException e){
//            System.out.println("Something went wrong when fetching leaderboard results");
//        }
//        
//        return results;
//        }
    
    
    public UserResults getUserResults(User user) {
        ResultSet resultSet = null;
        UserResults results = new UserResults();
        UserResult result = null;
        try {
            getUserQuizResults.setInt(1, user.getId());
            resultSet = getUserQuizResults.executeQuery();
            int count = 0;
            while (resultSet.next()) {
                result = new UserResult();
                result.setCategory(resultSet.getString(QUIZZES_COLUMN_SUBJECT_NAME));
                result.setScore(resultSet.getInt(RESULTS_COLUMN_SCORE_NAME));
                result.setQuizId(resultSet.getInt(QUIZZES_COLUMN_ID_NAME));

                results.addResults(result);
                count++;
            }
            
        }catch(SQLException e){
            System.out.println("Something went wrong when fetching quiz results");
        }
        
        return results;
    }
    
    private void prepareStatements() throws SQLException{
        searchExistingUserByUsernameStmt = db.getCon().prepareStatement("SELECT * FROM users WHERE username = ?");
        searchExistingUserByEmailStmt = db.getCon().prepareStatement("SELECT * FROM users WHERE email = ?");
        createNewUserStmt = db.getCon().prepareStatement("INSERT INTO users (firstName,lastName,email,username,password) VALUES (?,?,?,?,?)");
        getUserQuizResults = db.getCon().prepareStatement("SELECT q.subject, q.id,r.score FROM quizzes AS q INNER JOIN results AS r WHERE q.id = r.quiz_id AND r.user_id = ?");
        updateUserQuizResultsStatement = db.getCon().prepareStatement("INSERT INTO results (score, user_id, quiz_id) VALUES (?,?,?)");
        updateUserTotalScoreStatement = db.getCon().prepareStatement("INSERT INTO LeaderboardResult (user_id, total_score) VALUES (?,?)");
        getUserTotalScoreStatement = db.getCon().prepareStatement("TO DO");
        getUserResultByUserAndQuizIdStmt = db.getCon().prepareStatement("SELECT q.category, q.difficulty, q.id,r.score, r.user_id FROM quizzes AS q INNER JOIN results AS r WHERE q.id = ? AND r.user_id = ?");
    }    
}
