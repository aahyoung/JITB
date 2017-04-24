package com.manage.movie;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel{
	Vector<String> columnName;
	Vector<Vector> data;
	
	public TableModel() {
		columnName.add("������");
		columnName.add("����");
		columnName.add("����");
		columnName.add("�� �ð�");
		columnName.add("���� ����");
		columnName.add("���� ����");
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
