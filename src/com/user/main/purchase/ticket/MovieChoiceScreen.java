package com.user.main.purchase.ticket;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jitb.db.MovieRate;
import com.user.frame.ScreenFrame;
import com.user.main.ClientMain;

public class MovieChoiceScreen extends ScreenFrame{
	JPanel p_north;
	JLabel la_branch;
	JLabel la_date;
	Canvas bt_next;
	public JLabel la_time;
	
	Calendar cal = Calendar.getInstance();
	String[] days = {"��", "��", "ȭ", "��", "��", "��", "��"};
	int yy;
	int mm;
	int dd;
	int day;
	
	ArrayList<MovieRate> movieRate = new ArrayList<MovieRate>();
	ArrayList<MovieTime> movieTime = new ArrayList<MovieTime>();
	
	public MovieChoiceScreen(ClientMain main) {
		super(main);
		
		p_north = new JPanel();
		la_branch = new JLabel("������");
		la_date = new JLabel("2017�� 4��");
		la_time = new JLabel("10:00");
		bt_next = new Canvas(){
			@Override
			public void paint(Graphics g) {
				URL url = getClass().getResource("/bt_next.png");
				try {
					Image img = ImageIO.read(url);
					g.drawImage(img, 0, 26, 10, 35, this);
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
		};
		
		setDate();
		
		p_north.setPreferredSize(new Dimension(800, 80));
		la_branch.setPreferredSize(new Dimension(250, 80));
		la_date.setPreferredSize(new Dimension(250, 80));
		la_time.setPreferredSize(new Dimension(250, 80));
		bt_next.setPreferredSize(new Dimension(10, 80));
		
		la_branch.setForeground(Color.WHITE);
		la_date.setForeground(Color.WHITE);
		la_time.setForeground(Color.WHITE);
		
		la_branch.setFont(new Font("Malgun Gothic", Font.PLAIN, 30));
		la_date.setFont(new Font("Malgun Gothic", Font.PLAIN, 40));
		la_time.setFont(new Font("Malgun Gothic", Font.PLAIN, 40));
		
		la_branch.setHorizontalAlignment(JLabel.CENTER);
		la_date.setHorizontalAlignment(JLabel.CENTER);
		la_time.setHorizontalAlignment(JLabel.CENTER);
		
		p_north.setBackground(new Color(33,33,33));
		
		p_north.add(la_branch);
		p_north.add(la_date);
		p_north.add(bt_next);
		p_north.add(la_time);
		
		add(p_north);
		
		selectMovieRateOrder();
		
		for(int i=0; i<movieRate.size(); i++){
			selectMovieTime(movieRate.get(i).getMovie_id());
			String poster = movieTime.get(i).getPoster();
			String name = movieTime.get(i).getMovie_name();
			
			Canvas can_movie = new Canvas(){
				@Override
				public void paint(Graphics g) {
					Graphics2D g2 = (Graphics2D)g;
					
					URL url = getClass().getResource("/"+poster);
					try {
						Image img = ImageIO.read(url);
						g2.drawImage(img, 25, 25, 200, 250, this);
					} catch (IOException e) {
						e.printStackTrace();
					}
					g2.setColor(Color.WHITE);
					g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 30));
					g2.drawString(name, 250, 50);
					
					for(int j=0; j<movieTime.size(); j++){
						Rectangle rect = new Rectangle(250+(j*160), 80, 150, 100);
						g2.draw(rect);
						g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 50));
						g2.drawString(movieTime.get(j).getStart_time(), 250+(j*160)+10, 130);
						g2.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
						g2.drawString(movieTime.get(j).getRemaining_seat()+"/"+movieTime.get(j).getTotal_seat(), 
								250+(j*160)+60, 160);
					}
				}
			};
			
			can_movie.setPreferredSize(new Dimension(800, 300));
			
			add(can_movie);
		}
	}
	
	public void setDate(){
		yy = cal.get(Calendar.YEAR);
		mm = cal.get(Calendar.MONTH);
		dd = cal.get(Calendar.DATE);
		day = cal.get(Calendar.DAY_OF_WEEK);
		StringBuffer date = new StringBuffer();
		
		date.append(mm+1);
		date.append("��");
		date.append(dd);
		date.append("��");
		date.append("(");
		date.append(days[day-1]);
		date.append(")");
		
		la_date.setText(date.toString());
	}
	
	public void selectMovieRateOrder(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		//������ ������� ��ȭ ���̵� �����´�.
		String sql = "select * from movie_rate order by movie_rate desc";
		
		try {
			pstmt = main.con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				MovieRate dto = new MovieRate();
				dto.setMovie_id(rs.getInt("movie_rate_id"));
				dto.setMovie_id(rs.getInt("movie_id"));
				dto.setMovie_rate(rs.getDouble("movie_rate"));
				
				movieRate.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(rs!=null){
					rs.close();
				}
				if(pstmt!=null){
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void selectMovieTime(int movie_id){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select m.poster as ������, m.name as ��ȭ�̸�, sd.screening_date as �󿵳�¥, st.start_time as �󿵽ð�,");
		sql.append(" count(s.status) as �¼���, t.row_line*t.column_line as ���¼���");
		sql.append(" from movie m");
		sql.append(" inner join screening_date sd on m.movie_id = sd.movie_id");
		sql.append(" inner join start_time st on sd.screening_date_id = st.screening_date_id");
		sql.append(" inner join theater_operate toper on st.start_time_id = toper.start_time_id");
		sql.append(" inner join theater t on toper.theater_id = t.theater_id");
		sql.append(" inner join seat s on toper.theater_operate_id = s.theater_operate_id");
		sql.append(" where m.movie_id = ");
		sql.append(movie_id);
		sql.append(" and sd.screening_date = '");
		sql.append(yy);
		sql.append("-");
		sql.append(mm+1);
		sql.append("-");
		sql.append(dd-1);
		sql.append("' and s.status=1");
		sql.append(" group by m.poster, m.name, sd.screening_date, st.start_time, t.row_line, t.column_line");
		
		System.out.println(sql.toString());
		
		movieTime.removeAll(movieTime);
		
		try {
			pstmt = main.con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				MovieTime dto = new MovieTime();
				dto.setMovie_name(rs.getString("��ȭ�̸�"));
				dto.setScreening_date(rs.getString("�󿵳�¥"));
				String time = rs.getString("�󿵽ð�");
				String[] splitTime = time.split(":");
				if(Integer.parseInt(splitTime[0]) < 10){
					time = 0+time;
				}
				dto.setStart_time(time);
				dto.setPoster(rs.getString("������"));
				dto.setRemaining_seat(rs.getString("�¼���"));
				dto.setTotal_seat(rs.getString("���¼���"));
				
				movieTime.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(rs!=null){
					rs.close();
				}
				if(pstmt!=null){
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}

