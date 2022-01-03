package controller;

import beans.Leaderboard;
import beans.LeaderboardResult;
import beans.Quiz;
import beans.User;
import beans.UserResults;
import db.QuizDAO;
import db.UserDAO;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Francesco
 */
@Controller
public class HomepageController {
    
//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    public String start(){
//        return "index";
//    }
    
  
    
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String submit(
            @RequestParam("username") String username,
            ModelMap model) {
        
            UserDAO userDAO = new UserDAO();
            User dbUser = userDAO.getUserByUsername(username);
            model.addAttribute("user", dbUser);
            model.addAttribute("id", dbUser.getId());
            
            String[] categories = {"Linux", "DevOps", "Docker", "Networking", "Programming"};
            model.addAttribute("categories", categories);
        
            String[] difficulties = {"Easy", "Medium", "Hard"};
            model.addAttribute("difficulties", difficulties);
            
            UserResults results = userDAO.getUserResults(dbUser);
            model.addAttribute("results", results.getResults());
            
            QuizDAO quizDAO = new QuizDAO();
            ArrayList<Quiz> onGoingQuizzes = quizDAO.getOnGoingQuizzes(dbUser.getId());
            model.addAttribute("onGoingQuizzes", onGoingQuizzes);
            
            Leaderboard leaderboard = userDAO.getTotalScore(); 
            model.addAttribute("leaderboard", leaderboard.getLeaderboard());
        
        
            return "homepage.html";
    }

}
