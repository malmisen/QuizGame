package controller;

import api.Parser;
import beans.Question;
import db.UserDAO;
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
    public String start() { //method name is not mapped
        
        Parser parser = new Parser();
        Question[] questions = parser.getQuiz("linux", "easy");
        for(int i = 0; i < questions.length; i++){
            System.out.println("Question:\n" + questions[i].getQuestion());
            String[] alternatives = questions[i].getAlternatives();
            System.out.println("Alternatives:");
            for(int j = 0; j < alternatives.length; j++){
                System.out.println(alternatives[j]);
            }
        }
        
        return "index";
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
