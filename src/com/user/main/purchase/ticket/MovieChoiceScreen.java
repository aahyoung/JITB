package com.user.main.purchase.ticket;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.user.frame.ScreenFrame;
import com.user.main.ClientMain;
import com.user.main.InitScreen;
import com.user.main.purchase.MenuChoiceScreen;

public class MovieChoiceScreen extends ScreenFrame{
	JPanel p_north;
	JLabel la_branch;
	JLabel la_date;
	Canvas bt_prev;
	Canvas bt_next;
	public JLabel la_time;
	Canvas container;
	
	Calendar cal = Calendar.getInstance();
	String[] days = {"일", "월", "화", "수", "목", "금", "토"};
	int yy, mm, dd, day;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	HashMap<Integer, Double> orderMovie = new HashMap<Integer, Double>();
	ArrayList<Movie> movies = new ArrayList<Movie>();
	
	boolean isDragged;
	int[] offY;
	
	Image buffrImg;
	Graphics2D buffr;
	
	public MovieChoiceScreen(ClientMain main) {
		super(main); 
		selectMovieRateOrder();
		
		p_north = new JPanel();
		la_branch = new JLabel("신촌점");
		la_date = new JLabel("2017년 4월");
		la_time = new JLabel("10:00");
		
		bt_prev = new Canvas(){
			@Override
			public void paint(Graphics g) {
				URL url = getClass().getResource("/bt_prev.png");
				try {
					Image img = ImageIO.read(url);
					g.drawImage(img, 0, 26, 10, 35, this);
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
		};
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
		
		setToday();
		
		p_north.setPreferredSize(new Dimension(800, 80));
		la_branch.setPreferredSize(new Dimension(250, 80));
		la_date.setPreferredSize(new Dimension(250, 80));
		la_time.setPreferredSize(new Dimension(250, 80));
		bt_prev.setPreferredSize(new Dimension(10, 80));
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
		
		
		bt_prev.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Date date = new Date();
				cal.setTime(date);
				String today = sdf.format(cal.getTime());
				
				cal.set(yy, mm, dd-1);
				String select = sdf.format(cal.getTime());
				
				System.out.println(select);
				System.out.println(today);
				
				if(Integer.parseInt(today) <= Integer.parseInt(select)){
					cal.set(yy, mm+1, 0);
					int lastDate = cal.get(Calendar.DATE);
					
					if(dd == 1){
						mm--;
						dd = lastDate;
					}else dd--;
					
					if(day == 1){
						day = 7;
					}else day--;
					
					setDate();
					
					remove(container);
					movies.removeAll(movies);
					createMovieContainer(today, select);
					container.repaint();
				}
			}
		});
		
		bt_next.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Date date = new Date();
				cal.setTime(date);
				String today = sdf.format(cal.getTime());
				cal.add(Calendar.DATE, 7);
				String sevenAfter = sdf.format(cal.getTime());
				
				cal.set(yy, mm, dd+1);
				String select = sdf.format(cal.getTime());
				
				if(Integer.parseInt(select) <= Integer.parseInt(sevenAfter)){ 
					cal.set(yy, mm+1, 0);
					int lastDate = cal.get(Calendar.DATE);
					
					if(dd == lastDate){
						mm++;
						dd = 1;
					}else dd++;
					
					if(day == 7){
						day = 1;
					}else day++;
					
					setDate();
					
					System.out.println("today="+today);
					System.out.println("select="+select);

					remove(container);
					movies.removeAll(movies);
					createMovieContainer(today, select);
					container.repaint();
				}
			}
		});
		
		p_north.add(la_branch);
		p_north.add(bt_prev);
		p_north.add(la_date);
		p_north.add(bt_next);
		p_north.add(la_time);
		
		add(p_north);
		
		Date date = new Date();
		cal.setTime(date);
		String today = sdf.format(cal.getTime());
		
		//예매율 순으로 영화 아이디를 얻어온다
		selectMovieRateOrder();
		createMovieContainer(today, today);
		offY = new int[movies.size()];
	}
	
	public void setToday(){
		yy = cal.get(Calendar.YEAR);
		mm = cal.get(Calendar.MONTH);
		dd = cal.get(Calendar.DATE);
		day = cal.get(Calendar.DAY_OF_WEEK);
		setDate();
	}
	
	public void setDate(){
		StringBuffer date = new StringBuffer();
		
		date.append(mm+1);
		date.append("월");
		date.append(dd);
		date.append("일");
		date.append("(");
		date.append(days[day-1]);
		date.append(")");
		
		la_date.setText(date.toString());
	}
	
	public void createMovieContainer(String today, String selectDay){
		Iterator it = sortByValue(orderMovie).iterator();
		
		//일단 모든 영화에 대한 movie객체를 만들어 배열에 담아놓는다
		while(it.hasNext()){
			int movie_id = (int)it.next();
			if(today.equals(selectDay)){
				selectMovieTime(movie_id);
			}else{
				selectMovieTimeNotToday(movie_id);
			}
		}
		
		System.out.println(movies.size());
		InitScreen initScreen = (InitScreen)main.screen.get(0);
		MenuChoiceScreen menuScreen = (MenuChoiceScreen)main.screen.get(1);
		
		String poster = movies.get(0).getPoster();
		if(poster!=null){
			try {
				initScreen.poster_url = new URL("http://localhost:8989/image/movie/"+poster);
				initScreen.touch.repaint();
				menuScreen.poster_url = new URL("http://localhost:8989/image/movie/"+poster);
				menuScreen.poster.repaint();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			//initScreen.poster_url = getClass().getResource("/"+poster);
			//menuScreen.poster_url = getClass().getResource("/"+poster);
			
		}
	
		int height = 300;
		int y_point = 0;
		
		for(int i=0; i<movies.size(); i++){
			Movie movie = movies.get(i);
			movie.setLocation(0, y_point);
			
			height = 300;
			int x = 0;
			int y = 0;
			
			for(int j=0; j<movie.getTimeCard().size(); j++){
				if(x == 3){
					x = 0;
					y++;
				}
				x++;
			}
			if(y >= 2){
				height += (y-1) * 100;
			}
			movie.setSize(730, height);
			y_point += height;
		}
		
		container = new Canvas() {
			@Override
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D)g;
				
				buffrImg = createImage(750, 1000);
				buffr = (Graphics2D)buffrImg.getGraphics();
				
				int height = 300;
				int y_point = 0;
				
				//영화별로 화면에 뿌려준다
				for(int i=0; i<movies.size(); i++){
					buffr.setColor(Color.WHITE);
								
					Movie movie = movies.get(i);
					
					height = 300;
					int x = 0;
					int y = 0;
					
					for(int j=0; j<movie.getTimeCard().size(); j++){
						MovieTime timeCard = movie.getTimeCard().get(j);
						Point point = movie.getLocation();
						
						if(x == 3){
							x = 0;
							y++;
						}
						
						timeCard.setBounds(point.x+250+(x*160), point.y+60+(y*110), 150, 100);
						Point cardPoint = timeCard.getLocation();
						
						buffr.setFont(new Font("Malgun Gothic", Font.PLAIN, 40));
						buffr.drawString(timeCard.getStart_time(), cardPoint.x+30, cardPoint.y+40);
						buffr.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
						buffr.setColor(new Color(66,106,126));
						buffr.fillRect(cardPoint.x, cardPoint.y+50, 150, 25);
						buffr.setColor(Color.BLACK);
						buffr.drawString(timeCard.getTheater_name(), cardPoint.x+10, cardPoint.y+70);
						buffr.setColor(new Color(179,51,81));
						buffr.fillRect(cardPoint.x, cardPoint.y+75, 150, 25);
						buffr.setColor(Color.BLACK);
						buffr.drawString(timeCard.getRemaining_seat()+"/"+timeCard.getTotal_seat(), cardPoint.x+50, cardPoint.y+95);
						buffr.setColor(new Color(66,106,126));
						buffr.draw(timeCard);
						buffr.setColor(Color.WHITE);
						x++;
					}
					
					if(y >= 2){
						height += (y-1) * 100;
					}
					movie.setSize(730, height);
					
					URL url = null;
					try {
						url = new URL("http://localhost:8989/image/movie/"+movie.getPoster());
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
					//URL url = getClass().getResource("/"+movie.getPoster());
					Image img;
					try {
						img = ImageIO.read(url);
						buffr.drawImage(img, movie.x+25, movie.y+25, 200, 250, this);
					} catch (IOException e) {
						e.printStackTrace();
					}
					buffr.setFont(new Font("Malgun Gothic", Font.PLAIN, 30));
					buffr.drawString(movie.getMovie_name(), movie.x+250, movie.y+40);
					//buffr.draw(movie);
					y_point += height;
				}
				g2.drawImage(buffrImg, 0, 0, 750, 1000, this);
			}
			
			@Override
			public void update(Graphics g) {
				paint(g);
			}
		};
		container.setPreferredSize(new Dimension(750, 1000));
		
		container.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Point point = e.getPoint();
				
				for(int i=0; i<movies.size(); i++){
					for(int j=0; j<movies.get(i).getTimeCard().size(); j++){
						MovieTime movieTime = movies.get(i).getTimeCard().get(j);
						
						if(movieTime.contains(point)){
							System.out.println(movies.get(i).getMovie_name() +", "+movieTime.start_time+" 클릭했어???");
							main.selectList.setProduct_id(movieTime.getProduct_id());
							main.setPage(5);
						}
					}
				}
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				isDragged = true;
				for(int i=0; i<movies.size(); i++){
					offY[i] = e.getY() - movies.get(i).y;
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				isDragged = false;
			}
		});
		
		container.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				if(isDragged){
					for(int i=0; i<movies.size(); i++){
						movies.get(i).y = e.getY()-offY[i];
						movies.get(i).translate(0, 10);
					}
					
				}
				container.repaint();
			}
		});
		add(container);
	}
	
	
	
	public void selectMovieRateOrder(){
		//영화 아이디, 예매건수
		ArrayList <Integer> movieId = new ArrayList<Integer>();
		int[] rate;
		int total = 0;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer();
		
		try {
			sql.append("select movie_id from movie");
			sql.append(" where start_date <= (select sysdate from dual)");
			sql.append(" and end_date >= (select sysdate from dual)");
			pstmt = main.con.prepareStatement(sql.toString());
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				movieId.add(rs.getInt("movie_id"));
			}
			
			rate = new int[movieId.size()];
			
			sql.delete(0, sql.length());
			sql.append("select count(*) as 전체예매 from product p");
			sql.append(" inner join buy_seat bs on p.product_id = bs.product_id ");
			sql.append(" inner join order_movie om on bs.order_id = om.order_id");
			sql.append(" where order_time between (select sysdate-1 from dual) ");
			sql.append(" and (select sysdate from dual)");
			pstmt = main.con.prepareStatement(sql.toString());
			
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				total = rs.getInt("전체예매");
			}
			sql.delete(0, sql.length());
			sql.append("select count(*) as 부분예매 from product p");
			sql.append(" inner join buy_seat bs on p.product_id = bs.product_id ");
			sql.append(" inner join order_movie om on bs.order_id = om.order_id");
			sql.append(" where order_time between (select sysdate-1 from dual)");
			sql.append(" and (select sysdate from dual)");
			sql.append(" and movie_id=?");
			
			for(int i=0; i<movieId.size(); i++){
				pstmt = main.con.prepareStatement(sql.toString());
				pstmt.setInt(1, movieId.get(i));
				rs = pstmt.executeQuery();
				if(rs.next()){
					rate[i] = rs.getInt("부분예매");
				}
			}
			
			for(int i=0; i<movieId.size(); i++){
				double r;
				if(total!=0){
					r = ((double)rate[i]/total)*100;
				}else{
					r = 0;
				}
				orderMovie.put(movieId.get(i), r);
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
	
	public static List sortByValue(final Map map) {
        List<String> list = new ArrayList();
        list.addAll(map.keySet());
         
        Collections.sort(list,new Comparator() {
             
            public int compare(Object o1,Object o2) {
                Object v1 = map.get(o1);
                Object v2 = map.get(o2);
                 
                return ((Comparable) v2).compareTo(v1);
            }
             
        });
        //Collections.reverse(list);
        return list;
    }
	
	public void selectMovieTime(int movie_id){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		boolean isFirst = true;
		Movie movie = new Movie();
		ArrayList<MovieTime> timeCards = new ArrayList<MovieTime>();
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select p.product_id as 상품아이디, p.start_time as 시작시간, t.theater_id as 관아이디,");
		sql.append(" t.count as 총좌석수, t.name as 관이름, m.name as 영화이름, m.poster as 포스터, t.count-count(b.buy_seat_id) as 남은좌석수");
		sql.append(" from product p");
		sql.append(" full outer join theater t on p.theater_id = t.theater_id");
		sql.append(" full outer join movie m on m.movie_id = p.movie_id");
		sql.append(" full outer join buy_seat b on p.product_id = b.product_id");
		sql.append(" where screening_date = ?");
		sql.append(" and start_time > (select to_char(sysdate, 'hh24:mi') from dual)");
		sql.append(" and m.movie_id = ?");
		sql.append(" group by p.product_id, p.start_time, t.theater_id, t.count, t.name, m.name, m.poster");
		sql.append(" order by p.start_time");
		
		try {
			pstmt = main.con.prepareStatement(sql.toString());
			pstmt.setString(1, yy+"-"+(mm+1)+"-"+dd);
			pstmt.setInt(2, movie_id);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				if(isFirst){
					isFirst = false;
					movie.setMovie_name(rs.getString("영화이름"));
					movie.setPoster(rs.getString("포스터"));
				}
				MovieTime timeCard = new MovieTime();
				timeCard.setProduct_id(rs.getInt("상품아이디"));
				timeCard.setTheater_id(rs.getInt("관아이디"));
				timeCard.setStart_time(rs.getString("시작시간"));
				timeCard.setTheater_name(rs.getString("관이름"));
				timeCard.setTotal_seat(rs.getInt("총좌석수"));
				timeCard.setRemaining_seat(rs.getInt("남은좌석수"));
				
				timeCards.add(timeCard);
			}
			movie.setTimeCard(timeCards);
			movies.add(movie);
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
	
	public void selectMovieTimeNotToday(int movie_id){
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		boolean isFirst = true;
		Movie movie = new Movie();
		ArrayList<MovieTime> timeCards = new ArrayList<MovieTime>();
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select p.product_id as 상품아이디, p.start_time as 시작시간, t.theater_id as 관아이디,");
		sql.append(" t.count as 총좌석수, t.name as 관이름, m.name as 영화이름, m.poster as 포스터, t.count-count(b.buy_seat_id) as 남은좌석수");
		sql.append(" from product p");
		sql.append(" full outer join theater t on p.theater_id = t.theater_id");
		sql.append(" full outer join movie m on m.movie_id = p.movie_id");
		sql.append(" full outer join buy_seat b on p.product_id = b.product_id");
		sql.append(" where screening_date = ?");
		sql.append(" and m.movie_id = ?");
		sql.append(" group by p.product_id, p.start_time, t.theater_id, t.count, t.name, m.name, m.poster");
		sql.append(" order by p.start_time");
		
		try {
			pstmt = main.con.prepareStatement(sql.toString());
			pstmt.setString(1, yy+"-"+(mm+1)+"-"+dd);
			pstmt.setInt(2, movie_id);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				if(isFirst){
					isFirst = false;
					movie.setMovie_name(rs.getString("영화이름"));
					movie.setPoster(rs.getString("포스터"));
				}
				MovieTime timeCard = new MovieTime();
				timeCard.setProduct_id(rs.getInt("상품아이디"));
				timeCard.setTheater_id(rs.getInt("관아이디"));
				timeCard.setStart_time(rs.getString("시작시간"));
				timeCard.setTheater_name(rs.getString("관이름"));
				timeCard.setTotal_seat(rs.getInt("총좌석수"));
				timeCard.setRemaining_seat(rs.getInt("남은좌석수"));
				
				timeCards.add(timeCard);
			}
			movie.setTimeCard(timeCards);
			movies.add(movie);
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

