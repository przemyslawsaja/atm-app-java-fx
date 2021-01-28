package atm;

import java.security.MessageDigest;

public class PinEncryptionSHA1 {
	
	public static String SHA1(int PIN)
	{
		try {
		MessageDigest mDigest =MessageDigest.getInstance("SHA1");
		String input = Integer.toString(PIN);
		byte[] result =mDigest.digest(input.getBytes());
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<result.length;i++)
		{
			sb.append(Integer.toString((result[i] & 0xff)+ 0x100,16).substring(1));
		}
		return sb.toString();
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	public static void main(String[] args) {
		int password =4321;
		String encrypt= PinEncryptionSHA1.SHA1(password);
		System.out.println(encrypt);
		
	}

}
