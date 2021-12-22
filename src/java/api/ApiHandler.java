/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import beans.Question;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


/**
 * This class is responsible for querying data to the REST API https://quizapi.io/
 * 
 * @author Sébastien Malmberg
 */
public class ApiHandler {    
    //A private token used to authenticate to the API
    private static final String TOKEN = "lb4CnB2Zr7mNM9nUNsi4LYTBka9xTOG6wzYNDZyY";
   
    private static int BUFFER_SIZE = 1024;    
    /**
     * A default constructor
     */
    public ApiHandler(){};

    /**
     * Categories example: Linux, DevOps, Networking, Programming, Cloud, Docker
     * Difficulties: easy, medium, hard.
     * 
     * Returns a list of type Question. 
     * The list will contain 10 questions. 
     * Each question carries 6 alternatives (Some might be null). 
     * See Object/Class Question in package beans.
     * 
     * Find examples here: https://quizapi.io/docs/1.0/overview#examples of the JSON object returned by the API
     * 
     * @param category the desired question category
     * @param difficulty the desired question difficulty
     * @return Question[], 10 random questions fetched from the API 
     */
    public ArrayList<Question> getQuestions(String category, String difficulty){
        String command = "curl https://quizapi.io/api/v1/questions -G -d apiKey="+TOKEN+" -d limit=10 -d category="+category+" -d difficulty="+difficulty;
        try {
            Parser parser = new Parser();
            ArrayList<Question> questions = parser.parseJSON(callApi(command));
            return questions;
        } catch (IOException ex) {
            System.out.println("Something went wrong when requesting EASY linux questions ");
            ex.printStackTrace();
        }
        return null;
    }
    
    /**
     * Queries the API https://quizapi.io/ 
     * @param command , the command used to query the API
     * @return the data received from the API as a string.
     * @throws IOException 
     */
    private static String callApi(String command) throws IOException{
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        processBuilder.directory(new File("C:\\Users\\franc\\OneDrive\\Datateknik (180hp)\\ID1212 - Nätverksprogrammering\\files"));
        Process process = processBuilder.start();    
        
        InputStream inputStream = process.getInputStream();
        StringBuilder builder = new StringBuilder();
        byte[] fromApiBuffer = new byte[BUFFER_SIZE];
        
        while(true){
            int fromApiLength = inputStream.read(fromApiBuffer);
            builder.append(new String(fromApiBuffer, 0, fromApiLength, StandardCharsets.UTF_8));
            if(builder.charAt(builder.length() - 1) == ']'){
                break;
            }
            
            fromApiBuffer = new byte[BUFFER_SIZE];
        }
        
        return builder.toString();
    }
    
}
