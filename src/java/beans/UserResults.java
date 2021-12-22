package beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Francesco
 */
public class UserResults implements Serializable {

    ArrayList<UserResult> results;
    int size;

    public UserResults() {
        size = 0;
        results = new ArrayList<UserResult>();
    }

    public void addResults(UserResult result) {
        size++;
        results.add(result);
    }

    public int getSize() {
        return size;
    }

    public UserResult getResult(int i) {
        return results.get(i);
    }

    public ArrayList<UserResult> getResults() {
        return results;
    }
}
