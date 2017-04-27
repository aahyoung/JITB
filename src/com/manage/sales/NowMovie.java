/*
���� ������ ��ȭ�� ��� ����, ��� ���Ÿ� Ȯ���� �� �ִ� �г�
 */
package com.manage.sales;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.manage.main.Main;

public class NowMovie extends JPanel implements ActionListener{
	
	private Connection con;
	//String path = "C:/project/JITB/res_manager/";
	ArrayList <BuyMovie> list = new ArrayList<BuyMovie>();
	
	URL url_image;

	GregorianCalendar date = new GregorianCalendar ( );
	Calendar strDate = Calendar.getInstance();
	Calendar today = Calendar.getInstance();

	public NowMovie() {
		this.setVisible(true);
		setPreferredSize(new Dimension(1000, 650));
	}

	public void setConnection(Connection con) {
		this.con=con;
		loadData();
	}

	public void loadData() {
		
		String sql = "SELECT a.name as ��ǰ��, a.poster, a.END_DATE as ������, a.START_DATE �󿵽�����,"
				+ " sum(e.TOTAL_PRICE) as total, count(e.order_id) as ���ŰǼ�"
				+ " FROM movie a FULL OUTER JOIN product b ON a.MOVIE_ID = b.MOVIE_ID"
				+ " FULL OUTER JOIN buy_seat c ON b.PRODUCT_ID=c.PRODUCT_ID"
				+ " FULL OUTER JOIN movie_price d ON d.TYPE_ID = c.TYPE_ID"
				+ " FULL OUTER JOIN order_movie e on e.ORDER_ID = c.ORDER_ID"
				+ " GROUP BY a.name, a.poster, a.END_DATE, a.START_DATE"
				+ " HAVING a.END_DATE >= TO_CHAR (SYSDATE, 'YYYY-MM-DD')";
		
		//System.out.println(sql);

		PreparedStatement pstmt= null;
		ResultSet rs = null;

		try {
			pstmt= con.prepareStatement(sql);
			rs = pstmt.executeQuery(); //���� ����!!

			while(rs.next()){
				
				BuyMovie dto = new BuyMovie();

				dto.setName(rs.getString("��ǰ��"));
				dto.setPoster(rs.getString("poster"));
				dto.setPrice(rs.getInt("total"));
				dto.setStart_date(rs.getString("�󿵽�����"));
				dto.setEnd_date(rs.getString("������"));
				dto.setCountBuy(rs.getInt("���ŰǼ�"));

				list.add(dto);
				//System.out.println(list);
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
	 list�� ����ִ� movie ��ü��ŭ MovieItem�� �����ؼ� ȭ�鿡 �����ش�.
	 ������ �� �������� START_DATE, ���� ��¥(TODAY)�� �ҷ���
	 �� ���� ��ȭ�� ��� (TODAY - START_DATE)�� �Ⱓ���� ����
	---------------------------------------*/
	public void init() {
		for(int i=0; i<list.size(); i++) {
			Image poster;
			BuyMovie buyMovie = list.get(i);
			try {
				//http://172.20.10.4:9090/abouttime.jpg
				//Image poster = ImageIO.read(new File(path+buyMovie.getPoster()));

				//Image ��ο��� �޾ƿ���
				url_image = new URL("http://211.238.142.100:8989/"+buyMovie.getPoster());
				//System.out.println("img�� ������"+buyMovie.getPoster());
				poster=ImageIO.read(url_image);
				
				String name = buyMovie.getName();
				int price = buyMovie.getPrice();
				
				String str = buyMovie.getStart_date();
				String end = buyMovie.getEnd_date();
				
				int count = buyMovie.getCountBuy();

				int str_year = Integer.parseInt(str.substring(0, 4));	
				int str_month = Integer.parseInt(str.substring(5,7));
				int str_date = Integer.parseInt(str.substring(8,10));

				//System.out.println(str_year+","+str_month+","+str_date);
				
				//��ȭ ������ ������ ������ startDate�� �޶� �ʱ�ȭ ��Ű��
				int period = 0; 

				//���� ��¥ ���ϱ�
				int year = date.get ( date.YEAR );
				int month = date.get ( date.MONTH ) + 1;
				int day = date.get ( date.DAY_OF_MONTH ); 
				
				//today.setTime(new Date());
				//today�� ���� ��¥ �ֱ�
				today.set(year, month, day);
				
				//strDate ��ȭ ������ �ֱ�
				strDate.set(str_year, str_month, str_date);

				period = (int)((today.getTimeInMillis()-strDate.getTimeInMillis())/(60*60*24*1000))+1;
				
				String sales = String.format("%.1f", (double)price/period);
				String booking = String.format("%.1f", (double)count/period);
				
				MovieItem item = new MovieItem(poster, name, sales, booking, con);
				add(item);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		Object ojb = e.getSource();

	}
}
