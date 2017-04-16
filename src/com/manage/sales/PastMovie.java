/*
과거에 상영했던 영화별 평균 매출, 평균 예매를 확인할 수 있는 패널
 */

package com.manage.sales;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class PastMovie extends JPanel {

	private Connection con;
	String path = "C:/project/JITB/res_manager/";
	ArrayList<BuyMovie> list = new ArrayList<BuyMovie>();
	ArrayList<String> max= new ArrayList<String>();
	
	/*---------------------------------------
	Calendar를 사용해 strDate랑 endDate 만든다.
	---------------------------------------*/
	Calendar strDate = Calendar.getInstance();
	Calendar endDate = Calendar.getInstance();

	public PastMovie() {
		this.setVisible(true);
		this.setBackground(Color.yellow);
		setPreferredSize(new Dimension(1000, 650));
	}

	public void setConnection(Connection con) {
		this.con = con;
		loadData();
	}

	/*---------------------------------------
	 Join을 통해 원하는 정보(Poster, StartDate, EndDATE를 불러온 후 
	 현재 날짜를 불러오는 오라클 함수를 사용해
	 현재 날짜와 영화의 EndDate 비교해서 상영이 끝난 영화 데이터만을 출력
	---------------------------------------*/
	public void loadData() {
		
		String sql = "select a.poster, a.name as 상품명, sum(g.PRICE) as total"
				+ " ,MAX(b.SCREENING_DATE) as 상영일 ,MIN(b.SCREENING_DATE) as 종영일"
				+ " from movie a, SCREENING_DATE b, START_TIME c, THEATER_OPERATE d"
				+ " , SEAT e, BUY_MOVIE f, movie_price g"
				+ " where a.MOVIE_ID=b.MOVIE_ID"
				+ " and b.SCREENING_DATE_ID=c.SCREENING_DATE_ID"
				+ " and c.START_TIME_ID=d.START_TIME_ID"
				+ " and d.THEATER_OPERATE_ID=e.THEATER_OPERATE_ID"
				+ " and e.SEAT_ID=f.SEAT_ID and f.TYPE_ID=g.TYPE_ID"
				+ " group by a.poster, a.name"
				+ " having max(screening_date) < TO_CHAR(SYSDATE, 'YYYY-MM-DD')";
				
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery(); // 쿼리 실행!!

			while (rs.next()) {
				BuyMovie dto = new BuyMovie();

				dto.setPoster(rs.getString("poster"));
				dto.setName(rs.getString("상품명"));
				dto.setPrice(rs.getInt("total"));
				dto.setStart_date(rs.getString("상영일"));
				dto.setEnd_date(rs.getString("종영일"));

				list.add(dto);
			}
			//init();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}


	/*---------------------------------------
	 END_DATE, START_DATE를 불러옴
	 상영이 끝난 영화의 경우 (END_DATE - START_DATE)를 기간으로 설정
	---------------------------------------*/
	public void init() {
		for(int i=0; i<list.size(); i++) {
			BuyMovie buyMovie = list.get(i);
			try {
				Image poster = ImageIO.read(new File(path+buyMovie.getPoster()));
				String name = buyMovie.getName();
				int price = buyMovie.getPrice();
				
				//영화 시작하는 날짜 구하기
				String str = buyMovie.getStart_date();
				String end = buyMovie.getEnd_date();
				
				int str_year = Integer.parseInt(str.substring(0, 4));
				int str_month = Integer.parseInt(str.substring(5,7))-1;
				int str_date = Integer.parseInt(str.substring(8,10))-1;
				
				int end_year = Integer.parseInt(end.substring(0, 4));
				int end_month = Integer.parseInt(end.substring(5,7))-1;
				int end_date = Integer.parseInt(end.substring(8,10))-1;
				
				//영화 여러번 돌리기 때문에 startDate가 달라 초기화 시키기
				int period = 0;

				//startDate에 영화 개봉일, endDate에 영화 끝난 날 넣어줌
				strDate.set(str_year, str_month, str_date);
				//endDate.set(end_year, end_month, end_date);
				
				//end-date에서 영화 개봉일을 뺀 상영기간을 구해줌
				//오늘날짜에서 끝난 날짜를 빼줘야한다.
				period = (int)((endDate.getTimeInMillis()-strDate.getTimeInMillis())/(60*60*24*1000))+1;
				System.out.println(period);
				
				String sales = String.format("%.1f", (double)price/period);
				String booking = String.format("%.1f", (double)price/period*100);
				
				MovieItem item = new MovieItem(poster, sales, booking);
				add(item);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
