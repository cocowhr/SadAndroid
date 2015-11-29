/*
 * Copyright (C) 2013-2015 北京爱百车信息技术服务有限公司
 * 
 * 		http://www.aibaiche.com
 *
 * Unless required by applicable law or agreed to in writing, you 
 * may not use or modify this file. 
 */
package com.example.hospital.tool;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;


/**
 * 网络数据 加解密
 * 依赖于{@link CipherHelper}, {@link SHA1}
 * @author tennkei
 *
 */
public class MsgCrypt {
	private static String token = "A1i3b5a7iche";
	private static final String const_time = "﻿1421569379";
	private static final String const_nonce = "85dEH9doprT0kKst";
	// 随机生成16位字符串
	private static String getRandomStr() {
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 16; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	// decrypt
	public static String decodeMsg(String timeStamp,String nonce,String signature,String encryptMsg){
		String test_signature = SHA1.getSHA1(token, timeStamp, nonce, encryptMsg);
		if(!test_signature.equals(signature)) return "invalidSignature";
		try {
			return CipherHelper.decrypt_aes(encryptMsg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error:decryptFailed";//e.getMessage();
		}
	}
	// encrypt
	public static String encodeMsg(String msg){
		String timeStamp = String.valueOf( System.currentTimeMillis() / 1000 );
		String nonce = getRandomStr();
		String encryptMsg = "";
		try {
			encryptMsg = CipherHelper.encrypt_aes(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.getMessage();//"error:encryptFailed";
		}
		String signature = SHA1.getSHA1(token, timeStamp, nonce, encryptMsg);
		try {
			//System.out.println(timeStamp + "++++" + nonce);
			return new StringBuffer("from=android&timestamp=").append(timeStamp).append("&nonce=").append(nonce)
					.append("&signature=").append( signature ).append("&msg=")
					.append(URLEncoder.encode(encryptMsg, "utf-8")).toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error:urlencodeFailed";
		}
	}
	/**
	 * 为缓存数据，需要nonce参数等不变，以保证加密后的URL不变
	 * @param msg
	 * @return
	 */
	public static String encodeMsgCache(String msg){
		String timeStamp = const_time;//"1424503440";//const_time;
		String nonce = const_nonce;//"LopHvUealzXxK2db";//const_nonce;
		String encryptMsg = "";
		try {
			encryptMsg = CipherHelper.encrypt_aes(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.getMessage();//"error:encryptFailed";
		}
		String signature = SHA1.getSHA1(token, timeStamp, nonce, encryptMsg);
		try {
			return new StringBuffer("from=android&timestamp=").append(timeStamp).append("&nonce=").append(nonce)
					.append("&signature=").append( signature ).append("&msg=")
					.append(URLEncoder.encode(encryptMsg, "utf-8")).toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error:urlencodeFailed";
		}
	}
}
