package com.manage.movie;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel{
	Vector<String> columnName;
	Vector<Vector> data;
	
	public TableModel() {
		columnName.add("포스터");
		columnName.add("제목");
		columnName.add("감독");
		columnName.add("상영 시간");
		columnName.add("개봉 일자");
		columnName.add("종료 일자");
	}
	
	public String getColumnName(int col) {
		return columnName.get(col);
	}
	
	public int getRowCount() {
		
		return data.size();
	}

	public int getColumnCount() {
		
		return columnName.size();
	}

	public Object getValueAt(int row, int col) {
		
		return data.get(row).get(col);
	}

}
