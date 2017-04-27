package com.manage.discount;

	import java.awt.BorderLayout;
	import java.awt.Choice;
	import java.awt.Dimension;
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
	import java.awt.event.ItemEvent;
	import java.awt.event.ItemListener;
	import java.awt.event.MouseAdapter;
	import java.awt.event.MouseEvent;
	import java.sql.Connection;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.util.Vector;

	import javax.swing.JButton;
	import javax.swing.JFrame;
	import javax.swing.JPanel;
	import javax.swing.JScrollPane;
	import javax.swing.JTable;

	import com.jitb.db.DBManager;
	import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

	public class Discount_Final_Panel extends JPanel implements ActionListener,ItemListener{
		Choice choice;
		JTable table;
		JScrollPane scroll;
		JButton bt_add,bt_del,bt_discount,bt_point,bt_payway;
		
		JPanel p_center,p_north,p_south;
		
		Connection con;
		DBManager manager=DBManager.getInstance();
		Vector<DiscountFCategory> dst=new Vector<DiscountFCategory>();
		
		JFrame[] Add=new JFrame[4];
		int index=0;
		String column=null;
		String table_name=null;
		String nameH=null;
		table_modelF tablemodel;
		
		public Discount_Final_Panel(){
			setLayout(new BorderLayout());
			setPreferredSize(new Dimension(1000, 650));
			con=manager.getConnect();
			choice=new Choice();
			table=new JTable();
			p_center=new JPanel();
			p_north=new JPanel();
			scroll=new JScrollPane(table);
			bt_add=new JButton("추가");
			bt_point=new JButton("회원 관리");
			bt_del=new JButton("삭제");
			bt_discount=new JButton("할인종류 추가");
			bt_payway=new JButton("결재방식 추가");
			
			p_north.add(choice);
			p_north.add(bt_point);
			p_north.add(bt_discount);
			p_north.add(bt_payway);
			p_north.add(bt_add);
			p_north.add(bt_del);
			p_center.add(scroll);
			

			scroll.setPreferredSize(new Dimension(1000, 650));
			add(p_north,BorderLayout.NORTH);
			add(p_center);

			bt_add.addActionListener(this);
			bt_discount.addActionListener(this);
			bt_del.addActionListener(this);
			bt_point.addActionListener(this);
			bt_payway.addActionListener(this);
			setChoice();
			init();
			choice.addItemListener(this);
			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					String id=dst.get(choice.getSelectedIndex()).getName();
					if(id.equalsIgnoreCase("포인트")){
						id="point";
					}
					else if(id.equals("상품권")){
						id="gift";
					}
					else if(id.equals("카드사")){
						id="card";
					}
				}
			});
		}
		
		public void setChoice() {
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql = "select * from discount_type order by discount_type_id asc";
			try {
				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					DiscountFCategory dto = new DiscountFCategory();
					dto.setdiscount_id((rs.getInt("discount_type_id")));
					dto.setName(rs.getString("name"));
					dst.add(dto);// 리스트에 탑재
					choice.add(dto.getName());
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				else if(pstmt!=null){
					try {
						pstmt.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			setName(dst.get(choice.getSelectedIndex()).getName());
		}
		public void setName(String name){
			if(name.equalsIgnoreCase("포인트")){
				nameH="포인트";
				table_name="point";
				column="point";
			}
			else if(name.equals("상품권")){
				nameH="상품권";
				table_name="gift";
				column="gift";
			}
			else if(name.equals("카드사")){
				nameH="카드사";
				table_name="card";
				column="card";
			}
		}
		
		public void init(){
			String id=dst.get(choice.getSelectedIndex()).getName();
			table.setModel(tablemodel=new table_modelF(con,id));
		}
		
		public static void main(String[] args){
			new Discount_Final_Panel();
		}
		
		public void setadd(int id){
			
			Add[0]=new Add_Point_Final(id,table);//테이블,discount_type_id;
			Add[0].setVisible(false);
			Add[1]=new Add_Gift_Final(id,table);
			Add[1].setVisible(false);
			Add[2]=new Add_Card_Final(id,table);
			Add[2].setVisible(false);
		}
		
		public void add(){
			int id=dst.get(index).getdiscount_id();
			setadd(id);
			Add[index].setVisible(true);
		}
		
		public void del(){
			int id=dst.get(index).getdiscount_id();
			int row=table.getSelectedRow();
			int num=Integer.parseInt((table.getValueAt(row, 0).toString()));
			setName(dst.get(index).getName());
			System.out.println(num);
			new del_Final(con,table, table_name,num, id,nameH);
			
		}
		
		public void discount(){
			new Add_Discount_Final(table);
		}
		
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Object obj=e.getSource();
			if(obj==bt_add){
				index=choice.getSelectedIndex();
				add();
			}
			else if(obj==bt_del){
				index=choice.getSelectedIndex();
				del();
			}
			else if(obj==bt_discount){
				discount();
			}

			else if(obj==bt_point){
				new Add_Point_SerialF(1);
			}
			else if(obj==bt_payway){
				new Add_payment_way(table);
			}

		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			String id=dst.get(choice.getSelectedIndex()).getName();
			table.setModel(tablemodel=new table_modelF(con,id));
		}
	}
