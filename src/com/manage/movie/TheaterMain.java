package com.manage.movie;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.jitb.db.DBManager;

/*
 * 1. ���̾ƿ� ����
 * p_north		��ȭ�� �߰� ��ư ����
 * p_content	- ��ȭ�� ����� �����ϸ� 			p_list �߰��� ��ȭ�� ��� ���
 * 				- ��ȭ�� ����� �������� ������ 		p_warning ��ȭ�� �߰� ��û ���� ���
 * 2. ��� ����
 * 1) ��ȭ�� �߰� ��ư�� JDialog���� ����
 * 2) p_list�� ��ȭ�� ����Ʈ�� ��ȭ�� �̸�/�࿭ ���� ����
 * 3) �� ��ȭ�� �г��� ������ JDialog���� ����/����/��ȭ ���� ����
 * */

public class TheaterMain extends JPanel implements ActionListener{
	JPanel p_north, p_load, p_content;
	JPanel p_list, p_warning;
	JButton bt_add, bt_form, bt_seat;
	
	JLabel lb_title, lb_warning;
	
	JScrollPane scroll;
	
	// ��ȭ�� �߰� Dialog
	AddTheater addTheater;
	
	// ��ȭ�� ����/����/��ȭ ���� Dialog
	EditTheater editTheater;
	
	// �� ��ȭ�� �г�
	TheaterItem theaterItem;
	
	// ���� ��ȭ�� �����Ͱ� �����ϴ��� ���� Ȯ��
	boolean exist=false;
	
	// ���� DB�� �����ϴ� ��ȭ�� ����
	int count;
	
	// DB������ �ʿ�
	DBManager manager;
	Connection con;
	
	// DB�κ��� ���� �����ϴ� ��ȭ�� ������ ��Ƴ��� collection framework
	ArrayList<Theater> theaterList=new ArrayList<Theater>();
	
	// ��ȭ�� �г��� ��Ƴ��� collection framework
	ArrayList<TheaterItem> theaters=new ArrayList<TheaterItem>();
	
	// ���õ� ��ȭ���� id
	int id;
	
	public TheaterMain() {
		// DB ����
		connect();
		
		// ���� �����ϴ� ��ȭ�� ���� ��� �� ���
		getTheaterList();
		
		p_north=new JPanel();
		p_load=new JPanel();
		p_content=new JPanel();
		
		p_list=new JPanel();
		p_warning=new JPanel();
		
		lb_title=new JLabel("��ȭ�� ���");
		lb_warning=new JLabel("���� ��ϵ� ��ȭ���� �����ϴ�.\n ��ȭ���� ������ּ���.");
		
		bt_add=new JButton("��ȭ�� �߰�");
		bt_form=new JButton("�¼�ǥ ��� �ٿ�");
		bt_seat=new JButton("�¼�ǥ �ϰ� ���");
		
		// ��ȭ�� ��� �гο� scroll ���̱�
		//scroll=new JScrollPane(p_list);
		showTheaterList();
		
		p_load.add(bt_form);
		p_load.add(bt_seat);
		
		p_north.setLayout(new BorderLayout());
		p_north.add(lb_title, BorderLayout.WEST);
		p_north.add(p_load);
		p_north.add(bt_add, BorderLayout.EAST);
		
		bt_add.addActionListener(this);
		bt_form.addActionListener(this);
		bt_seat.addActionListener(this);
		
		//p_warning.add(lb_warning);
		//p_warning.setBackground(Color.red);
		
		p_content.setBackground(Color.white);
		p_content.setLayout(new BorderLayout());
		p_content.add(p_list);
		//p_content.add(p_warning);
		
		setLayout(new BorderLayout());
		add(p_north, BorderLayout.NORTH);
		add(p_content);
		
		//addFrame();

	}
	
	// DB ����
	public void connect(){
		manager=DBManager.getInstance();
		con=manager.getConnect();
	}
	
	// ���� �����ϴ� ��ȭ���� DB���� ��������
	public void getTheaterList(){
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		theaterList.removeAll(theaterList);
		
		String sql="select * from theater order by theater_id asc";
		
		try {
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
	
			while(rs.next()){			
				Theater dto=new Theater();
				dto.setTheater_id(rs.getInt("theater_id"));
				dto.setBranch_id(rs.getInt("branch_id"));
				dto.setName(rs.getString("name"));
				dto.setCount(rs.getInt("count"));
				
				theaterList.add(dto);
			}
			
			// ���� �����ϴ� ��ȭ ���� ���ϱ�
			sql="select count(*) from theater";
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			rs.next();
			count=rs.getInt("count(*)");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// ��ȭ�� �г� ����
	public void setTheaterList(){
		theaters.removeAll(theaters);
		p_list.removeAll();
		
		// ���� �����ϴ� ��ȭ����ŭ �����ϰ� ���� �� ���
		for(int i=0; i<theaterList.size(); i++){
			String name=theaterList.get(i).getName();
			int count=theaterList.get(i).getCount();

			theaterItem=new TheaterItem(name, count);
			theaterItem.theater_id=theaterList.get(i).getTheater_id();
					
			p_list.add(theaterItem);
			
			theaters.add(theaterItem);
		}
		
		for(int j=0; j<theaters.size(); j++){
			theaters.get(j).addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					Object obj=e.getSource();
					
					for(int k=0; k<theaters.size(); k++){
						if(obj==theaters.get(k)){
							System.out.println(theaters.get(k).theater_id+"�� ����");
							//id=theaters.get(k).theater_id;
							editTheater=new EditTheater(TheaterMain.this, k);
							System.out.println(k);
							return;
						}
					}
				}
			});
		}
		
		System.out.println("��ȭ�� ����");
	}
	
	// ��ȭ�� ��� ���
	public void showTheaterList(){
		if(theaterList.size()==0){
			p_warning.setVisible(true);
			p_list.setVisible(false);
		}
		else{
			setTheaterList();
			p_warning.setVisible(false);
			p_list.setVisible(true);
			System.out.println("��ȭ�� ���");
		}
	}
	
	// ��ȭ�� �¼�ǥ ��� ���� �� �ٿ�(�ϴ��� ���常 ������ ����, ���� ����Ǹ� ������ �ø� ����)
	public void saveExcelForm(){
		System.out.println("�¼�ǥ ���");
		if(theaterList.size()==0){
			JOptionPane.showMessageDialog(this, "���� ��ȭ���� �߰����� �ʾҽ��ϴ�.");
			return;
		}
		HSSFRow row;
		HSSFCell cell = null;
		
		HSSFWorkbook workbook=new HSSFWorkbook();
		
		// sheet�� ����
		// ��ȭ������ ���ο� sheet ����
		HSSFSheet[] sheet=new HSSFSheet[theaterList.size()];
		for(int i=0; i<sheet.length; i++){
			sheet[i]=workbook.createSheet(theaterList.get(i).getName()+"��");		
			
			// 10*10, ������ ���� �� �¼��� ǥ��
			for(int j=0; j<10; j++){
				// ��� row ����
				row=sheet[i].createRow(j);
				for(int k=0; k<10; k++){
					row.createCell(k).setCellValue("O");
				}
			}
			row=sheet[i].createRow(10);
			row.createCell(0).setCellValue("�� �¼���");
			row.createCell(1).setCellValue(theaterList.get(i).getCount());
			
			// cell ��� ����
			/*
			CellStyle center=workbook.createCellStyle();
			center.setAlignment(CellStyle.ALIGN_CENTER);
			cell.setCellStyle(center);
			*/
			
		}
		FileOutputStream fos=null;
		try {
			fos=new FileOutputStream("res_manager/��ȭ�� �¼�ǥ.xls");
			workbook.write(fos);
			JOptionPane.showMessageDialog(this, "�¼�ǥ ��� �ٿ� �Ϸ�");
			
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
		}
	}
	
	// ���� �����ϸ� �ø� ����
	// ��ȭ�� �¼�ǥ ���
	public void loadSeatExcel(){
		
	}

	// ��ȭ�� �߰�, ��ȭ�� �¼�ǥ ��� ��ư
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		
		// ��ȭ�� �߰�
		if(obj==bt_add){
			if(count>=10){
				JOptionPane.showMessageDialog(this, "��ȭ���� �ִ� ������ 10���Դϴ�. �� �̻� �߰��� �� �����ϴ�.");
			}
			else{
				addTheater=new AddTheater(this);
				System.out.println("��ȭ�� �߰�");
			}
		}
		
		// ��ȭ�� �¼�ǥ ��� �ٿ�
		else if(obj==bt_form){
			saveExcelForm();
		}
		// ��ȭ�� �¼�ǥ ���
		else if(obj==bt_seat){
			loadSeatExcel();
		}
	}
}
