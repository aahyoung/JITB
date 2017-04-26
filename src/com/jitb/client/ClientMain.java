package com.jitb.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.jitb.db.DBManager;

public class ClientMain extends JFrame implements ActionListener{
	JButton bt_load;
	Socket socket;
	//String ip="211.238.142.100";
	String ip="localhost";
	int port=9090;
	
	static URL url_movie;
	static URL url_buy_movie;
	
	DBManager manager;
	Connection con;
	
	ClientThread ct;
	
	JFileChooser chooser;
	File file;
	
	ArrayList<String> path_movie=new ArrayList<String>();
	ArrayList<String> path_snack;
	
	public ClientMain() {
		bt_load=new JButton("파일 업로드");
		bt_load.addActionListener(this);
		
		//getImage();
		//connect();
		//displayProduct();
		
		add(bt_load);
		setVisible(true);
		setSize(400, 300);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	// DB 연결
	public void DBConnect(){
		manager=DBManager.getInstance();
		con=manager.getConnect();
	}
	
	// DB로부터 이미지명 가져오기
	public void getImage(){
		DBConnect();
		
		path_movie.removeAll(path_movie);
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql="select poster from movie";
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			while(rs.next()){
				path_movie.add(rs.getString("poster"));
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
	
	// 웹서버에서 상품 이미지를 가져오자!
	public void displayProduct(){
		// http://localhost:9090 -> myserver
		// http://localhost:9090/data/이미지명
		try {
			for(int i=0; i<path_movie.size(); i++){
				// DB연동 시 담아와야 함
				// -> DB에 이미지가 저장되어 있어야 함
				// Oracle에 이미지명을 저장하고 stream으로 이미지 파일 자체를 myserver에 등록
				url_movie = new URL("http://"+ip+":9090/jitb_server/image/"+path_movie.get(i));
				url_buy_movie=new URL("http://"+ip+":9090/jitb_server/image/");
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	// 접속 메소드 정의
	public void connect(){
		try {
			socket=new Socket(ip, port);
			
			ct=new ClientThread(this, socket);
			ct.start();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 파일 업로드
	public void uploadFile(){
		int result=chooser.showOpenDialog(this);
		if(result==JFileChooser.APPROVE_OPTION){
			file=chooser.getSelectedFile();
		}
	}
	
	public static void main(String[] args) {
		new ClientMain();
	}

	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		if(obj==bt_load){
			file=new File("C:/Users/sist110/Pictures/images/cat5.jpg");
			connect();
		}
	}

}
