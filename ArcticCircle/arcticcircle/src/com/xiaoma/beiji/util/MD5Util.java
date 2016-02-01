package com.xiaoma.beiji.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    public static String getMD5(String val) throws NoSuchAlgorithmException{  
        MessageDigest md5 = MessageDigest.getInstance("MD5");  
        try {
        	md5.reset();
			md5.update(val.getBytes("UTF-8"));
			byte[] m = md5.digest();//加密  
		    return getString(m);  
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
       return null;
    }  
    private static String getString(byte[] b){  
        StringBuffer sb = new StringBuffer();  
         for(int i = 0; i < b.length; i ++){  
        	 int val = ((int) b[i]) & 0xff;
        	 if (val < 16) {
        		 sb.append("0");
        	 }
        	 sb.append(Integer.toHexString(val));  
         }  
         return sb.toString();  
    }  
}
