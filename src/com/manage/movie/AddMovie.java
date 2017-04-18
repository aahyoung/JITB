package com.manage.movie;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.jitb.db.DBManager;

import javafx.embed.swing.JFXPanel;
import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


/*
 * ��ȭ �߰� ���̾ƿ�
 * ��ȭ �߰� �� DB ����
 * 1. getTheaterList()		: ���� �Ҵ� ������ ��ȭ�� ��� choice�� ����
 * 2. insertMovie()			: ���� ���̾ƿ����� �Է��� ������ movie���̺� ����
 * 3. getMovieId()			: ���� �ֱٿ� ����� ���ڵ��� movie_id ���
 * 4. setScreeningDate()	: movie_id�� �̿��� screening_date���̺� ������ ����
 * 5. getScreeningDateId()	: ���� �ֱٿ� ����� ���ڵ��� screening_date_id ���(�ݺ������� ���� ��ȭ �߰� �� insert�� ��� screening_date_id ����)
 * 6. calStartTime()		: ���� �ֱٿ� ����� ��ȭ ���ڵ��� run_time�� �̿��Ͽ� �� ���� �ð� ���
 * 7. setStartTime()		: screening_date_id�� �̿��� ���� �� ���� �ð��� start_time���̺� ����
 * 8. getStartTimeId()		: ���� �ֱٿ� ����� ���ڵ��� start_time_id ���(�ݺ������� ���� ��ȭ �߰� �� insert�� ��� start_time_id ����)
 * 9. setTheaterOperate()	: start_time_id�� �̿��� �Ҵ�� �󿵰� ������ theater_operate���̺� ����
 * 10. getTheaterOperateId(): ���� �ֱٿ� ����� ���ڵ��� theater_opearate_id ���(�ݺ������� ���� ��ȭ �߰� �� insert�� ��� theater_operate_id ����)
 * 11. setSeat()			: theater_operate_id�� �̿��� ����-�󿵽ð�-�󿵰�-�̸�-���� seat���̺� ����
 * */

public class AddMovie extends JDialog implements ActionListener, FocusListener{
	JPanel p_outer;
	
	JFXPanel p_date;
	
	Canvas can;
	
	JLabel lb_title;
	JTextField t_title;
	JLabel lb_director;
	JTextField t_director;
	JLabel lb_actor;
	JTextField t_actor;
	JLabel story;
	JTextArea ta_story;
	JLabel lb_start_date;
	// Date Picker
	JLabel lb_end_date;
	// Date Picker
	JLabel lb_run_time;
	JTextField t_run_time;
	
	// �Ҵ��� ��ȭ�� ����
	Choice ch_theater;
	
	JButton bt_confirm, bt_cancel;
	
	Toolkit kit=Toolkit.getDefaultToolkit();
	Image img;
	
	File file;
	JFileChooser chooser;
	
	// Date Picker
	private Stage stage;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    
    // DB ����
    DBManager manager;
    Connection con;
    
    // textArea�� Ŀ�� �ø� �� ���͸�ũ
    boolean Flag=false;
    
    // textField default��
    String[] txtDefault={
    		"��ȭ ������ �Է��ϼ���.",
    		"��ȭ ������ �Է��ϼ���.",
    		"�ֿ� ��츦 �Է��ϼ���.",
    		"���丮�� �Է��ϼ���.",
    		"�� �ð��� �Է��ϼ���.",
    };
    
    JTextField[] t_input=new JTextField[5];
    
    ArrayList<TheaterItem> theater=new ArrayList<TheaterItem>();
    
    // ��ȭ�� ���� index
    int theater_index;
    
    // ��ȭ id
    int movie_id;
    
    // �� ���ϼ�
    int run_date;
    
    // ��ȭ �󿵽ð�
    int run_time;
    
    // �� ��¥ id
    ArrayList screening_date_id=new ArrayList();
    
    // �� ���۽ð� id
    //int[] start_time_id=new int[7];
    ArrayList start_time_id=new ArrayList();
    
    // theater_operate id
    ArrayList theater_operate_id=new ArrayList();
    
    // �θ� �г�
    MovieMain movieMain;
    
    // �� ��¥��(��ȭ)���� �ð� ���
    String[] start_time=new String[7];
    
    // DB�κ��� ���� �Ҵ� ������ ��ȭ�� ������ ��Ƴ��� collection framework
 	ArrayList<Theater> theaterList=new ArrayList<Theater>();
 	
 	LocalDate start_date;
 	LocalDate end_date;
 
	public AddMovie(MovieMain movieMain) {
		this.movieMain=movieMain;
		
		URL url=this.getClass().getResource("/shrek.jpg");
		
		try {
			img=ImageIO.read(url);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		// ������ ���
		can=new Canvas(){
			
			public void paint(Graphics g) {
				// ������ ���̱�
				g.drawImage(img, 0, 0, 200, 300, this);
			}
		};
		
		can.setPreferredSize(new Dimension(500, 300));
		can.setBounds(0, 0, 200, 300);
		
		chooser=new JFileChooser("C:/Users/user/Pictures/AC3");
		
		can.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent e) {
				getPoster();
			}
		});
		
		p_outer=new JPanel();
		p_date=new JFXPanel();
		p_date.setLayout(new BorderLayout());
		
		t_title=new JTextField(txtDefault[0],20);
		t_director=new JTextField(txtDefault[1],20);
		t_actor=new JTextField(txtDefault[2],20);
		ta_story=new JTextArea(txtDefault[3],4,20);
		t_run_time=new JTextField(txtDefault[4],20);
		ch_theater=new Choice();
		
		bt_confirm=new JButton("Ȯ��");
		bt_cancel=new JButton("���");
		
		bt_confirm.addActionListener(this);
		bt_cancel.addActionListener(this);
		
		t_title.addFocusListener(this);
		t_director.addFocusListener(this);
		t_actor.addFocusListener(this);
		ta_story.addFocusListener(this);
		t_run_time.addFocusListener(this);
		
		createCalendar();
				
		p_date.setLayout(new GridLayout(1, 2));
		p_outer.add(can);
		p_outer.add(t_title);
		p_outer.add(t_director);
		p_outer.add(t_actor);
		p_outer.add(ta_story);
		p_outer.add(p_date);
		p_outer.add(t_run_time);
		p_outer.add(ch_theater);
		p_outer.add(bt_confirm);
		p_outer.add(bt_cancel);
		add(p_outer);
		
		setBounds(250, 50, 500, 600);
		setVisible(true);
		
		// DB ����
		connect();
		
		// ��ȭ ����Ʈ ��������
		getTheaterList();
	}
	
	// DB ����
	public void connect(){
		manager=DBManager.getInstance();
		con=manager.getConnect();
	}
	
	// ���� �Ҵ� ������ ��ȭ�� ��� �޾ƿ���
	public void getTheaterList(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		theaterList.removeAll(theaterList);
		ch_theater.removeAll();
		
		// �������� ���� ���� not in
		// theater_operate ���̺��� theater_id�� ���� theater���̺��� theater_id ����
		String sql="select * from theater where theater_id not in(select theater_id from theater_operate) order by theater_id asc";
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
	
			while(rs.next()){			
				Theater dto=new Theater();
				dto.setTheater_id(rs.getInt("theater_id"));
				dto.setBranch_id(rs.getInt("branch_id"));
				dto.setName(rs.getString("name"));
				dto.setRow_line(rs.getInt("row_line"));
				dto.setColumn_line(rs.getInt("column_line"));
				
				theaterList.add(dto);
				
				ch_theater.add(dto.getName()+" ��");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
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
	
	// ������ ��ȭ ���� ����
	// -> movie ���̺� ������ ����
	public void insertMovie(){
		PreparedStatement pstmt=null;
		/*
		// �Է°��� ������ ����
		String poster=file.getName();
		String title=t_title.getText();
		String director=t_director.getText();
		String actor=t_actor.getText();
		String story=ta_story.getText();
		int run_time=Integer.parseInt(t_run_time.getText());
		*/
		//LocalDate start_date=startDatePicker.getValue();
		//LocalDate end_date=endDatePicker.getValue();
		start_date=startDatePicker.getValue();
		end_date=endDatePicker.getValue();
		
		System.out.println(start_date.toString()+","+end_date.toString());
		
		// movie_id, poster, name, director, main_actor, story, start_date, end_date, run_time
		StringBuffer sql_insert=new StringBuffer();
		sql_insert.append("insert into movie(movie_id, poster, name, director, main_actor, story, run_time)");
		sql_insert.append(" values(seq_movie.nextval, ?, ?, ?, ?, ?, ?)");
		//sql.append("to_date(?,'YYYY-MM-DD'), ");
		//sql.append("to_date(?,'YYYY-MM-DD'), ?)");
		
		// ��������(���������� �� �������Ф�)
		/* 1. �̹������� ����� ������
		 * 2. ��ȭ �̸��� null�� �ƴϰ�
		 * 3. ��ȭ ������ null�� �ƴϰ�
		 * 4. �ֿ� ��찡 null�� �ƴϰ�
		 * 5. ���丮�� null�� �ƴϰ�
		 * 6. �󿵽ð��� 0���� Ŀ���ϰ�
		 * 7. �������ڰ� �������ں��� ũ�� �ȵǰ�
		 * */
		//if(file.getName()!=null&&!t_title.getText().equals("")&&!t_director.getText().equals("")&&!t_actor.getText().equals("")&&!ta_story.getText().equals("")&&Integer.parseInt(t_run_time.getText())>0&&start_date.isAfter(end_date)){
			try {
				//pstmt=con.prepareStatement(sql.toString());
				pstmt=con.prepareStatement(sql_insert.toString());
				pstmt.setString(1, file.getName());
				pstmt.setString(2, t_title.getText());
				pstmt.setString(3, t_director.getText());
				pstmt.setString(4, t_actor.getText());
				pstmt.setString(5, ta_story.getText());
				//pstmt.setString(6, startDatePicker.getValue().toString());
				//pstmt.setString(7, endDatePicker.getValue().toString());
				pstmt.setInt(6, Integer.parseInt(t_run_time.getText()));
				
				// ���������� insert�ߴٸ� ��ȯ���� 1
				int result=pstmt.executeUpdate();
				if(result!=0){
					JOptionPane.showMessageDialog(this, "��ȭ �߰� �Ϸ�");
					
					// ��ϰ� ���ÿ� movie_id�� ���
					getMovieId();
					
					// ��� �Ϸ������� ������ ���� ����
					copyPoster();
					
					// screening_date ���̺� ����
					setScreeningDate(start_date, end_date);
					
					// start_time ���̺� ����
					setStartTime();
					
					// theater_operate ���̺� ����
					setTheaterOperate();
					
					// seat ���̺� ����
					setSeat();
					/*
					// start_time ���̺� ����
					setStartTime();
					
					// theater_operate ���̺� ����
					setTheaterOperate();
					
					// seat ���̺� ����
					setSeat();
					*/
				}
				else{
					JOptionPane.showMessageDialog(this, "��ȭ �߰� ����");
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
				if(pstmt!=null){
					try {
						pstmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		//}
	/*
		else{
			JOptionPane.showMessageDialog(this, "�Է°��� ����� �Է����ּ���.");
		}
	*/
	}
	
	// ������ ���
	public void getPoster(){
		int result=chooser.showOpenDialog(this);
		if(result==JFileChooser.APPROVE_OPTION){
			file=chooser.getSelectedFile();
			img=kit.getImage(file.getAbsolutePath());
			can.repaint();
		}
	}
	
	// ��ȭ�� ����ϸ� ������ ����
	public void copyPoster(){
		FileInputStream fis=null;
		FileOutputStream fos=null;
		
		try {
			fis=new FileInputStream(file);
			fos=new FileOutputStream("res_manager/"+file.getName());
			
			byte[] b=new byte[1024];
			int flag;
			while(true){
				flag=fis.read(b);
				if(flag==-1){
					break;
				}
				fos.write(b);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	// ��ȭ ��ϰ� ���ÿ� insert�� movie���̺��� record_id �ޱ�
	public void getMovieId(){
		System.out.println("movie_id���");
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select seq_movie.currval from dual";
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			rs.next();
			movie_id=rs.getInt("currval");
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			
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

	// ��ȭ ��ϰ� ���ÿ� screening_date�� movie_id�� screening_date ����
	public void setScreeningDate(LocalDate start_date, LocalDate end_date){
		PreparedStatement pstmt_insert=null;
		PreparedStatement pstmt_select=null;
		
		ResultSet rs=null;
		
		StringBuffer sql=new StringBuffer();
		sql.append("insert into screening_date(screening_date_id, movie_id, screening_date)");
		sql.append(" values(seq_screening_date.nextval, ?, ");
		sql.append("to_date(?, 'YYYY-MM-DD'))");
		
		Period period=Period.between(start_date, end_date);
		run_date=period.getDays()+1;
		
		String sql2="select seq_screening_date.currval from dual";
		
		try {
			
			for(int i=0; i<run_date; i++){
				pstmt_insert=con.prepareStatement(sql.toString());
				pstmt_insert.setInt(1, movie_id);
				pstmt_insert.setString(2, start_date.plusDays(i).toString());
				pstmt_insert.executeQuery();
				
				pstmt_select=con.prepareStatement(sql2);
				rs=pstmt_select.executeQuery();
				
				rs.next();
				screening_date_id.add(rs.getInt("currval"));
				
				// screening_date_id�� ���
				//getScreeningDateId();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
	}
	
	// ��ȭ ��ϰ� ���ÿ� insert�� screening_date���̺��� record_id �ޱ�
	public void getScreeningDateId(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		screening_date_id.removeAll(screening_date_id);
		
		String sql="select seq_screening_date.currval from dual";
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			rs.next();
			screening_date_id.add(rs.getInt("currval"));
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
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
	
	// ��ȭ ��ϰ� ���ÿ� start_time ���̺� start_time�� ����ϰ� screening_date_id�� �޾� ����
	public void setStartTime(){
		
		calStartTime();
		
		PreparedStatement pstmt=null;
		
		// ��ȭ�� �󿵽��۽ð���ŭ insert
		StringBuffer sql=new StringBuffer();
		sql.append("insert into start_time(start_time_id, screening_date_id, start_time)");
		sql.append(" values(seq_start_time.nextval, ?, ?)");
		
		try {
			// ��ȭ �� ��¥ �ϳ��� start_time�迭 ���̸�ŭ
			for(int i=0; i<screening_date_id.size(); i++){
				for(int j=0; j<start_time.length; j++){
					pstmt=con.prepareStatement(sql.toString());
					pstmt.setInt(1, (Integer)screening_date_id.get(i));
					pstmt.setString(2, start_time[j]);
					pstmt.executeQuery();
					
					// start_time_id�� ���
					getStartTimeId();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}	
	}
	
	// ���� �󿵽ð��� ���ϴ� �޼ҵ�
	// screening_date, �����ڸ�ŭ �����ǹǷ� for�� Ƚ���� movie_id�� �ҷ��� screening_date ���̺� ���ڵ� ��
	public void calStartTime(){
		int run_time;
		int hour;		// ���� �ð��� �� ������ ���
		int min;		// ���� ��
		int result;		// ���� ���� �ð� : �� ������ ���۽ð� + �󿵽ð� + 30

		// ǥ���ϱ� ���� string ��ȯ
		//String[] start_time=new String[7];
		start_time[0]="09:00";	// �׻� ��ȭ�� ù��° �� �ð��� ���� 9��
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select run_time from movie where movie_id=?";
		try {
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, movie_id);
			rs=pstmt.executeQuery();
			rs.next();
			run_time=rs.getInt("run_time");
			
			// �� �ð��� 7Ÿ��
			for(int i=1; i<7; i++){
				// screening_date id�� �̿��ؼ� start_time���̺� ���� �����ؾ� ��
				// ���� ��ȭ�� �󿵽ð��� �̿��Ͽ� ���
				/*
				 * ���� ���� �ð� - 09:32
				 * ��ȭ ���� Ÿ�� - 165��
				 * ���� ���� �ð� - 09:32 + 135�� => 
				 * -> ���۽ð�*60 + ���۽ð� �� => 9*60+32=572
				 * -> �д����� ��ȯ�� ���۽ð� + �󿵽ð� + 30�� ���½ð� = ���� ���۽ð� => 767��
				 * -> �д����� ���� ���۽ð�/60 => 12(��)
				 * -> �д����� ���� ���۽ð�%60 => 47(��)
				 * => ���� ���� �ð��� 12�� 47��
				 * */
				String[] divide=start_time[i-1].split(":");
				hour=Integer.parseInt(divide[0])*60;
				min=Integer.parseInt(divide[1]);
				result=hour+min+run_time+20;
				
				// �� ������ 10���� �۾Ƽ� 14:2 �̷��� �� ����
				if(result%60<10){
					start_time[i]=result/60+":0"+result%60;
				}
				else{
					start_time[i]=result/60+":"+result%60;
				}

				System.out.println(start_time);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
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
	
	// ��ȭ ��ϰ� ���ÿ� insert�� start_time���̺��� record_id �ޱ�
	public void getStartTimeId(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select seq_start_time.currval from dual";
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			rs.next();
			start_time_id.add(rs.getInt("currval"));
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
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
	
	// ��ȭ�� �� �ð��� ���� �� ����
	public void setTheaterOperate(){
		PreparedStatement pstmt=null;
		StringBuffer sql=new StringBuffer();
		
		sql.append("insert into theater_operate(theater_operate_id, theater_id, start_time_id)");
		sql.append(" values(seq_theater_operate.nextval, ?, ?)");
		
		// ���� ������ ��ȭ�� id �ޱ�
		int theater_id=theaterList.get(ch_theater.getSelectedIndex()).getTheater_id();
		
		try {
			for(int i=0; i<start_time_id.size(); i++){
				pstmt=con.prepareStatement(sql.toString());
				pstmt.setInt(1, theater_id);
				pstmt.setInt(2, (Integer)start_time_id.get(i));
				pstmt.executeQuery();
				
				getTheaterOperateId();
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	// theater_operate�� insert�� ������ theater_operate ���̺��� record_id �ޱ�
	public void getTheaterOperateId(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select seq_theater_operate.currval from dual";
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			rs.next();
			theater_operate_id.add(rs.getInt("currval"));
			
			System.out.print(theater_operate_id+" ");
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
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
	
	//cannot insert NULL into ("JITB"."SEAT"."NAME") ���� �߻�
	// �¼� ���̺� ����
	public void setSeat(){
		PreparedStatement pstmt=null;
		
		StringBuffer sql=new StringBuffer();
		sql.append("insert into seat(seat_id, theater_operate_id, name, status)");
		sql.append(" values(seq_seat.nextval, ?, ?, 1)");
		
		// �࿭ ���ϰ� �� ��� ������ �¼���ȣ �����
		int row=theaterList.get(ch_theater.getSelectedIndex()).getRow_line();
		int col=theaterList.get(ch_theater.getSelectedIndex()).getColumn_line();
		
		String[] seatName=new String[row*col];
		
		// 4�� 3���� ���
		// A1 A2 A3
		// B1 B2 B3
		// C1 C2 C3
		// D1 D2 D3
		// seatName[i][j]=Character.toString((char)(65+i));
		// seatName[i][j]+=Integer.toString(j+1);
		int count=0;

		for(int i=0; i<row; i++){
			for(int j=0; j<col; j++){
				seatName[count]=Character.toString((char)(65+i))+Integer.toString(j+1);
				count++;
			}
		}
		
		try {
			// theater_operate_id �ϳ��� row*col ���� seat �Ҵ�
			for(int i=0; i<theater_operate_id.size(); i++){
				for(int j=0; j<seatName.length; j++){
					pstmt=con.prepareStatement(sql.toString());
					pstmt.setInt(1, (Integer)theater_operate_id.get(i));
					pstmt.setString(2, seatName[j]);
					pstmt.executeQuery();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// �ϳ��� frame�� ������ ����ϹǷ� ���� ������ �׻� �ʱ�ȭ
	public void setDefault(){
		t_title.setText(txtDefault[0]);
		t_director.setText(txtDefault[1]);
		t_actor.setText(txtDefault[2]);
		ta_story.setText(txtDefault[3]);
		t_run_time.setText(txtDefault[4]);
		ch_theater.select(0);
		startDatePicker.setValue(LocalDate.now());
		endDatePicker.setValue(startDatePicker.getValue().plusDays(1));
		
	}
	
	public void createCalendar(){
		Group root=new Group();
		//VBox root=new VBox(20);
		Scene scene=new Scene(root,337,50);
        startDatePicker = new DatePicker();
        endDatePicker = new DatePicker();
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(startDatePicker.getValue().plusDays(1));
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        Label checkInlabel = new Label("���� ����");
        gridPane.add(checkInlabel, 0, 0);
        GridPane.setHalignment(checkInlabel, HPos.LEFT);
        gridPane.add(startDatePicker, 0, 1);
        Label checkOutlabel = new Label("���� ����");
        gridPane.add(checkOutlabel, 1, 0);
        GridPane.setHalignment(checkOutlabel, HPos.RIGHT);
        gridPane.add(endDatePicker, 1, 1);
        root.getChildren().add(gridPane);
        p_date.setScene(scene);
        
	}
	
	// Ȯ�� ��ư�� ������
	public void actionPerformed(ActionEvent e) {
		JButton bt=(JButton)e.getSource();
		
		if(bt==bt_confirm){
			insertMovie();
			setVisible(false);
			movieMain.getMovieList();
			movieMain.p_present.updateUI();
			movieMain.p_present.setVisible(true);
			System.out.println("��ȭ �߰� Ȯ��");
		}
		else if(bt==bt_cancel){
			//this.dispose();
			this.setVisible(false);
			movieMain.p_present.setVisible(true);
			System.out.println("��ȭ �߰� ���");
		}
	}
	
	/*
	// ��ȭ�� ����
	public void itemStateChanged(ItemEvent e) {
		Object obj=e.getSource();
		if(obj==ch_theater.getSelectedItem()){
			
		}
		
	}
	*/
	
	// textfield default�� ���ϱ�
	public String getTextField(int index){
		return txtDefault[index];
	}

	// Ŀ���� �÷��� ��
	public void focusGained(FocusEvent e) {
		//JTextArea txta=(JTextArea)e.getSource();
		if(e.getComponent().equals(ta_story)){
			JTextArea txta=(JTextArea)e.getSource();
			if(txta.getText().trim().equals(txtDefault[3])){
				txta.setText("");
			}
		}
		
		else{
			JTextField txt=(JTextField)e.getSource();
			
			for(int i=0; i<5; i++){
				String value=getTextField(i);
				if(txt.getText().trim().equals(value)){
					txt.setText("");
				}
			}
		}
	}

	// Ŀ���� ���� ��
	public void focusLost(FocusEvent e) {
		
		if(e.getComponent().equals(ta_story)){
			JTextArea txta=(JTextArea)e.getSource();
			
			if(txta.getText().trim().equals("")){
				txta.setText(txtDefault[3]);
			}
		}
		else{
			JTextField txt=(JTextField)e.getSource();
			
			for(int i=0; i<5; i++){
				String value=getTextField(i);
				if(txt.getText().trim().equals("")){
					txt.setText(value);
				}
			}
		}
	}
	
}
