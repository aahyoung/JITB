/*
현재 상영중인 영화별 평균 매출, 평균 예매를 확인할 수 있는 패널
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
import java.util.Date;
import java.util.GregorianCalendar;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class NowMovie extends JPanel{
	private Connection con;
	String path = "C:/project/JITB/res_manager/";
	ArrayList <BuyMovie> list = new ArrayList<BuyMovie>();
	
	GregorianCalendar date = new GregorianCalendar ( );
	Calendar strDate = Calendar.getInstance();
	Calendar today = Calendar.getInstance();

	public NowMovie() {
		this.setVisible(true);
		this.setBackground(Color.orange);
		setPreferredSize(new Dimension(1000, 650));
	}

	public void setConnection(Connection con) {
		this.con=con;
		loadData();
	}

	public void loadData() {
		
		String sql = "SELECT a.name as 상품명, a.poster, a.END_DATE as 종영일, a.START_DATE 상영시작일,"
				+ " sum(e.TOTAL_PRICE) as total, count(e.order_id) as 구매건수"
				+ " FROM movie a FULL OUTER JOIN product b ON a.MOVIE_ID = b.MOVIE_ID"
				+ " FULL OUTER JOIN buy_seat c ON b.PRODUCT_ID=c.PRODUCT_ID"
				+ " FULL OUTER JOIN movie_price d ON d.TYPE_ID = c.TYPE_ID"
				+ " FULL OUTER JOIN order_movie e on e.ORDER_ID = c.ORDER_ID"
				+ " GROUP BY a.name, a.poster, a.END_DATE, a.START_DATE"
				+ " HAVING a.END_DATE >= TO_CHAR (SYSDATE, 'YYYY-MM-DD')";
		
		System.out.println(sql);

		PreparedStatement pstmt= null;
		ResultSet rs = null;

		try {
			pstmt= con.prepareStatement(sql);
			rs = pstmt.executeQuery(); //쿼리 실행!!

			while(rs.next()){
				BuyMovie dto = new BuyMovie();

				dto.setName(rs.getString("상품명"));
				dto.setPoster(rs.getString("poster"));
				dto.setPrice(rs.getInt("total"));
				dto.setStart_date(rs.getString("상영시작일"));
				dto.setEnd_date(rs.getString("종영일"));
				dto.setCountBuy(rs.getInt("구매건수"));

				list.add(dto);
				System.out.println(list);
			}
			init();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/*---------------------------------------
	 list에 들어있는 movie 객체만큼 MovieItem을 생성해서 화면에 보여준다.
	 보여줄 때 조건으로 START_DATE, 현재 날짜(TODAY)를 불러옴
	 상영 중인 영화의 경우 (START_DATE - TODAY)를 기간으로 설정
	---------------------------------------*/
	public void init() {
		for(int i=0; i<list.size(); i++) {
			BuyMovie buyMovie = list.get(i);
			try {
				Image poster = ImageIO.read(new File(path+buyMovie.getPoster()));
				//String name = buyMovie.getName();
				int price = buyMovie.getPrice();
				
				String str = buyMovie.getStart_date();
				String end = buyMovie.getEnd_date();
				
				int count = buyMovie.getCountBuy();

				int str_year = Integer.parseInt(str.substring(0, 4));	
				int str_month = Integer.parseInt(str.substring(5,7));
				int str_date = Integer.parseInt(str.substring(8,10));

				System.out.println(str_year+","+str_month+","+str_date);
				
				//영화 여러번 돌리기 때문에 startDate가 달라 초기화 시키기
				int period = 0; 

				
				//현재 날짜 구하기
				int year = date.get ( date.YEAR );
				int month = date.get ( date.MONTH ) + 1;
				int yoil = date.get ( date.DAY_OF_MONTH ); 
				
				//today.setTime(new Date());
				//today에 현재 날짜 넣기
				today.set(year, month, yoil);
				
				//strDate 영화 개봉일 넣기
				strDate.set(str_year, str_month, str_date);

				period = (int)((today.getTimeInMillis()-strDate.getTimeInMillis())/(60*60*24*1000))+1;
				
				System.out.println(period);

				String sales = String.format("%.1f", (double)price/period);
				String booking = String.format("%.1f", (double)count/period);
				
				MovieItem item = new MovieItem(poster, sales, booking);
				add(item);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
