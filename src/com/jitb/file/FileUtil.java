package com.jitb.file;

public class FileUtil {
	// Ȯ���ڸ� �����ϴ� �޼ҵ�
	public static String getOnlyFileName(String path){
		// ex) mario.png
		// ���� �̸� �߰��� .�� �� ��쵵 ����
		// ������ '.'�� ��ǥ
		int last=path.lastIndexOf(".");
		
		return path.substring(last, path.length());
	}
}
