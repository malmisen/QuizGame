/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;

import beans.Alternative;
import beans.Question;
import beans.Quiz;
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
    PreparedStatement getQuizByIdStmt;
    PreparedStatement getQuestionsByQuizIdStmt;
    PreparedStatement getAlternativesByQuestionAndQuizIdStmt;
    PreparedStatement storeClientAnswers;
    
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
    
    public ArrayList<Question> getQuestionsByQuizId(int quizId){
        ArrayList<Question> questions = new ArrayList<>();
        ResultSet set = null;
        
        try{
            getQuestionsByQuizIdStmt.setInt(1, quizId);
            set = getQuestionsByQuizIdStmt.executeQuery();
            while(set.next()){
                Question q = new Question();
                q.setQuestion(set.getString("text"));
                q.setQuestionId(set.getInt("id"));
                questions.add(q);
            }
            
        } catch (SQLException e) {
            System.out.println("Could not fetch questions by quizId: " +quizId);
            e.printStackTrace();
        }
        
        return questions;
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
    
    public Quiz getQuiz(int quizId){
        Quiz quiz = new Quiz();
        ResultSet set = null;
        
        try{
            getQuizByIdStmt.setInt(1, quizId);
            set = getQuizByIdStmt.executeQuery();
            while(set.next()){
                quiz.setId(set.getInt("id"));
                quiz.setCategory(set.getString("category"));
                quiz.setDifficulty(set.getString("difficulty"));
            }
            
        } catch (SQLException e){
            System.out.println("Could not fetch quiz with id: " + quizId);
            e.printStackTrace();
        }
        
        return quiz;
    }
    
    public ArrayList<Question> getAlternativesByQuestionId(ArrayList<Question> questions){
        ResultSet set = null;
        
        try{
            for(int i = 0; i < questions.size(); i++){
                ArrayList<Alternative> alist = new ArrayList<>();
                questions.get(i).setAlternativesList(alist);
                getAlternativesByQuestionAndQuizIdStmt.setInt(1, questions.get(i).getQuestionId());
                set = getAlternativesByQuestionAndQuizIdStmt.executeQuery();
                while(set.next()){
                    Alternative alt = new Alternative();
                    alt.setAlternative(set.getString("text"));
                    alt.setId(set.getInt("id"));
                    alt.setIsCorrect(set.getInt("isCorrect"));
                    questions.get(i).addAlternativeToList(alt);
                }
            }
        } catch (SQLException e){
            System.out.println("Could not fetch alternatives");
            e.printStackTrace();
        }
        
        return questions;
        
    }
    
    public void storeAnswers(int userId, int questionId, String[] answers){
        int updatedRows = 0;
        try{
            for(int i = 0; i < answers.length; i++){
                storeClientAnswers.setString(1, answers[i]);
                storeClientAnswers.setInt(2, userId);
                storeClientAnswers.setInt(3, questionId);
                updatedRows += storeClientAnswers.executeUpdate();
            }
        } catch (SQLException e){
            System.out.println("Could not store client answers");
            e.printStackTrace();
        }
        
        System.out.println("Updated clientAnswers, #rows: " + updatedRows);
    }
    
    
    private void prepareStatements() throws SQLException{
        createNewQuizStmt = db.getCon().prepareStatement("INSERT INTO quizzes (category, difficulty) VALUES (?,?)");
        createQuestionsStmt = db.getCon().prepareStatement("INSERT INTO questions (text, quiz_id) VALUES (?,?)");
        createAlternativesStmt = db.getCon().prepareStatement("INSERT INTO alternative (text, isCorrect, question_id, quiz_id) VALUES (?,?,?,?)");
        countRowsInQuizzesStmt = db.getCon().prepareStatement("SELECT COUNT(*) FROM quizzes");
        selectQuestionsByQuizIdStmt = db.getCon().prepareStatement("SELECT id FROM questions WHERE quiz_id = ?");
        getQuizByIdStmt = db.getCon().prepareStatement("SELECT * FROM quizzes WHERE id = ?");
        getQuestionsByQuizIdStmt = db.getCon().prepareStatement("SELECT id, text FROM questions WHERE quiz_id = ?");
        getAlternativesByQuestionAndQuizIdStmt = db.getCon().prepareStatement("SELECT id, text, isCorrect FROM alternative WHERE question_id = ?");
        storeClientAnswers = db.getCon().prepareStatement("INSERT INTO clientAnswers (text, user_id, question_id) VALUES (?,?,?)");
    }

    
}
