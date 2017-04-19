package com.manage.discount;

	import java.sql.Connection;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.ResultSetMetaData;
	import java.sql.SQLException;
	import java.util.Vector;

	import javax.swing.event.TableModelEvent;
	import javax.swing.event.TableModelListener;
	import javax.swing.table.AbstractTableModel;

	public class table_model extends AbstractTableModel{
		Vector<String> columnName =new Vector<String>();
		Vector<Vector> data =new Vector<Vector>();
		Connection con;
		String name;
		Discount discount;
		public table_model(Connection con,String name){
			this.con=con;
			this.name=name;
			getList(name);
			
		}
		
		//목록 가져오기
		public void getList(String name){
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			StringBuffer sql=new StringBuffer();
			sql.append("select * from "+name);
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
				if(name.equals("discount_info")){
				while(rs.next()){
					Vector vec=new Vector();
					vec.add(rs.getInt("discount_info_id"));
					vec.add(rs.getInt("discount_type_id"));
					vec.add(rs.getString("name"));
					vec.add(rs.getInt("rate"));
					vec.add(rs.getString("img"));
					
					data.add(vec);
				}}
				else if(name.equals("discount_type")){
					while(rs.next()){
					Vector vec=new Vector();
					vec.add(rs.getInt("discount_type_id"));
					vec.add(rs.getString("name"));
					data.add(vec);
				}}
				else if(name.equals("gift_info")){
					while(rs.next()){
					Vector vec=new Vector();
					vec.add(rs.getInt("gift_info_id"));
					vec.add(rs.getInt("gift_type_id"));
					vec.add(rs.getInt("no"));
					vec.add(rs.getInt("price"));
					vec.add(rs.getInt("status"));
					vec.add(rs.getString("img"));
					data.add(vec);
				}}
				else if(name.equals("gift_type")){
					while(rs.next()){
						Vector vec=new Vector();
						vec.add(rs.getInt("gift_type_id"));
						vec.add(rs.getString("name"));
						data.add(vec);
					}
				}
				else{
					System.out.println("꽝");
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
			boolean flag=false;
			if(col==0){
				flag=false;
			}
			else{
				flag=true;
			}
			return flag;
		}
		@Override
		public void setValueAt(Object value, int row, int col) {
			//층,호수를 변경한다.
			Vector vec=data.get(row);//row한 레코드를 반환!
			vec.setElementAt(value, col);
			int id=Integer.parseInt(vec.elementAt(0).toString());
			new Update_Model2(con, name, row+1,col,value,id);
			this.fireTableCellUpdated(row+1, col);
		}
	}


