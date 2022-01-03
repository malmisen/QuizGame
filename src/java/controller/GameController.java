/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import api.ApiHandler;
import beans.Alternative;
import beans.Question;
import beans.Quiz;
import beans.User;
import beans.UserResult;
import beans.UserResults;
import db.QuizDAO;
import db.UserDAO;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * This controller handles game logic.
 * 
 * @author Sébastien Malmberg
 */
@Controller
public class GameController {
    
    /**
     * Provides client with the quiz to be played
     * @param category, the quiz category
     * @param difficulty, the quiz difficulty
     * @param user, the current user
     * @param model, the model used to passed data down to the view
     * @return quizzing page
     */
    @RequestMapping(value = "/playQuiz", method = RequestMethod.GET)
    public String playNewQuiz(@RequestParam("category") String category, 
                                  @RequestParam("difficulty") String difficulty,
                                  @RequestParam("userId") int userId,
                                  ModelMap model) {
        
        ApiHandler api = new ApiHandler();
        //Question[] questions = api.getQuestions("linux", "easy");
        ArrayList<Question> questions = api.getQuestions(category, difficulty);
        QuizDAO dao = new QuizDAO();
        int quizId = dao.createNewQuiz(category, difficulty);
        System.out.println("QUIZID RECEIVED: " + quizId);
        ArrayList<Integer> questionIds = dao.createQuestions(questions, quizId);
        System.out.println("Size of questionIds: " + questionIds.size());
        //Update each question with their respective id received from the database
        for(int i = 0; i < questionIds.size(); i++){
            questions.get(i).setQuestionId(questionIds.get(i));
        }
        
        dao.createAlternatives(questions, quizId);
        
        //database now contains everything
        Quiz quiz = dao.getQuiz(quizId);
        
        ArrayList<Question> quests = dao.getQuestionsByQuizId(quizId);
        quiz.setQuestions(quests);
        
        quests = dao.getAlternativesByQuestionId(quests);
        
        quiz.setQuestions(quests);
        quiz.setId(quizId);
        
        model.addAttribute("quiz", quiz);
        model.addAttribute("userId", userId);
        model.addAttribute("score", "score");
        model.addAttribute("difficulty", difficulty);
        model.addAttribute("category", category);
        return "quizzing.html";
    }
    
    @RequestMapping(value= "/playSavedQuiz", method = RequestMethod.GET)
    public String playSavedQuiz(@RequestParam("userId") int userId, @RequestParam("quizId") int quizId, ModelMap model){
        QuizDAO quizDAO = new QuizDAO();
        Quiz quiz = quizDAO.getQuiz(quizId);
        ArrayList<Question> quests = quizDAO.getQuestionsByQuizId(quizId);
        quiz.setQuestions(quests);
        quests = quizDAO.getAlternativesByQuestionId(quests);
        quiz.setQuestions(quests);
        quiz.setId(quizId);
        
        /* FETCH CURRENT ANSWERS GIVEN */
        
        for(int i = 0; i < quests.size(); i++){
             ArrayList<Alternative> alternatives = quests.get(i).getAlternativesList();
             ArrayList<String> clientAnswers = quizDAO.getClientAnswers(userId, quests.get(i).getQuestionId());
             if(clientAnswers.size() > 0){
                for(int j = 0; j < alternatives.size(); j++){
                    for(int k = 0; k < clientAnswers.size(); k++){
                        if(clientAnswers.get(k).equals(alternatives.get(j).getAlternative())){
                            alternatives.get(j).setChecked(true);
                        }
                    }
                }
             }
        }
       
        quiz.setQuestions(quests);
        
        model.addAttribute("quiz", quiz);
        model.addAttribute("difficulty", quiz.getDifficulty());
        model.addAttribute("category", quiz.getCategory());
        model.addAttribute("score", "score");
        model.addAttribute("userId", userId);
        
        return "quizzing.html";
    }
   
    /**
     * Saves the current clients ongoing quiz
     */
    @RequestMapping(value = "/result", method = RequestMethod.POST, params = "action=save")
    public String saveQuiz(HttpServletRequest req, ModelMap model) {
        Map<String, String[]> params = req.getParameterMap();
        String[] quizIdString = params.get("QuizId");
        String[] userIdString = params.get("userId");
        
        int quizId = Integer.parseInt(quizIdString[0]);
        int userId = Integer.parseInt(userIdString[0]);

        QuizDAO dao = new QuizDAO();
        Quiz quiz = dao.getQuiz(quizId);
        ArrayList<Question> questions = dao.getQuestionsByQuizId(quizId);
        questions = dao.getAlternativesByQuestionId(questions);
        quiz.setQuestions(questions);

        for (int i = 0; i < questions.size(); i++) {
            String[] clientAnswers = params.get(String.valueOf(questions.get(i).getQuestionId()));

            if (clientAnswers != null) {
                dao.storeAnswers(userId, questions.get(i).getQuestionId(), clientAnswers);
            }
        }

        boolean exists = dao.getOnGoingQuiz(quizId);
        if (!exists) {
            dao.addOnGoingQuiz(userId, quizId);
        }

        /*  Need to be changed  */
        User user = new User();

        UserDAO userDAO = new UserDAO();
        User dbUser = userDAO.getUserById(userId);

        model.addAttribute("username", dbUser.getUsername());
        return "redirect:http://localhost:8080/QuizGame/home";

    }
    
    
     /**
     * Provides client with the results of the quiz
     * 
     * @param req
     * @param quizId
     * @param model
     * @return 
     */
    @RequestMapping(value = "/result", method = RequestMethod.POST, params="action=submit")
    public String getResults(HttpServletRequest req, 
                            @RequestParam("QuizId") int quizId, 
                            @RequestParam("userId") int userId, 
                            @RequestParam("difficulty") String difficulty,
                            @RequestParam("category") String category,
                            ModelMap model) {
        //Contains client answers
        Map<String, String[]> parameters = req.getParameterMap();
        
        
        //Fetch questions and alternatives for the played guiz from DB to calculate points
        QuizDAO dao = new QuizDAO();
        Quiz quiz = dao.getQuiz(quizId);
        ArrayList<Question> questions = dao.getQuestionsByQuizId(quizId);
        questions = dao.getAlternativesByQuestionId(questions);
     

        
        //Calc client results
        int score = 0; 
        for(int i = 0; i < questions.size(); i++){
            String[] clientAnswers = parameters.get(String.valueOf(questions.get(i).getQuestionId()));
            if(clientAnswers != null){ 
                int temp = questions.get(i).calculatePoints(clientAnswers);
                if(temp > 0){
                    score += temp;
                    questions.get(i).setIsCorrectlyAnswered(true);
                }
                dao.storeAnswers(userId, questions.get(i).getQuestionId(), clientAnswers);
            }
        }
        for(int i = 0; i < questions.size(); i++){
             ArrayList<Alternative> alternatives = questions.get(i).getAlternativesList();
             ArrayList<String> clientAnswers = dao.getClientAnswers(userId, questions.get(i).getQuestionId());
             if(clientAnswers.size() > 0){
                for(int j = 0; j < alternatives.size(); j++){
                    for(int k = 0; k < clientAnswers.size(); k++){
                        if(clientAnswers.get(k).equals(alternatives.get(j).getAlternative())){
                            alternatives.get(j).setChecked(true);
                        }
                    }
                }
             }
        }
        
        dao.updateQuizCurrentScore(score, quizId);
        quiz.setQuestions(questions);
        System.out.println("Score: " + score);
        
        User user = new User();
        user.setId(userId);
        UserResult result = new UserResult();
        result.setQuizId(quizId);
        result.setScore(score);
        UserDAO userdao = new UserDAO();
        result = userdao.updateUserResults(user, result);
        
        model.addAttribute("quiz", quiz);
        model.addAttribute("userId", userId);
        model.addAttribute("score", score);
        model.addAttribute("difficulty", difficulty);
        model.addAttribute("category", category);
        return "result.html";
    }
    
    
    
}
