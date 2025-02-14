package Utils;
import Model.User;

import java.util.*;

public class TempBuffer {
    /**
     * Utility class for temporarily buffering user data.
     */
    Map<String,User> usersMap = new HashMap<>();
    IOHandler io;

    public TempBuffer(){
        io = new IOHandler();
    }
    /**
     * Loads user profile data from a file into the temporary buffer.
     *
     * @return a map containing user IDs as keys and User objects as values
     */
    public Map<String,User> LoadUserProfileData(){
        this.usersMap = io.ReadUserDataFromFile();
        return usersMap;
    }

}
