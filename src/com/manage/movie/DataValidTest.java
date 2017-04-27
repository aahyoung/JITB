/*
 * 입력한 데이터의 유효성 검사
 * JTextfield, JTextArea, File, DatePicker 검사
 * 
 * 1. 영화관
 * 1) 영화관 이름	
 * 2) 영화관 좌석수	숫자만 허용
 * 
 * 2. 영화
 * 1) 영화 포스터	따로 포스터를 입력하지 않은 경우, 존재하는 파일만 허용
 * 2) 종료 날짜		현재 상영작 - 현재 날짜+8일부터 선택 가능
 * 				상영 예정작 - 시작 날짜 : 현재 날짜+8일부터 선택 가능
 * 						    종료 날짜 : 현재 날짜+8일부터 선택 가능 
 * 3) 영화 상영 시간	숫자만 허용
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
	
	// 상영 시간이 숫자인지 확인
	public static boolean isNumber(String value) {
		try {
			Double.parseDouble(value);
			return true;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// 선택된 날짜가 유효한지 확인
	public static boolean inDateRange(LocalDate date){
		// 현재 날짜로부터 8일 이후 날짜
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
		
		// 현재 날짜에서 8일 이후의 날짜와 현재 선택된 날짜 비교 
		// java.sql.Date.valueOf(localDate)
		int result=java.sql.Date.valueOf(date).compareTo(standard);
		System.out.println("result : "+result);
		// 선택된 날짜가 현재 날짜에서 8일 이후의 날짜보다 크거나 같다면
		if(result>=0){
			return true;
		}
		// 선택된 날짜가 현재 날짜에서 8일 이후의 날짜보다 작다면
		else{
			return false;
		}
	}
	
	// 파일 확장자만 추출
	public static String getFileType(String value){
		int identifier=value.lastIndexOf(".");
		return value.substring(identifier, value.length());
		
	}
}
