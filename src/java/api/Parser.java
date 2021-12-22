/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import beans.Alternative;
import beans.Question;
import java.util.ArrayList;
import java.util.HashMap;
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
    

    /**
     * 
     * Returns an array list of questions
     * 
     * @param data the JSON to be parsed
     * @return 
     */
    public ArrayList<Question> parseJSON(String data){
        Object obj = null;
        System.out.println(data.replace(",", "\n"));
        try {
            obj = new JSONParser().parse(data);
        } catch (ParseException ex) {
            System.out.println("Could not parse data");
            ex.printStackTrace();
        }
        Question[] questions = new Question[10];
        ArrayList<Question> quests = new ArrayList<>();
        JSONArray json = (JSONArray) obj;
        for(int i = 0; i < json.size(); i++){
            Object o = json.get(i);
            JSONObject jsonObject = (JSONObject) o;
            
            Question question = new Question();
            question.setQuestion(jsonObject.get("question").toString());
            question.setQuestionId(i);
            
            Object oAlternatives = jsonObject.get("answers");
            JSONObject jsonAlternatives = (JSONObject) oAlternatives;
            String[] altTemp = jsonAlternatives.toString().replace("{", "").replace("}","").split(",\"");
            HashMap<String, Alternative> alternatives = new HashMap<>();
            for(int j = 0; j < altTemp.length; j++){
                String[] alt = altTemp[j].split(":");
                Alternative alternative = new Alternative();
                alternative.setId(j);
                alternative.setAlternative(alt[1].replace("\"", ""));
                alternatives.put(alt[0].replace("\"",""),alternative);
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
            //questions[i] = question;
            quests.add(question);
            
        }
       
        return quests;
    }
    
    
}
