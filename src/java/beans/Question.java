/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author regularclip
 */
public class Question {
    private String question;
    private HashMap<String, String> alternatives;
    private HashMap<String,String> correct;
    
    public Question(){};    
    
    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAlternatives(HashMap<String, String> alt) {
        alternatives = alt; 
    }
    
    public void setCorrectAnswer(HashMap<String,String> correct){
        this.correct = correct;
    }
    
    public String getQuestion(){
        return question;
    }
    
    public String[] getAlternatives(){
        Object[] alts = alternatives.values().toArray();
        String[] strAlts = new String[alts.length];
        for(int i = 0; i < alts.length; i++){
            strAlts[i] = String.valueOf(alts[i]);
        }
        return strAlts;
    }
    
    
    
    
    
    
    
}
