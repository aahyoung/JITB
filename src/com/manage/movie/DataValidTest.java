/*
 * �Է��� �������� ��ȿ�� �˻�
 * JTextfield, JTextArea, File, DatePicker �˻�
 * 
 * 1. ��ȭ��
 * 1) ��ȭ�� �̸�	
 * 2) ��ȭ�� �¼���	���ڸ� ���
 * 
 * 2. ��ȭ
 * 1) ��ȭ ������	���� �����͸� �Է����� ���� ���, �����ϴ� ���ϸ� ���
 * 2) ���� ��¥		���� ���� - ���� ��¥+8�Ϻ��� ���� ����
 * 				�� ������ - ���� ��¥ : ���� ��¥+8�Ϻ��� ���� ����
 * 						    ���� ��¥ : ���� ��¥+8�Ϻ��� ���� ���� 
 * 3) ��ȭ �� �ð�	���ڸ� ���
 * */
package com.manage.movie;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.ss.usermodel.DateUtil;
import org.jfree.date.DateUtilities;

public class DataValidTest {
	static String yymmdd;
	
	// �� �ð��� �������� Ȯ��
	public static boolean isNumber(String value) {
		try {
			Double.parseDouble(value);
			return true;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// ���õ� ��¥�� ��ȿ���� Ȯ��
	public static boolean inDateRange(LocalDate date){
		// ���� ��¥�κ��� 8�� ���� ��¥
		Calendar current=Calendar.getInstance();
		current.set(Calendar.HOUR, -12);
		current.set(Calendar.MINUTE, 0);
		current.set(Calendar.SECOND, 0);
		current.set(Calendar.MILLISECOND, 0);
		current.add(current.DATE, 8);
		
		Date standard=current.getTime();
		
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(standard);
		
		yymmdd=format.format(standard);
		
		// ���� ��¥���� 8�� ������ ��¥�� ���� ���õ� ��¥ �� 
		// java.sql.Date.valueOf(localDate)
		int result=java.sql.Date.valueOf(date).compareTo(standard);
		System.out.println("result : "+result);
		// ���õ� ��¥�� ���� ��¥���� 8�� ������ ��¥���� ũ�ų� ���ٸ�
		if(result>=0){
			return true;
		}
		// ���õ� ��¥�� ���� ��¥���� 8�� ������ ��¥���� �۴ٸ�
		else{
			return false;
		}
	}
	
	// ���� Ȯ���ڸ� ����
	public static String getFileType(String value){
		int identifier=value.lastIndexOf(".");
		return value.substring(identifier, value.length());
		
	}
}
