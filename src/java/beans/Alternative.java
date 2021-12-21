/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import java.io.Serializable;

/**
 *
 * @author Sébastien Malmberg
 */
public class Alternative implements Serializable{
    private int id;
    private String alternative;
    private int isCorrect;
    
    public Alternative(){};
    
    public void setIsCorrect(int correct){
        isCorrect = correct;
    }
    
    public int getIsCorrect(){
        return isCorrect;
    }
    
    public void setAlternative(String str){
        alternative = str;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }
    public String getAlternative(){
        return alternative;
    }
    
}
