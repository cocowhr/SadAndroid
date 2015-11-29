/*
 * Copyright (C) 2013-2015 北京爱百车信息技术服务有限公司
 * 
 * 		http://www.aibaiche.com
 *
 * Unless required by applicable law or agreed to in writing, you 
 * may not use or modify this file. 
 */
package com.example.hospital.tool;

import java.nio.charset.Charset;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;
/**
 * AES DES 加密解密算法
 * @author tennkei
 *
 */
public class CipherHelper {
	static Charset CHARSET = Charset.forName("utf-8");  //字符编码选为UTF-8编码
    private static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
    private static final String ALGORITHM_AES = "AES/CBC/NoPadding";
    private static byte[] aes_key = "o9iA1DOz5jwod2x4".getBytes();
	private static byte[] aes_iv =  "Jn8mixDoWvGMv1yf".getBytes();
    
    /**
     * DES加密算法
     *
     * @param data 待加密字符串
     * @param key  加密私钥，长度不能够小于8位
     * @return  加密后的字节数组，一般结合Base64编码使用
     * @throws CryptException异常
     */
    public static String encode(String key, String data) 
    {
        try
        {
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey,paramSpec);
            
            byte[] bytes = cipher.doFinal(data.getBytes());
            //return bytes.toString();
            return Base64.encodeToString(bytes, Base64.DEFAULT).replace("\n", "");
            
        }
        catch (Exception e)
        {
        	e.printStackTrace();
            return "加密失败";
        }
    }

    /**
     * DES解密算法
     *
     * @param data 待解密字符串
     * @param key  解密私钥，长度不能够小于8位
     * @return 解密后的字节数组
     * @throws Exception异常
     */
    public static String decode(String key, String data) throws Exception
    {
        try
        {
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //key的长度不能够小于8位
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey,paramSpec);
            return  new String( cipher.doFinal(Base64.decode(data, Base64.DEFAULT)) );
        }
        catch (Exception e)
        {
        	//e.printStackTrace();
        	throw new Exception(e);
            //return "解密失败";
        }
    }

   
    
    // AES
    /**
     * AES加密算法
     * 
     * @param content 需要加密的内容
     * @param password  加密密码
     * @return
     */
    public static String encrypt_aes(String content) throws Exception {
		try {
			/*KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");*/
			Cipher cipher = Cipher.getInstance(ALGORITHM_AES);// 创建密码器
			SecretKeySpec key_spec = new SecretKeySpec(aes_key, "AES");
			IvParameterSpec iv = new IvParameterSpec(aes_iv);
			
			content = strlen2_16(content);
			//System.out.println(content.length());
			byte[] byteContent = content.getBytes(CHARSET);
			//System.out.println(byteContent.length);
			cipher.init(Cipher.ENCRYPT_MODE, key_spec,iv);// 初始化
			byte[] result = cipher.doFinal(byteContent);
			return Base64.encodeToString(result, Base64.DEFAULT); // 加密
		} catch (Exception e) {
			throw new Exception(e);
		}
    }
    /**
     * AES解密算法
     * 
     * @param content  待解密的内容
     * @param password 解密秘钥
     * @return
     */
    public static String decrypt_aes(String content) throws Exception {
		try {
			/*KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(256, new SecureRandom(aes_key));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key_spec = new SecretKeySpec(enCodeFormat, "AES");*/
			
			Cipher cipher = Cipher.getInstance(ALGORITHM_AES);// 创建密码器
			SecretKeySpec key_spec = new SecretKeySpec(aes_key, "AES");
			IvParameterSpec iv = new IvParameterSpec(aes_iv);
			
			cipher.init(Cipher.DECRYPT_MODE, key_spec, iv);// 初始化
			
			// byte[] result = cipher.doFinal(content);
			byte[] result = cipher.doFinal(Base64.decode(content,Base64.DEFAULT));
			
			return new String(result,CHARSET).trim();
		} catch (Exception e) {
			throw new Exception(e);
		}
    }
    /**
     * 判断字符串长度是否符合规范,对字符串补全16位,对于加密串，要求16的整数倍
     * @param s 待检查的字符串
     * @return 返回处理后的字符串
     */ 
    private static String strlen2_16(String s){
    	int l = s.getBytes(CHARSET).length;
    	while(l%16!=0){ s+=' '; l++;}
    	return s;
    }
}
