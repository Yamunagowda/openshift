package com.example.demo.util;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.tomcat.util.codec.binary.Base64;

public class Crypto {

	private Cipher encryptCipher;
	private Cipher decryptCipher;

	public Crypto() throws SecurityException {

		char[] pass = "CHANGE THIS TO A BUNCH OF RANDOM CHARACTERS".toCharArray();

		byte[] salt = {

				(byte) 0xa3, (byte) 0x21, (byte) 0x24, (byte) 0x2c,

				(byte) 0xf2, (byte) 0xd2, (byte) 0x3e, (byte) 0x19 };

		int iterations = 3;

		init(pass, salt, iterations);

	}

	private void init(char[] pass, byte[] salt, int iterations) throws SecurityException {

		try {

			PBEParameterSpec ps = new javax.crypto.spec.PBEParameterSpec(salt, 20);

			SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");

			SecretKey k = kf.generateSecret(new javax.crypto.spec.PBEKeySpec(pass));

			encryptCipher = Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding");

			encryptCipher.init(Cipher.ENCRYPT_MODE, k, ps);

			decryptCipher = Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding");

			decryptCipher.init(Cipher.DECRYPT_MODE, k, ps);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public synchronized String encrypt(String str) throws SecurityException {

		try {

			byte[] utf8 = str.getBytes(StandardCharsets.UTF_8);

			byte[] enc = encryptCipher.doFinal(utf8);

			byte[] encryptedByteValue = new Base64().encode(enc);

			return new String(encryptedByteValue);

		} catch (Exception e) {
			throw new SecurityException(e.getMessage());
		}

	}

	public synchronized String decrypt(String str) throws SecurityException {

		try {

			byte[] dec = new Base64().decode(str);

			byte[] utf8 = decryptCipher.doFinal(dec);


			return new String(utf8);

		} catch (Exception e) {
			throw new SecurityException(e.getMessage());
		}

	}
	
	public static void main(String[] args) {
		
		
		Crypto c1= new Crypto();
		
		String password=c1.encrypt("e5dmtEBi");
		String encPassword=c1.decrypt(password);
		
		
		
		System.out.println("Password : "+ password);
		System.out.println("Enc Password : "+ encPassword);

	}
}
