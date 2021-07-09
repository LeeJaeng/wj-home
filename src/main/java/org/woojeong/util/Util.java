package org.woojeong.util;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Util {
    public static String createRandStr(int length) {
        String result = "";

        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < length; i++) {
            result += letters.charAt((int)(Math.random() * letters.length()));
        }
        return result;
    }

    public static String md5Hash (String str) {
        String result = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digested = md5.digest(str.getBytes());
            result = DatatypeConverter.printHexBinary(digested).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Map simplifyKey (Map map) {
        HashMap<String, Object> result = new HashMap<>();
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            String key = pair.getKey().toString();

            it.remove(); // avoids a ConcurrentModificationException
        }

        return result;
    }
}
