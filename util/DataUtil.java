package common.util;

public class DataUtil {
	
	static public String getDateStr(String n){
		//4 --> 04  ,  02
		//n�� 2�� �̸��̸� 0�� ������!!
		String str="";
		if(n.length()<2){
			str="0"+n;
		}else{
			str=n;
		}
		return str;
	}
/*
	public static void main(String[] args) {
		System.out.println(getDateStr("05"));
	}
*/
}
