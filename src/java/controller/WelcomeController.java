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
            
            model.addAttribute("username", dbUser.getUsername());
           
            return "redirect:http://localhost:8080/QuizGame/home";
          
        } else {
            return "index.html";
        }
    }
}
