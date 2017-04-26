package com.manage.movie;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
import java.sql.SQLException;
import java.time.LocalDate;
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
import com.manage.main.Main;

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
	
	JButton bt_confirm, bt_cancel;
	
	Toolkit kit=Toolkit.getDefaultToolkit();
	Image img;
	
	Main main;
	
	File file;
	JFileChooser chooser;
	String filePath;
	
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
    
    // ClientMain�� url
    URL url;
    
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
		
		chooser=new JFileChooser("C:/Hyeona/myServer/data");
		
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
		p_outer.add(bt_confirm);
		p_outer.add(bt_cancel);
		add(p_outer);
		
		setSize(500, 600);
		setLocationRelativeTo(movieMain);
		setVisible(true);
		
		// DB ����
		connect();

	}
	
	// DB ����
	public void connect(){
		manager=DBManager.getInstance();
		con=manager.getConnect();
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
		sql_insert.append("insert into movie(movie_id, poster, name, director, main_actor, story, start_date, end_date, run_time)");
		sql_insert.append(" values(seq_movie.nextval, ?, ?, ?, ?, ?, ?, ?, ?)");
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
				pstmt.setString(6, startDatePicker.getValue().toString());
				pstmt.setString(7, endDatePicker.getValue().toString());
				pstmt.setInt(8, Integer.parseInt(t_run_time.getText()));
				
				// ���������� insert�ߴٸ� ��ȯ���� 1
				int result=pstmt.executeUpdate();
				if(result!=0){
					JOptionPane.showMessageDialog(this, "��ȭ �߰� �Ϸ�");
					
					// ��ϰ� ���ÿ� movie_id�� ���
					//getMovieId();
					
					// ��� �Ϸ������� ������ ���� ����
					copyPoster();
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
	}
	
	// �Է°��� ��ȿ���� �˻�
		public void checkDataFormat(){
			// �Է°��� ������ ����
			//String poster=file.getName();
			String title=t_title.getText();
			String director=t_director.getText();
			String actor=t_actor.getText();
			String story=ta_story.getText();
			String run_time=t_run_time.getText();
			
			start_date=startDatePicker.getValue();
			end_date=endDatePicker.getValue();
			
			// �Է°� �������� �Լ� ȣ��
			if(file==null){
				JOptionPane.showMessageDialog(this, "��ȭ �����Ͱ� ���õ��� �ʾҽ��ϴ�.");
				return;
			}
			if(!DataValidTest.isNumber(run_time)){
				JOptionPane.showMessageDialog(this, "�� �ð��� ���ڷ� �Էµ��� �ʾҽ��ϴ�.");
				return;
			}
			if(Integer.parseInt(run_time)<0){
				JOptionPane.showMessageDialog(this, "�� �ð��� ����� �Է����ּ���.");
				return;
			}

			// ��ȿ�� �˻簡 ������ ���� ���� ����
			insertMovie();
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
		filePath=file.getAbsolutePath();
		System.out.println(filePath);
		Main main=Main.getMain();
		main.upload(filePath);
	}
	
	
	// �ϳ��� frame�� ������ ����ϹǷ� ���� ������ �׻� �ʱ�ȭ
	public void setDefault(){
		t_title.setText(txtDefault[0]);
		t_director.setText(txtDefault[1]);
		t_actor.setText(txtDefault[2]);
		ta_story.setText(txtDefault[3]);
		t_run_time.setText(txtDefault[4]);
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
			checkDataFormat();
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
				if(txt.getText().trim().equals("")){
					String value=getTextField(i);
					txt.setText(value);
				}
			}
		}
	}
	
}
