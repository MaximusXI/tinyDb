package Utils;
import Constants.Constants;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptionAlgorithm {
    MessageDigest md;
    public EncryptionAlgorithm(){}
    /**
     * Hashes the credentials data to store into file, to keep data secure (userId,password) to be hashed
     *
     * @param data the data to be hashed
     * @return the hashed data as a hexadecimal string
     * @throws RuntimeException if the specified algorithm is not found
     */
    public String hashData(String data) {
        try {
            md =  MessageDigest.getInstance(Constants.ALGORITHMINSTANCE);
            md.update(data.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
