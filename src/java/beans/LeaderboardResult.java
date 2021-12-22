package beans;

import java.io.Serializable;
/**
 *
 * @author Francesco
 */
public class LeaderboardResult implements Serializable {
    
    private String username;
    private int totalScore;
    
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
}
