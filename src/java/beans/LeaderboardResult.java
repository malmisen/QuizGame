package beans;

import java.io.Serializable;
/**
 *
 * @author Francesco
 */
public class LeaderboardResult implements Serializable {
    
    private int user_id;
    private String username;
    private int totalScore;
    
    public void setUserId(int id) {
        this.user_id = id;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
    
    public String getUsername() {
        return username;
    }
    
    public int getTotalScore() {
        return totalScore;
    }
    
    public int getUserId(){
        return user_id;
    }
}
