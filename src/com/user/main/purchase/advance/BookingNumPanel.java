package com.user.main.purchase.advance;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.user.main.ClientMain;

public class BookingNumPanel extends JPanel{
	JPanel p_pilot;
	JPanel p_bn;
	JTextField t_init;
	JTextField t_num;
	JPanel p_label;
	JPanel bt_del_num;
	JLabel la_detail1, la_detail2;
	JPanel p_grid;
	Canvas can_line, can_separ;
	
	Image line, separ, del;
	
	ClientMain main;
	Image btnImg[];
	NumButton btnPanel[] = new NumButton[12];
	
	StringBuffer num = new StringBuffer();
	
	ArrayList<Ticket> ticket = new ArrayList<Ticket>();
	String[] days = {"일", "월", "화", "수", "목", "금", "토"};
	
	public BookingNumPanel(ClientMain main, Image btnImg[]) {
		this.main = main;
		this.btnImg = btnImg;
		
		String str1 = "앞 4자리를 제외하고 뒤 11자리를 입력해주세요";
		String str2 = "예매번호를 모르신다면 생년월일+휴대폰번호로 조회해주세요";
		
		setLine();
		setDel();
		
		can_line = new Canvas(){
			@Override
			public void paint(Graphics g) {
				g.drawImage(line, 0, 0, 600, 1, this);
			}
		};
		can_separ = new Canvas(){
			@Override
			public void paint(Graphics g) {
				g.drawImage(separ, 0, 10, 1, 60, this);
			}
		};
		bt_del_num = new JPanel(){
			@Override
			public void paint(Graphics g) {
				g.drawImage(del, 0, 0, 50, 50, this);
			}
		};
		p_pilot = new JPanel();
		p_bn = new JPanel();
		t_init = new JTextField("0057"){
			@Override
			public void setBorder(Border border) {}
		};
		t_num = new JTextField("뒷 자리(11자)"){
			@Override
			public void setBorder(Border border) {}
		};
		p_label = new JPanel();
		la_detail1 = new JLabel(str1);
		la_detail2 = new JLabel(str2);
		p_grid = new JPanel(new GridLayout(4, 3));
		
		for(int i=0; i<btnPanel.length; i++){
			btnPanel[i] = new NumButton(btnImg[i], i);
			btnPanel[i].setPreferredSize(new Dimension(50, 50));
			btnPanel[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					NumButton btn = (NumButton)e.getSource();
					
					if(btn.index == 10){
						deleteNum();
					}else if(btn.index == 11){
						selectBookingNum();
						if(ticket.size() == 0){
							main.setPage(3);
						}else{
							ticketProcessiong();
							main.setPage(10);
						}
					}else{
						insertNum(btn.index);
					}
				}
			});
		}
		
		for(int i=0; i<9; i++){
			p_grid.add(btnPanel[i+1]);
		}
		p_grid.add(btnPanel[10]);
		p_grid.add(btnPanel[0]);
		p_grid.add(btnPanel[11]);
		
		t_init.setEditable(false);
		
		t_init.setFont(new Font("Malgun Gothic", Font.BOLD, 50));
		t_init.setForeground(Color.WHITE);
		t_init.setBackground(new Color(33,33,33));
		t_num.setFont(new Font("Malgun Gothic", Font.BOLD, 50));
		t_num.setForeground(Color.WHITE);
		t_num.setBackground(new Color(33,33,33));
		la_detail1.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
		la_detail1.setForeground(Color.WHITE);
		la_detail2.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
		la_detail2.setForeground(Color.WHITE);
		
		p_bn.setBackground(new Color(33,33,33));
		p_pilot.setBackground(new Color(33,33,33));
		p_label.setBackground(new Color(33,33,33));
		p_grid.setBackground(new Color(33,33,33));
		
		can_line.setPreferredSize(new Dimension(600, 10));
		can_separ.setPreferredSize(new Dimension(10, 70));
		bt_del_num.setPreferredSize(new Dimension(50, 50));
		t_init.setPreferredSize(new Dimension(120, 100));
		t_num.setPreferredSize(new Dimension(380, 100));
		p_bn.setPreferredSize(new Dimension(600, 100));
		p_label.setPreferredSize(new Dimension(600, 130));
		p_grid.setPreferredSize(new Dimension(300, 500));
		p_pilot.setPreferredSize(new Dimension(600, 900));
		
		p_bn.add(t_init);
		p_bn.add(can_separ);
		p_bn.add(t_num);
		p_bn.add(bt_del_num);
		p_label.add(la_detail1);
		p_label.add(la_detail2);
		
		p_pilot.add(p_bn);
		p_pilot.add(can_line);
		p_pilot.add(p_label);
		p_pilot.add(p_grid);
		add(p_pilot);
		
		bt_del_num.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				num.delete(0, num.length());
				t_num.setText("뒷 자리(11자)");
			}
		});
		
		setBackground(new Color(33,33,33));
		setVisible(true);
	}
	
	public void insertNum(int index){
		num.append(index);
		t_num.setText(num.toString());
	}
	
	public void deleteNum(){
		if(num.length() != 0){
			num.delete(num.length()-1, num.length());
		}
		if(num.length() == 0){
			t_num.setText("뒷 자리(11자)");
		}else{
			t_num.setText(num.toString());
		}
	}
	
	public void setLine(){
		URL url1 = getClass().getResource("/line.png");
		URL url2 = getClass().getResource("/separ.png");
		try {
			line = ImageIO.read(url1);
			separ = ImageIO.read(url2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setDel(){
		URL url = getClass().getResource("/remove.png");
		try {
			del = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void selectBookingNum(){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select m.name as 영화이름, b.name as 지점, t.name as 관,");
		sql.append(" sd.screening_date as 날짜, time.start_time as 시작시간,");
		sql.append(" mp.type as 타입, m.poster as 포스터,count(*) as 인원");
		sql.append(" from booking_number bn");
		sql.append(" inner join order_movie o on bn.order_id = o.order_id");
		sql.append(" inner join buy_movie buy on o.order_id = buy.order_id");
		sql.append(" inner join movie_price mp on buy.type_id = mp.type_id");
		sql.append(" inner join seat s on buy.seat_id = s.seat_id");
		sql.append(" inner join theater_operate toper on toper.theater_operate_id = s.theater_operate_id");
		sql.append(" inner join theater t on t.theater_id = toper.theater_id");
		sql.append(" inner join branch b on b.branch_id = t.branch_id");
		sql.append(" inner join start_time time on toper.start_time_id = time.start_time_id");
		sql.append(" inner join screening_date sd on time.screening_date_id = sd.screening_date_id");
		sql.append(" inner join movie m on sd.movie_id = m.movie_id");
		sql.append(" where bn.booking_number = "+num.toString());
		sql.append(" group by m.name, b.name, t.name, sd.screening_date, time.start_time, mp.type, m.poster");
		
		try {
			pstmt = main.con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				Ticket dto = new Ticket();
				dto.setBranch(rs.getString("지점"));
				dto.setTheater(rs.getString("관"));
				dto.setMovie_name(rs.getString("영화이름"));
				dto.setPersons(rs.getString("인원"));
				dto.setType(rs.getString("타입"));
				dto.setDate(rs.getString("날짜"));
				dto.setTime(rs.getString("시작시간"));
				dto.setPoster(rs.getString("포스터"));
				
				ticket.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			try {
				if(rs != null){
					rs.close();
				}
				if(pstmt != null){
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void ticketProcessiong(){
		AdvancedTicket at = ((TicketConfirmScreen)main.screen.get(10)).rect;
		
		Calendar cal = Calendar.getInstance();
		
		StringBuffer persons = new StringBuffer();
		StringBuffer dateTime = new StringBuffer();
		
		String s_yy = ticket.get(0).getDate().substring(0,4);
		String s_mm = ticket.get(0).getDate().substring(5,7);
		String s_dd = ticket.get(0).getDate().substring(8,10);
		
		int yy = Integer.parseInt(s_yy);
		int mm = Integer.parseInt(s_mm);
		int dd = Integer.parseInt(s_dd);
		
		String time = ticket.get(0).getTime();
		String[] timeSplit = ticket.get(0).getTime().split(":");
		String am_pm = null;
		
		for(int i=0; i<ticket.size(); i++){
			if(i>0){
				persons.append(" / ");
			}
			persons.append(ticket.get(i).getType());
			persons.append(" ");
			persons.append(ticket.get(i).getPersons());
			persons.append("명");
		}
		
		if(Integer.parseInt(timeSplit[0]) < 12){
			am_pm = "오전";
		}else{
			am_pm = "오후";
		}
		
		cal.set(yy, mm-1, dd);
		int day = cal.get(Calendar.DAY_OF_WEEK);
		dateTime.append(s_yy);
		dateTime.append(".");
		dateTime.append(s_mm);
		dateTime.append(".");
		dateTime.append(s_dd);
		dateTime.append("(");
		dateTime.append(days[day-1]);
		dateTime.append(") ");
		dateTime.append(am_pm);
		dateTime.append(time);
		
		System.out.println(dateTime.toString());
		
		at.setBranch(ticket.get(0).getBranch());
		at.setTheater(ticket.get(0).getTheater()+"관");
		at.setMovie_name(ticket.get(0).getMovie_name());
		at.setPersons(persons.toString());
		at.setMovie_time(dateTime.toString());
		at.setPoster(ticket.get(0).getPoster());
	}
}
