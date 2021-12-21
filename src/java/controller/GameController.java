/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import api.ApiHandler;
import beans.Question;
import beans.Quiz;
import beans.User;
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
    public String getQuizzingPage(@RequestParam("Category") String category, 
                                  @RequestParam("Difficulty") String difficulty,
                                  @RequestParam("User") User user,
                                  ModelMap model) {
        
        ApiHandler api = new ApiHandler();
        //Question[] questions = api.getQuestions(category, difficulty);
       // model.addAttribute("questions", questions);
        model.addAttribute("difficulty", difficulty);
        model.addAttribute("category", category);
        model.addAttribute("user", user);
        return "quizzing.html";
    }
    
    /**
     * Provides client with the results of the quiz
     * 
     * @param req
     * @param quiz
     * @param questions
     * @param model
     * @return 
     */
    @RequestMapping(value = "/result", method = RequestMethod.POST)
    public String getResults(HttpServletRequest req, @RequestParam("Quiz") String questions, ModelMap model) {
        Map<String, String[]> parameters = req.getParameterMap();
        // parameters.remove("Quiz");
        System.out.println(questions);
        /*
        for(int i  = 0; i < questions.size(); i++){
            System.out.println(questions.get(i).getQuestion());
        }
        int score = 0;
        // A quiz always consists of 10 questions (thus this map will have keys from 0 - 9)
        // Each key maps to 1 or more values. The values represent the answer the client chose.
        for(int i = 0; i < parameters.size(); i++){
            String[] values = parameters.get(String.valueOf(i));
            score += questions.get(i).calculatePoints(values);
        }
        System.out.println("Score: " + score);
        
        */
        return "quizzing.html";
    }
    
    /**
     * Saves the current clients ongoing quiz
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void saveQuiz(@RequestParam("Questions") Question[] questions){
        
    }
    
    
}
