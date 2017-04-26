package com.manage.discount;

import java.awt.Checkbox;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

public class table_modelF extends AbstractTableModel {
	Vector<String> columnName = new Vector<String>();
	Vector<Vector> data = new Vector<Vector>();
	Connection con;
	String name = null;
	String table_name = "null";
	String column = "null";
	public table_modelF(Connection con, String id) {
		this.con = con;
		this.name = id;
		getList();
	}

	// 목록 가져오기
	public void setName(){
		if(name.equalsIgnoreCase("포인트")){
			table_name="point";
			column="point";
		}
		else if(name.equals("상품권")){
			table_name="gift";
			column="gift";
		}
		else if(name.equals("카드사")){
			table_name="card";
			column="card";
			System.out.println("카드값 받았소");
		}
	}
	public void getList(){
		setName();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sql=new StringBuffer();
		sql.append("select * from "+table_name);
		try {
			pstmt=con.prepareStatement(sql.toString());
			rs=pstmt.executeQuery();

			//벡터들을 초기화
			columnName.removeAll(columnName);
			data.removeAll(data);
			
			//컬럼명 추출
			ResultSetMetaData meta=rs.getMetaData();
			for(int i=1;i<=meta.getColumnCount();i++){
				columnName.add(meta.getColumnName(i));
			}
			while(rs.next()){
				Vector vec=new Vector();
				for(int i=0;i<columnName.size();i++){
					vec.add(rs.getString(columnName.elementAt(i)));
				}
				data.add(vec);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
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
	@Override
	public int getColumnCount() {
		return columnName.size();
	}

	@Override
	public String getColumnName(int col) {

		return columnName.get(col);
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		return data.get(row).get(col);
	}

	public boolean isCellEditable(int row, int col) {
		boolean flag = false;
		if (col == 0) {
			flag = false;
		} else if (col == columnName.size() - 1) {
			flag = false;
		} else {
			flag = true;
		}
		return flag;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		// 층,호수를 변경한다.
		Vector vec = data.get(row);// row한 레코드를 반환!
		vec.setElementAt(value, col);
		int id = Integer.parseInt(vec.elementAt(0).toString());
		new Table_UpdateF(con, column, row + 1, columnName.elementAt(col), value, id, "point_serial");
		this.fireTableCellUpdated(row + 1, col);
	}
}
