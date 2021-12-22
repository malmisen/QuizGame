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
    public String getQuizzingPage(@RequestParam("category") String category, 
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
        model.addAttribute("userId", 1);
        model.addAttribute("score", "score");
        model.addAttribute("difficulty", difficulty);
        model.addAttribute("category", category);
        return "quizzing.html";
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
        quiz.setQuestions(questions);
        questions = dao.getAlternativesByQuestionId(questions);
        quiz.setQuestions(questions);

        
        //Calc client results
        int score = 0; 
        for(int i = 0; i < questions.size(); i++){
            String[] clientAnswers = parameters.get(String.valueOf(questions.get(i).getQuestionId()));
            if(clientAnswers != null){ 
                score += questions.get(i).calculatePoints(clientAnswers);
            }
        }
        
        System.out.println("Score: " + score);
        
        User user = new User();
        user.setId(userId);
        UserResult result = new UserResult();
        result.setQuizId(quizId);
        result.setScore(score);
        UserDAO userdao = new UserDAO();
        result = userdao.updateUserResults(user, result);
        
        model.addAttribute("quiz", quiz);
        model.addAttribute("userId", 1);
        model.addAttribute("score", score);
        model.addAttribute("difficulty", difficulty);
        model.addAttribute("category", category);
        return "quizzing.html";
    }
    
    /**
     * Saves the current clients ongoing quiz
     */
    @RequestMapping(value = "/result", method = RequestMethod.POST, params="action=save")
    public String saveQuiz(HttpServletRequest req, ModelMap model){
        System.out.println("Insicde SAVE QUIZ");
        Map<String, String[]> params = req.getParameterMap();
        String[] quizIdString = params.get("QuizId");
        String[] userIdString = params.get("userId");
        int quizId = Integer.parseInt(quizIdString[0]);
        int userId = Integer.parseInt(userIdString[0]);
        
        System.out.println("QUIZ ID: " + quizId);
        System.out.println("User ID: " + userId);
        
        QuizDAO dao = new QuizDAO();
        Quiz quiz = dao.getQuiz(quizId);
        ArrayList<Question> questions = dao.getQuestionsByQuizId(quizId);
        questions = dao.getAlternativesByQuestionId(questions);
        quiz.setQuestions(questions);

        for(int i = 0; i < questions.size(); i++){
            String[] clientAnswers = params.get(String.valueOf(questions.get(i).getQuestionId()));

            if(clientAnswers != null){
                dao.storeAnswers(userId, questions.get(i).getQuestionId(), clientAnswers);
            }
        }
        
        dao.addOnGoingQuiz(userId, quizId);
        
        /*  Need to be changed  */
        User user = new User();
        UserDAO userdao = new UserDAO();
        user.setUsername("stevie");                          //change back to username
        beans.User dbUser = userdao.getUserByUsername("stevie"); //change back to username
        model.addAttribute("user", dbUser);
        
        String[] categories = {"linux", "devOps", "docker", "networking", "programming"};
        model.addAttribute("categories", categories);
        
        String[] difficulties = {"easy", "medium", "hard"};
        model.addAttribute("difficulties", difficulties);
        
        model.addAttribute("id", user.getId());
        
        UserResults results = userdao.getUserResults(dbUser);
        model.addAttribute("results", results.getResults());
        /*  ----        ------ ----------------------------------------*/
        
        ArrayList<Quiz> onGoingQuizzes = dao.getOnGoingQuizzes(userId);
        model.addAttribute("onGoingQuizzes", onGoingQuizzes);
        
        return "homepage.html";
        
    }
    
    
}
