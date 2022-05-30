package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeSet;

public class Utils{

    public static byte[] hash256(byte[] content){
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
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

    public static String bytesToString(byte[] array) {
        return new String(array, StandardCharsets.UTF_8);
    }


    public static<E> E circularUpperBound(TreeSet<E> map, E value){
        E upperBound = map.ceiling(value);
        return upperBound == null ? map.first() : upperBound;
    }

    public static int indexOf(byte[] array, byte[] subArray){
        for(int i = 0; i < array.length; i++){
            boolean found = true;
            for(int j =0 ; j < subArray.length; j++){
                if(array[i+j] != subArray[j]){
                    found = false;
                    break;
                }
            }
            if(found){
                return i;
            }
        }
        return -1;
    }
}
