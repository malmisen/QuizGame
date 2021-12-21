/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Sébastien Malmberg
 */
public class Quiz implements Serializable{
    private int id;
    private ArrayList<Question> questions;
    
    public Quiz(){};
    
    public void setId(int id){ this.id = id; }
    public int getId(){ return id;}
    
    public void setQuestions(ArrayList<Question> questions){
        this.questions = questions;
    }
    
    public ArrayList<Question> getQuestions(){
        return questions;
    }
}
