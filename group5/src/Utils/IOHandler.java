package Utils;
import Model.*;
import Constants.*;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Utility class for handling input and output operations related to user data.
 */
public class IOHandler {

    static EncryptionAlgorithm encryptionAlgorithm;
    public IOHandler(){
        encryptionAlgorithm = new EncryptionAlgorithm();
    }
    /**
     * Writes user object data to a file.
     *
     * @param user the user object containing the user data to be written
     * @return true if the user data is successfully written to the file, false otherwise
     */
    public boolean WriteUserDataToFile(User user){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.USERPROFILEFILENAME,true))) {
                StringBuilder record = new StringBuilder();
                record.append(user.getUserId()).append(Constants.DOLLARCHAR)
                        .append(user.getPassword()).append(Constants.DOLLARCHAR);

                for (Map.Entry<Integer, String> entry : user.getSecurityAnswers().entrySet()) {
                    record.append(entry.getKey()).append(Constants.EQUALSCHAR).append(entry.getValue()).append(Constants.COMMACHAR);
                }

                if (record.charAt(record.length() - 1) == Constants.COMMACHAR.toCharArray()[0]) {
                    record.deleteCharAt(record.length() - 1);
                }

                record.append(Constants.RECORD_SEPARATION);

                writer.write(record.toString());
                writer.newLine();

                return true;
        }
        catch(Exception io){
            System.err.println("Error in WriteUserDataToFile method."+io.getMessage());
            return false;
        }
    }
    /**
     * Reads user data from a userProfile.txt file.
     *
     * @return a map containing userId as key and User object as value
     * @throws RuntimeException if an I/O error occurs while reading the file
     */
    public Map<String,User> ReadUserDataFromFile(){
        Map<String, User> usersMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(Constants.USERPROFILEFILENAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userColumns = line.split(Constants.DOLLARCHAR_READFILE);

                User record = new User();
                record.setUserId(userColumns[0]);
                record.setPassword(userColumns[1]);

                //Split the Security Answers
                String[] securityAnswerPairs = userColumns[2].split(Constants.COMMACHAR);
                Map<Integer, String> securityAnswers = new HashMap<>();
                for (String pair : securityAnswerPairs) {
                    String[] keyValue = pair.split(Constants.EQUALSCHAR);
                    if (keyValue.length == 2) {
                        int key = Integer.parseInt(keyValue[0]);
                        String value = keyValue[1];
                        if(value.contains(Constants.RECORD_SEPARATION)){
                            value = value.replace(";","");
                        }
                        securityAnswers.put(key, value);
                    }
                }
                record.setSecurityAnswers(securityAnswers);
                usersMap.put(record.getUserId(), record);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return usersMap;
    }
}
