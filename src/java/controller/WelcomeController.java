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
import org.springframework.web.bind.annotation.RequestMapping; // for mapping request path to invoking method
import org.springframework.web.bind.annotation.RequestMethod; // for mapping request method (GET, POST) to controller method 
import org.springframework.web.bind.annotation.RequestParam; // for retrieving HTTP parameters sent (GET, POST)
import org.springframework.web.bind.annotation.SessionAttributes; // same as setattribute on a HttpSession-object
import org.springframework.web.portlet.ModelAndView;


/**
 * @author Francesco
 */
@Controller
public class WelcomeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String start(ModelMap model) { //method name is not mapped
        
        /* MOVE BACK TO HOMEPAGECONTROLLER */
        /*
        beans.User user = new beans.User();
        UserDAO dao = new UserDAO();
        beans.Leaderboard result = new beans.Leaderboard();
        
        user.setUsername("stevie");                          //change back to username
        beans.User dbUser = dao.getUserByUsername("stevie"); //change back to username
        model.addAttribute("user", dbUser);
        
        String[] categories = {"Linux", "DevOps", "Docker", "Networking", "Programming"};
        model.addAttribute("categories", categories);
        
        String[] difficulties = {"Easy", "Medium", "Hard"};
        model.addAttribute("difficulties", difficulties);
        
        model.addAttribute("id", user.getId());
        
        UserResults results = dao.getUserResults(dbUser);
        model.addAttribute("results", results.getResults());
        
        Leaderboard leaderboard = dao.getTotalScore(); 
        model.addAttribute("leaderboard", leaderboard.getLeaderboard());
        */
        
        /* MOVE BACK TO HOMEPAGECONTROLLER */

        return "index"; // CHANGE BACK TO INDEX
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String submit(@RequestParam("username") String username, @RequestParam("password") String password, 
            ModelMap model) {
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        /* fetch user from DB */
        UserDAO dao = new UserDAO();
        User dbUser;
        dbUser = dao.getUserByUsername(username);

        if (dbUser.getUsername().equals(user.getUsername())&&(dbUser.getPassword().equals(user.getPassword()))) {
            /*
            model.addAttribute("user", dbUser);
            model.addAttribute("id", dbUser.getId());
            
            String[] categories = {"Linux", "DevOps", "Docker", "Networking", "Programming"};
            model.addAttribute("categories", categories);
        
            String[] difficulties = {"Easy", "Medium", "Hard"};
            model.addAttribute("difficulties", difficulties);
            
            UserResults results = dao.getUserResults(dbUser);
            model.addAttribute("results", results.getResults());
            
            QuizDAO quizDAO = new QuizDAO();
            ArrayList<Quiz> onGoingQuizzes = quizDAO.getOnGoingQuizzes(dbUser.getId());
            model.addAttribute("onGoingQuizzes", onGoingQuizzes);
            */
            
         
            
            
            model.addAttribute("username", dbUser.getUsername());
            
            return "redirect:http://localhost:8080/QuizGame/home";
          
            //return "homepage.html";
        } else {
            return "index.html";
        }
    }
}
