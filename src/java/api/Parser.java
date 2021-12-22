/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import beans.Question;
import org.json.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This class parses data received from the ApiHandler
 * 
 * @author Sébastien Malmberg
 */
public class Parser {
    
    public Parser(){}
    
    
    
    public Question[] getQuiz(String category, String difficulty){
        ApiHandler api = new ApiHandler();
        String json = api.getQuestions("linux", "easy");
        System.out.println(json);
        Question[] questions = getQuestions(json);
        return questions;
    }
  
    private static Question[] getQuestions(String data){
        System.out.println(data);
        Object obj = null;
        try {
            obj = new JSONParser().parse(data);
        } catch (ParseException ex) {
            System.out.println("Could not parse data");
            ex.printStackTrace();
        }
        Question[] questions = new Question[10];
        JSONArray json = (JSONArray) obj;
        for(int i = 0; i < json.size(); i++){
            Object o = json.get(i);
            JSONObject jsonObject = (JSONObject) o;
            
            Question question = new Question();
            question.setQuestion(jsonObject.get("question").toString());
            
            Object oAlternatives = jsonObject.get("answers");
            JSONObject jsonAlternatives = (JSONObject) oAlternatives;
            String[] altTemp = jsonAlternatives.toString().replace("{", "").replace("}","").split(",\"");
            HashMap<String, String> alternatives = new HashMap<>();
            for(int j = 0; j < altTemp.length; j++){
                String[] alt = altTemp[j].split(":");
                alternatives.put(alt[0].replace("\"",""), alt[1].replace("\"", ""));
            }
            question.setAlternatives(alternatives);

            Object correctAnswers = jsonObject.get("correct_answers");
            JSONObject jsonCorrectAnswers = (JSONObject) correctAnswers;
            String[] correctTemp = jsonCorrectAnswers.toString().replace("{", "").replace("}","").replace("\"","").split(",");
            HashMap<String, String> correct = new HashMap<>();
            for(int j = 0; j < correctTemp.length; j++){
                String[] alt = correctTemp[j].split(":");
                correct.put(alt[0].replace("\"", "").replace("_correct", ""), alt[1].replace("\"", ""));
            } 
            question.setCorrectAnswer(correct);
            questions[i] = question;
            
        }
       
        return questions;
    }
    
    
}
