/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


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
     * Returns JSON as a string. The JSON contains 10 questions on specified category.
     * Categories example: Linux, DevOps, Networking, Programming, Cloud, Docker
     * Difficulties: easy, medium, hard.
     * 
     * Find examples here: https://quizapi.io/docs/1.0/overview#examples
     * 
     * @param category the desired question category
     * @param difficulty the desired question difficulty
     * @return JSON as a string
     */
    public String geQuestions(String category, String difficulty){
         String command = "curl https://quizapi.io/api/v1/questions -G -d apiKey="+TOKEN+" -d limit=10 -d category="+category+" -d difficulty="+difficulty;
        try {
            return callApi(command);
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
        processBuilder.directory(new File("/home"));
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
