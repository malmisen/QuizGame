package beans;

import java.io.Serializable;

/**
 *
 * @author Francesco
 */
public class UserResult implements Serializable {
    
    private String category;
    private String difficulty;
    private int score;
    private int quizId;

    
    public void setDifficulty(String dif){
        difficulty = dif;
    }
    
    public String getDifficulty(){
        return difficulty;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }

    public void setQuizId(int id) {
        this.quizId = id;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getCategory() {
        return category;
    }

    public int getScore() {
        return score;
    }

    public int getQuizId() {
        return quizId;
    }
}
