package controller;

import beans.LeaderboardResult;
import beans.Quiz;
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
//            @RequestParam("username") String username,
            ModelMap model) {
        
        beans.User user = new beans.User();
        UserDAO dao = new UserDAO();
        beans.LeaderboardResult result = new beans.LeaderboardResult();
        
        user.setUsername("stevie");                          //change back to username
        beans.User dbUser = dao.getUserByUsername("stevie"); //change back to username
        model.addAttribute("user", dbUser);
        
        String[] categories = {"linux", "devOps", "docker", "networking", "programming"};
        model.addAttribute("categories", categories);
        
        String[] difficulties = {"easy", "medium", "hard"};
        model.addAttribute("difficulties", difficulties);
        
        model.addAttribute("id", user.getId());
        
        UserResults results = dao.getUserResults(dbUser);
        model.addAttribute("results", results.getResults());
        
        QuizDAO quizDAO = new QuizDAO();
        ArrayList<Quiz> onGoingQuizzes = quizDAO.getOnGoingQuizzes(dbUser.getId());
        model.addAttribute("onGoingQuizzes", onGoingQuizzes);
        
//        LeaderboardResult result = 
        
    return "homepage.html";
    }
}
