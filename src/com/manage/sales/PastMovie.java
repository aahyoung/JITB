/*
���ſ� ���ߴ� ��ȭ�� ��� ����, ��� ���Ÿ� Ȯ���� �� �ִ� �г�
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
	Calendar�� ����� strDate�� endDate �����.
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
	 Join�� ���� ���ϴ� ����(Poster, StartDate, EndDATE�� �ҷ��� �� 
	 ���� ��¥�� �ҷ����� ����Ŭ �Լ��� �����
	 ���� ��¥�� ��ȭ�� EndDate ���ؼ� ���� ���� ��ȭ �����͸��� ���
	---------------------------------------*/
	public void loadData() {
		
		String sql = "select a.poster, a.name as ��ǰ��, sum(g.PRICE) as total"
				+ " ,MAX(b.SCREENING_DATE) as ���� ,MIN(b.SCREENING_DATE) as ������"
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
			rs = pstmt.executeQuery(); // ���� ����!!

			while (rs.next()) {
				BuyMovie dto = new BuyMovie();

				dto.setPoster(rs.getString("poster"));
				dto.setName(rs.getString("��ǰ��"));
				dto.setPrice(rs.getInt("total"));
				dto.setStart_date(rs.getString("����"));
				dto.setEnd_date(rs.getString("������"));

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
	 END_DATE, START_DATE�� �ҷ���
	 ���� ���� ��ȭ�� ��� (END_DATE - START_DATE)�� �Ⱓ���� ����
	---------------------------------------*/
	public void init() {
		for(int i=0; i<list.size(); i++) {
			BuyMovie buyMovie = list.get(i);
			try {
				Image poster = ImageIO.read(new File(path+buyMovie.getPoster()));
				String name = buyMovie.getName();
				int price = buyMovie.getPrice();
				
				//��ȭ �����ϴ� ��¥ ���ϱ�
				String str = buyMovie.getStart_date();
				String end = buyMovie.getEnd_date();
				
				int str_year = Integer.parseInt(str.substring(0, 4));
				int str_month = Integer.parseInt(str.substring(5,7))-1;
				int str_date = Integer.parseInt(str.substring(8,10))-1;
				
				int end_year = Integer.parseInt(end.substring(0, 4));
				int end_month = Integer.parseInt(end.substring(5,7))-1;
				int end_date = Integer.parseInt(end.substring(8,10))-1;
				
				//��ȭ ������ ������ ������ startDate�� �޶� �ʱ�ȭ ��Ű��
				int period = 0;

				//startDate�� ��ȭ ������, endDate�� ��ȭ ���� �� �־���
				strDate.set(str_year, str_month, str_date);
				//endDate.set(end_year, end_month, end_date);
				
				//end-date���� ��ȭ �������� �� �󿵱Ⱓ�� ������
				//���ó�¥���� ���� ��¥�� ������Ѵ�.
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
