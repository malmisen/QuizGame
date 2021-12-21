/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import beans.Question;
import java.util.ArrayList;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 *
 * @author Sébastien Malmberg
 */
@Component
public class QuestionConverter implements Converter<String, ArrayList<Question>> {

    @Override
    public ArrayList<Question> convert(String s) {
        System.out.println("Attempting to convert " + s + " into an ArrayList");
        return null;
    }

    
}
