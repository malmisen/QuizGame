/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Sébastien Malmberg
 */
public class Question {
    private int questionId;
    private String question;
    private HashMap<String, Alternative> alternatives;
    private HashMap<String,String> correct;
    
    public Question(){};    
    
    public void setQuestionId(int id){
        questionId = id;
    }
    
    public int getQuestionId(){
        return questionId;
    }
    
    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAlternatives(HashMap<String, Alternative> alt) {
        alternatives = alt; 
    }
    
    public void setCorrectAnswer(HashMap<String,String> correct){
        this.correct = correct;
        
        Set<String> setKeys = alternatives.keySet();
        Object[] keys = setKeys.toArray();
        
        for(int i = 0; i < keys.length; i++){
            Alternative alt = alternatives.get(String.valueOf(keys[i]));
            if(correct.get(String.valueOf(keys[i])).equals("true")){
                alt.setIsCorrect(1);
            } else {
                alt.setIsCorrect(0);
            }
            
            alternatives.put(String.valueOf(keys[i]), alt);
        }
    }
    
    public String getQuestion(){
        return question;
    }
    
    public ArrayList<Alternative> getAlternatives(){
        Set<String> setKeys = alternatives.keySet();
        Object[] keys = setKeys.toArray();
        ArrayList<Alternative> alts = new ArrayList<>();
        for(int i = 0; i < keys.length; i++){
            Alternative alt = alternatives.get(String.valueOf(keys[i]));
            
            if(alt.getAlternative() != null || !(alt.getAlternative().equals("null"))){
                alts.add(alt);
            }
        }
        return alts;
    }
    
    /**
     * This method checks if the correct alternatives were given by the client for this question
     * 
     * @param answers the answers the client gave to this question
     * @return 0 if 1 or more alternatives were incorrect, else return 1
     */
    public int calculatePoints(String[] answers){
        Set<String> setKeys = alternatives.keySet();
        Object[] keys = setKeys.toArray();
        
        int totalNumberOfCorrectAnswers = 0;
        int j = 0;
        for(int i = 0; i < keys.length; i++){
            if(correct.get(String.valueOf(keys[i])).equals("true")){
                totalNumberOfCorrectAnswers++;
            }
        }
        
        String[] correctAnswers = new String[totalNumberOfCorrectAnswers];
        for(int i = 0; i < keys.length; i++){
            if(correct.get(String.valueOf(keys[i])).equals("true")){
                Alternative alt = alternatives.get(String.valueOf(keys[i]));
                correctAnswers[j++] = alt.getAlternative();
            }
        }
        
        boolean success = compareLists(answers, correctAnswers);
        if(success) return 1;
        else        return 0;
        
    }
    
    /**
     * Compare two lists, if they are equals the client answered this question correctly
     * 
     * 
     * @param givenAnswers the answers given by the client
     * @param correctAnswers the correct answers for this question
     * @return true if all given answers are correct else false
     */
    private static boolean compareLists(String[] givenAnswers, String[] correctAnswers){
        
        int numberOfCorrectAnswers = correctAnswers.length;
        int totalPoints = 0;
        
        //The answers in each list are not neccessarilly on the same positions
        for(int i = 0; i < givenAnswers.length; i++){
            String answer = givenAnswers[i];
            for(int j = 0; j < correctAnswers.length; j++){
                if(correctAnswers[j].equals(answer)){
                    totalPoints++;
                }
            }
        }
        
        return totalPoints == numberOfCorrectAnswers;

    }
    
    
    
    
    
    
    
}
