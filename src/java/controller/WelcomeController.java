package controller;

import api.ApiHandler;
import api.Parser;
import beans.Alternative;
import beans.Question;
import beans.Quiz;
import db.UserDAO;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping; // for mapping request path to invoking method
import org.springframework.web.bind.annotation.RequestMethod; // for mapping request method (GET, POST) to controller method 
import org.springframework.web.bind.annotation.RequestParam; // for retrieving HTTP parameters sent (GET, POST)
import org.springframework.web.bind.annotation.SessionAttributes; // same as setattribute on a HttpSession-object


/**
 * @author Francesco Giangiulio
 */

@Controller
public class WelcomeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String start(ModelMap model) { //method name is not mapped
        
        ApiHandler api = new ApiHandler();
        //Question[] questions = api.getQuestions("linux", "easy");
        ArrayList<Question> questions = api.getQuestions("linux", "easy");
        Quiz quiz = new Quiz();
        quiz.setQuestions(questions);
        
        model.addAttribute("quiz", quiz);
        
       
      
        return "quizzing.html";
        //return "index";
    }

    @RequestMapping(value = "/quiz", method = RequestMethod.GET)
    public String submit(@RequestParam("username") String username, @RequestParam("password") String password, ModelMap model) {
        
        System.out.println("Start");
        beans.User user = new beans.User();
        user.setUsername(username);
        user.setPassword(password);
        /* fetch user from DB */
        UserDAO dao = new UserDAO();
        beans.User dbUser = dao.getUserByUsername(username);

        if (dbUser.getUsername().equals(user.getUsername())&&(dbUser.getPassword().equals(user.getPassword()))) {
            model.addAttribute("user", dbUser);
            return "quiz.html";
        } else {
            return "index";
        }
    }
}
