package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeSet;

public class Utils{

    public static byte[] hash256(byte[] content) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(content);
    }

    public static String bytesToHexString(byte[] array) {
        StringBuilder hexString = new StringBuilder(2*array.length);
        for (byte b : array) {
            String hex = Integer.toHexString(0xff & b).toUpperCase();
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }


    public static<E> E circularUpperBound(TreeSet<E> map, E value){
        E upperBound = map.ceiling(value);
        return upperBound == null ? map.first() : upperBound;
    }
}
