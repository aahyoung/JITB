package com.manage.discountFinal;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import com.jitb.db.DBManager;

public class Plus_PointF_DoNotStart extends JFrame implements ActionListener{
	JTextField t_Point;
	JLabel la_SN,la_Point;
	JButton bt;
	Connection con;
	DBManager manager=DBManager.getInstance();
	JPanel center,north;
	PointFSNCategory dto=new PointFSNCategory();
	JTable table;
	table_modelF tablemodel;
	JScrollPane scroll;
	public Plus_PointF_DoNotStart() {
		con=manager.getConnect();
		tablemodel=new table_modelF(con, "point_serial");
		table=new JTable(tablemodel);
		scroll=new JScrollPane(table);
		
		t_Point=new JTextField(15);
		la_Point=new JLabel("Add Point Value : ");
		bt=new JButton("포인트 적립");
		table=new JTable();
		center=new JPanel();
		north=new JPanel();
		
		setLayout(new BorderLayout());
		north.add(scroll);
		center.add(la_Point);
		center.add(t_Point);
		center.add(bt);
		add(center);
		add(north,BorderLayout.NORTH);
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row=table.getSelectedRow();
				int col=table.getSelectedColumn();
				if(col==0){
					table.setValueAt(true, row, col);
					table.setModel(new table_modelF(con,"point_serial"));
				}
			}
		});
		
		bt.addActionListener(this);
		setChoice();
		
		setVisible(true);
		setSize(500,500);
	}
	public void setChoice(){
		PreparedStatement pstmt=null;
		StringBuffer sql=new StringBuffer();
		ResultSet rs=null;
		sql.append("select * from point_serial");
		System.out.println(sql);
		try {
			pstmt=con.prepareStatement(sql.toString());
			rs=pstmt.executeQuery();
			while(rs.next()){
				dto.setPoint_serial_id(rs.getInt("point_serial_id"));
				dto.setSerial_number(rs.getLong(("serial_number")));
				dto.setPoint(rs.getInt("point"));
				dto.setPoint_id(rs.getInt("point_id"));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args){
		new Plus_PointF_DoNotStart();
	}
}
