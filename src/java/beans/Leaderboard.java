package beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Francesco
 */
public class Leaderboard implements Serializable {
    
    ArrayList<LeaderboardResult> leaderboard;
    int size;
    
    public Leaderboard(){
        size = 0;
        leaderboard = new ArrayList<LeaderboardResult>();        
    }
    
    public void addResults(LeaderboardResult result){
        size++;
        leaderboard.add(result);
    }
    
    public LeaderboardResult getLeaderboardResult(int i) {
        return leaderboard.get(i);
    }

    public ArrayList<LeaderboardResult> getLeaderboard() {
        return leaderboard;
    }
}
