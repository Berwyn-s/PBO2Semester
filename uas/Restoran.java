package uas;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.*;

public class Restoran extends JFrame {

	private static final long serialVersionUID = 1L;
	MainClass sqlClass;
	JButton updateBtn, deleteBtn, createBtn, reportBtn, logoutBtn;
	JTable tabletex;
	JScrollPane scrolltex;
	String user;
	String data[][] = new String[100][100];
	
	Restoran(String username){
		try {
			sqlClass = new MainClass();
			sqlClass.GetData(username, data);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		user = username;
		Container pane = getContentPane();
		
		updateBtn = new JButton("Update");
		updateBtn.setSize(150, 20);
		updateBtn.setLocation(10, 10);
		updateHandler updtHndlr = new updateHandler();
		updateBtn.addActionListener(updtHndlr);
		
		deleteBtn = new JButton("Delete");
		deleteBtn.setSize(150, 20);
		deleteBtn.setLocation(170, 10);
		deleteHandler delHndlr = new deleteHandler();
		deleteBtn.addActionListener(delHndlr);
		
		createBtn = new JButton("Create");
		createBtn.setSize(150, 20);
		createBtn.setLocation(330, 10);
		insertHandler insrtHndlr = new insertHandler();
		createBtn.addActionListener(insrtHndlr);
		
		reportBtn = new JButton("Report");
		reportBtn.setSize(150, 20);
		reportBtn.setLocation(490, 10);
		reportHandler rprtHndlr = new reportHandler();
		reportBtn.addActionListener(rprtHndlr);
		
		logoutBtn = new JButton("Log out");
		logoutBtn.setSize(150, 20);
		logoutBtn.setLocation(650, 10);
		logoutHandler outHndlr = new logoutHandler();
		logoutBtn.addActionListener(outHndlr);
		
		String column[]={"Jumlah","Nama Makanan/Minuman"};   
		
		tabletex = new JTable(data,column);
		tabletex.setLocation(10, 40);
		tabletex.setSize(800, 460);
		tabletex.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		scrolltex = new JScrollPane(tabletex);
		scrolltex.setLocation(10, 40);
		scrolltex.setSize(800, 460);
		
		pane.add(updateBtn);
		pane.add(deleteBtn);
		pane.add(createBtn);
		pane.add(reportBtn);
		pane.add(logoutBtn);
		pane.add(scrolltex);
		
		pane.setLayout(null);
		setTitle("List pesanan");
	    setSize(820,500);
	    setMinimumSize(new Dimension(820,500));
	    setVisible(true);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	void RefreshTable(String username) {
	
			try {
				sqlClass.GetData(username, data);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			scrolltex.repaint();
		
	}
	
	class insertHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
				while(true)
				try {
				if (sqlClass.Insert( user,(int) Double.parseDouble( JOptionPane.showInputDialog("jumlah") ), JOptionPane.showInputDialog("Nama makanan/minuman")) )
					JOptionPane.showMessageDialog(null, "Makanan/Minuman berhasil di tambahkan");
					break;
				}catch(Exception E) {
					System.out.println(E);
					JOptionPane.showMessageDialog(null, "Harap masukan angka pada kolom jumlah");
				}
			RefreshTable(user);
		}
	}
	
	class logoutHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			setVisible(false);
			new LoginClass();
		}
	}
	
	class deleteHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				boolean exist = sqlClass.Delete(user, JOptionPane.showInputDialog("Nama makanan/minuman"));
				if (exist)
					JOptionPane.showMessageDialog(null, "Pesanan berhasil di hapus");
				else
					JOptionPane.showMessageDialog(null, "Makanan/Minuman tidak ditemukan");	
			}catch(SQLException e1) {
				System.out.println(e1);
			}
			RefreshTable(user);
		}
	}
	
	class updateHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			while(true)
			try {
				boolean exist = sqlClass.Update(user,(int) Double.parseDouble( JOptionPane.showInputDialog("jumlah") ), JOptionPane.showInputDialog("Nama makanan/minuman"));
				if (exist)
					JOptionPane.showMessageDialog(null, "Makanan/Minuman berhasil di update");
				else
					JOptionPane.showMessageDialog(null, "Makanan/Minuman tidak ditemukan");	
				break;
			} catch (NumberFormatException | HeadlessException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, "Harap masukan angka pada kolom jumlah");
			}
			RefreshTable(user);
		}
	}
	
	class reportHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			sqlClass.Report();
		}
	}
	
}
