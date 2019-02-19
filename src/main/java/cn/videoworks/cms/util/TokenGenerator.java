package cn.videoworks.cms.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;
import java.util.UUID;

public class TokenGenerator {
	static final char[] chars={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j','k','l',
		'm','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I',
		'J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	/**
	 * 生成会话token
	 * @return
	 */
	public static String generateToken(boolean login) {
		String token = UUID.randomUUID().toString();
		return genToken(token,1,login);
	}
	
	
	private static final String genToken(String s, int ct, boolean login){
		char[] chs = s.toCharArray();
		StringBuffer sb=new StringBuffer(36);
		for(int i=0;i<chs.length;i++){
			char ch=chs[i];
			if(ch!='-'){
				sb.append(ch);
			}
		}
		if(ct==0){//web 
			sb.append(getChar(0,9));
			sb.append(getChar(10,61));
			sb.append(getChar(10,61));
		}
		if(ct==1){//app
			sb.append(getChar(10,61));
			sb.append(getChar(0,9));
			sb.append(getChar(10,61));
		}
		if(ct==2){//share			
			sb.append(getChar(10,61));
			sb.append(getChar(10,61));
			sb.append(getChar(0,9));
		}
		if(login){
			sb.append(getChar(0,30));
		}else{
			sb.append(getChar(31,61));
		}
		return sb.toString();
	}
	public static boolean isLogin(String token){
		if(StringUtils.isEmpty(token)){
			return false;
		}
		int  at=token.length()-1;
		char v=token.charAt(at);
		int i=0;
		for(;i<chars.length;i++){
			if(chars[i]==v){
				break;
			}
		}
		return i<=30;
	}
	

	private static char getChar(int f,int t){
		if(f<0 || f>61)
			f=0;
		if(t<0 || t>61)
			t=0;
		if(f>t)
			return 'x';
		int sd= t-f;
		Random rd=new Random();
		int v=f+rd.nextInt(sd);
		return chars[v];
	}
	
	public static void main(String[] args) {
		String s = generateToken(true);
		System.out.println(s);
	}
}
