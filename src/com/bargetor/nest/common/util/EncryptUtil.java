package com.bargetor.nest.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>description: 加密工具</p>
 * <p>Date: 2013-9-30 上午10:13:22</p>
 * <p>modify：</p>
 * @author: Madgin
 * @version: 1.0
 */
public class EncryptUtil {
	
	// 全局数组
    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
    

    
    /**
     *<p>Title: byteToArrayString</p>
     *<p>Description:返回形式为数字跟字符串</p>
     * @param bByte
     * @return
     * @return String 返回类型
    */
    public static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        // System.out.println("iRet="+iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    /**
     *<p>Title: byteToNum</p>
     *<p>Description:返回形式只为数字</p>
     * @param bByte
     * @return
     * @return String 返回类型
    */
    public static String byteToNum(byte bByte) {
        int iRet = bByte;
        System.out.println("iRet1=" + iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        return String.valueOf(iRet);
    }

   
    /**
     *<p>Title: byteToString</p>
     *<p>Description:转换字节数组为16进制字串</p>
     * @param bByte
     * @return
     * @return String 返回类型
    */
    public static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    /**
     *<p>Title: getMD5Code</p>
     *<p>Description:MD5加密</p>
     * @param strObj
     * @return
     * @return String 返回类型
    */
    public static String getMD5Code(String strObj) {
        String resultString = null;
        try {
            resultString = new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }
    
    /**
     *<p>Title: urlEncode</p>
     *<p>Description:url编码</p>
     * @param url
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     * @return String 返回类型
    */
    public static String urlEncode(String url,String charset) throws UnsupportedEncodingException{
    	String rc = URLEncoder.encode(url, charset);
		return rc.replace("*", "%2A");
    }
	
	public static void main(String[] args) {
        System.out.println(getMD5Code("000000"));
    }
}
