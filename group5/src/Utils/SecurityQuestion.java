package Utils;

import java.util.HashMap;
import java.util.Map;

public class SecurityQuestion {
    private Map<Integer, String> securityQuestions = new HashMap<>();

    /**
     * Default constructor for the SecurityQuestion class.
     * Initializes the security questions map to used while registering and sign in.
     */
    public SecurityQuestion() {
        securityQuestions.put(1, "What is your mother's maiden name?");
        securityQuestions.put(2, "What was the name of your first pet?");
        securityQuestions.put(3, "What was the name of your elementary school?");
    }
    /**
     * Retrieves the security question  by using id provided.
     *
     * @param id the ID of the security question
     * @return the security question as a string.
     */
    public String getQuestion(int id) {
        return securityQuestions.get(id);
    }
}
