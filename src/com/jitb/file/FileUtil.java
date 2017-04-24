package com.jitb.file;

public class FileUtil {
	// 확장자만 추출하는 메소드
	public static String getOnlyFileName(String path){
		// ex) mario.png
		// 파일 이름 중간에 .가 들어간 경우도 포함
		// 마지막 '.'의 좌표
		int last=path.lastIndexOf(".");
		
		return path.substring(last, path.length());
	}
}
