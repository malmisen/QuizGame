/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;

import beans.Alternative;
import beans.Question;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Sébastien Malmberg
 */
public class QuizDAO {
    
    //Quizzes
    
    //Questions
    
    //Alternatives
    
    //Prepared Statements
    PreparedStatement createNewQuizStmt;
    PreparedStatement createQuestionsStmt;
    PreparedStatement createAlternativesStmt;
    PreparedStatement countRowsInQuizzesStmt;
    PreparedStatement selectQuestionsByQuizIdStmt;
    
    //Db connection handler
    private DBHandler db;
    
    public QuizDAO(){
        try{
            db = DBHandler.getInstance();
            prepareStatements();
        }catch(SQLException e){
            System.out.println("Something went wrong when preparing statement!");
            e.printStackTrace();
        }
    }
   
    
    private int getMostRecentlyAddedQuizId(){
        ResultSet set = null;
        int rows = -1;
        try{
            set = countRowsInQuizzesStmt.executeQuery();
            while(set.next()){
                rows = Integer.parseInt(set.getString("COUNT(*)"));
            }
        } catch (SQLException e){
            System.out.println("Could not retrieve the number of rows in the Quizzes table");
            e.printStackTrace();
        }     
        return rows;
    }
    
    private ArrayList<Integer> getIdOfAddedQuestions(int quizId){
        ArrayList<Integer> listOfQuestionIds = new ArrayList<>();
        ResultSet set = null;
        try{
            selectQuestionsByQuizIdStmt.setInt(1, quizId);
            set = selectQuestionsByQuizIdStmt.executeQuery();
            while(set.next()){
                listOfQuestionIds.add(set.getInt("id"));
            }
        } catch (SQLException e){
            System.out.println("Could not retrieve list of question ids");
            e.printStackTrace();
        }
        
        return listOfQuestionIds;
    }
    
    public void createAlternatives(ArrayList<Question> questions, int quizId){
        int updatedRows = 0;
        try{
            for(int i = 0; i < questions.size(); i++){
                ArrayList<Alternative> alts = questions.get(i).getAlternatives();
                for(int j = 0; j < alts.size(); j++){
                    createAlternativesStmt.setString(1, alts.get(j).getAlternative());
                    createAlternativesStmt.setInt(2, alts.get(j).getIsCorrect());
                    createAlternativesStmt.setInt(3, questions.get(i).getQuestionId());
                    createAlternativesStmt.setInt(4, quizId);
                    updatedRows += createAlternativesStmt.executeUpdate();
                }
            }
        } catch (SQLException e){
            System.out.println("Could not create alternatives");
            e.printStackTrace();
        }
    }
    
    public ArrayList<Integer> createQuestions(ArrayList<Question> questions, int quizId){
        int updatedRows = 0;
        try{
            for(int i = 0; i < questions.size(); i++){
                createQuestionsStmt.setString(1, questions.get(i).getQuestion());
                createQuestionsStmt.setInt(2, quizId);
                updatedRows += createQuestionsStmt.executeUpdate();
            }
        } catch (SQLException e){
            System.out.println("Could not create question");
            e.printStackTrace();
        }
        if(updatedRows < 0){
            System.out.println("No question rows were updated");
            return null;
        }
        ArrayList<Integer> questionIds = getIdOfAddedQuestions(quizId);
        
        return questionIds;
    }
    
    
    public int createNewQuiz(String category, String difficulty){
        int updatedRows = 0;
        try{
            createNewQuizStmt.setString(1, category);
            createNewQuizStmt.setString(2, difficulty);
            updatedRows = createNewQuizStmt.executeUpdate();
        } catch (SQLException e){
            System.out.println("Could not create new quiz");
            e.printStackTrace();
        }
        
        if(updatedRows > 0){
            int quizId = getMostRecentlyAddedQuizId();
            if(quizId == -1){
                System.out.println("Could not count rows, something went wrong");
                return -1;
            } else {
                return quizId;
            }
        } else {
            System.out.println("Updated rows is < 0 something went wrong");
            return -1;
        }
    }
    
    private void prepareStatements() throws SQLException{
        createNewQuizStmt = db.getCon().prepareStatement("INSERT INTO quizzes (category, difficulty) VALUES (?,?)");
        createQuestionsStmt = db.getCon().prepareStatement("INSERT INTO questions (text, quiz_id) VALUES (?,?)");
        createAlternativesStmt = db.getCon().prepareStatement("INSERT INTO alternative (text, isCorrect, question_id, quiz_id) VALUES (?,?,?,?)");
        countRowsInQuizzesStmt = db.getCon().prepareStatement("SELECT COUNT(*) FROM quizzes");
        selectQuestionsByQuizIdStmt = db.getCon().prepareStatement("SELECT id FROM questions WHERE quiz_id = ?");
    }
}
