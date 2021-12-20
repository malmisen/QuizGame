/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import beans.User;
import db.UserDAO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * This class is a controller.
 * It provides the registration page.
 * It utilizes the User Data access object (UserDAO) to check if a user with given parameters already exists.
 * If there is no such user a new one is created. The client is then forwarded to the homepage.
 * 
 * @author Sébastien Malmberg
 */
@Controller
public class RegisterController {
    @RequestMapping(value = "/regPage", method = RequestMethod.GET)
    public String getRegister( ModelMap model) {
        return "register.html";
    }
    
    
    @RequestMapping(value = "/registerMe", method = RequestMethod.POST)
    public String submit(@RequestParam("firstName") String firstName, 
                         @RequestParam("lastName")  String lastName,
                         @RequestParam("email")     String email,
                         @RequestParam("username")  String username,
                         @RequestParam("password1") String password1,
                         @RequestParam("password2") String password2,
                         ModelMap model) {
        
        System.out.println("Firstname: " + firstName);
        System.out.println("Lastname: " + lastName);
        
        
        if(firstName.length()==0 || lastName.length()==0 || email.length() == 0 || username.length() == 0 || password1.length() == 0 || password2.length()== 0){
            model.addAttribute("Incorrect", "Please fill in all the fields");
            return "register.html";
        } else {
            if(!password1.equals(password2)){
                model.addAttribute("Incorrect", "passwords do not match");
                return "register.html";
            }else{
                User newUser = new User();
                newUser.setFirstName(firstName);
                newUser.setLastName(lastName);
                newUser.setEmail(email);
                newUser.setUsername(username);
                newUser.setPassword(password1);

                UserDAO dao = new UserDAO();
                String response = dao.createNewUser(newUser);

                if(!response.equals("Successfully created new user!")){
                    model.addAttribute("Incorrect", response);
                    return "register.html";
                }

                model.addAttribute("User", newUser);

                return "homepage.html";  
            }
            
        }
    }
}
