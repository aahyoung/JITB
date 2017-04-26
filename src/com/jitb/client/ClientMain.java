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

public class ClientMain{
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
	String filePath;
	
	ArrayList<String> path_movie=new ArrayList<String>();
	ArrayList<String> path_snack;
	
	// DB ����
	public void DBConnect(){
		manager=DBManager.getInstance();
		con=manager.getConnect();
	}
	
	// DB�κ��� �̹����� ��������
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
	
	// ���������� ��ǰ �̹����� ��������!
	public void displayProduct(){
		// http://localhost:9090 -> myserver
		// http://localhost:9090/data/�̹�����
		try {
			for(int i=0; i<path_movie.size(); i++){
				// DB���� �� ��ƿ;� ��
				// -> DB�� �̹����� ����Ǿ� �־�� ��
				// Oracle�� �̹������� �����ϰ� stream���� �̹��� ���� ��ü�� myserver�� ���
				//url_movie = new URL("http://"+ip+":9090/jitb_server/image/"+path_movie.get(i));
				//url_buy_movie=new URL("http://"+ip+":9090/jitb_server/image/");
				url_movie = new URL("http://"+ip+":9090/"+path_movie.get(i));
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	// ���� �޼ҵ� ����
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
	
	// ���� ���ε�
	public void uploadFile(String filePath, String type){
		connect();
		if(type.equals("img")){
			ct.img_send=true;
			file=new File(filePath);
		}
		else if(type.equals("excel")){
			ct.file_send=true;
			file=new File(filePath);
		}
	}
/*	
	public static void main(String[] args) {
		new ClientMain();
	}
*/


}
