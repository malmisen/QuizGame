/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

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
    public String submit(@RequestParam("username") String username, @RequestParam("password") String password, ModelMap model) {
        return null;
    }
}
