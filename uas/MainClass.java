package uas;
import java.sql.Connection;  //represent the connection to the database
import java.sql.DriverManager; //obtain the connection 
import java.sql.PreparedStatement; 
import java.sql.ResultSet;
import java.sql.SQLException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class MainClass {
	Connection con;
    PreparedStatement stmt;
	
	MainClass() throws SQLException{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/baito","root","");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void clearArray(int index, String data[][]) {
		while (index != 99) {
			data[index][0] = "";
			data[index++][1] = "";
		}
	}
	
	private boolean CheckExist(String username, String Makanan) throws SQLException {
		try(PreparedStatement rs = con.prepareStatement("select * from pesanan where username= ? AND MakananMinuman = ? ")){
			rs.setString(1, username);
			rs.setString(2, Makanan);
			try (ResultSet a = rs.executeQuery()) {
		        if (a.next()) 
		        	return true;
		        else
		        	return false;
			}
		}
	}
	
	boolean login(String username, String pass) throws SQLException {
		stmt = con.prepareStatement("select * from user");
	    ResultSet ber = stmt.executeQuery();
	    while(ber.next())
	    	if (ber.getString(1).equals(username)) 
	    		if (ber.getString(2).equals(pass)) 
	    			return true;
	    	
		return false;
	}
	
	boolean Register(String username, String pass) throws SQLException {
		stmt = con.prepareStatement("select * from user");
	     ResultSet ber = stmt.executeQuery();
	     while (ber.next()) 
	    	 if (ber.getString(1).equals(username)) 	 
	    		 return false;
	     
	     stmt = con.prepareStatement("insert into user value(?,?)");
	     stmt.setString(1, username);
	     stmt.setString(2, pass);
	     stmt.executeUpdate();
		return true;
	}
	
	boolean Insert(String username, int Jumlah, String Makanan) throws SQLException {
		 stmt = con.prepareStatement("insert into pesanan value(?,?,?)");
		 stmt.setString(1,username);
	     stmt.setInt(2, Jumlah);
	     stmt.setString(3, Makanan);
	     stmt.executeUpdate();
	     stmt = con.prepareStatement("select * from totalpesanan");
	     ResultSet ber = stmt.executeQuery();
	     while (ber.next()) {
	    	 if (ber.getString(2).equalsIgnoreCase(Makanan)) {
	    		 System.out.println("ber.getString(2) = " + Makanan);
	    		 System.out.println("Makanan = " + Makanan);
	    		 int a = ber.getInt(1);
	    		 stmt = con.prepareStatement("update totalpesanan set total=? where MakananMinuman=?");
	    		 stmt.setInt(1, a + Jumlah);
	    		 stmt.setString(2, Makanan);
	    		 stmt.executeUpdate();
	    		 System.out.println(Jumlah + " " + Makanan + " berhasil di tambahkan di totalpesanan");
	    		 return true;
	    	 }
	     }
	     stmt = con.prepareStatement("insert into totalpesanan value(?,?)");
	     stmt.setInt(1, Jumlah);
	     stmt.setString(2, Makanan);
	     stmt.executeUpdate();
	     return true;
	}
	
	boolean GetData(String username, String data[][]) throws SQLException{
		stmt = con.prepareStatement("select * from pesanan");
		ResultSet ber = stmt.executeQuery();
		int index = 0;
		while (ber.next()) {
			if (ber.getString(1).equals(username)) {
				data[index][0] = Integer.toString(ber.getInt(2));
				data[index][1] = ber.getString(3);
				index++;
			}
		}
		clearArray(index,data);
		return true;
	}
	
	boolean Delete(String username, String Makanan) throws SQLException{
		if (CheckExist(username,Makanan)) {
			stmt = con.prepareStatement("delete from pesanan where Username=? AND MakananMinuman=? LIMIT 1");
		    stmt.setString(1, username);
		    stmt.setString(2, Makanan);
		    stmt.executeUpdate();
		    return true;
		}else
			return false; 
	}
	
	boolean Update(String username, int Jumlah, String Makanan) throws SQLException {
		 if (CheckExist(username,Makanan)) {
			 stmt = con.prepareStatement("update pesanan set jumlah=? where Username = ?  AND MakananMinuman=? LIMIT 1");
		     stmt.setInt(1, Jumlah);
		     stmt.setString(2, username);
		     stmt.setString(3,Makanan);
		        
		     stmt.executeUpdate();
		     return true;
		 } 
	     return false;
	}
	
	void Report() {
		try {	
	    	String sql = "select * from totalpesanan";
	    	JasperDesign jdesign = JRXmlLoader.load("C:\\Users\\Komputer rumah\\Documents\\Eclipse Practice\\src\\uas\\Cherry.jrxml");
	    	      
	    	System.out.println("Check point 1");
	    	JRDesignQuery updateQuery = new JRDesignQuery();
	    	updateQuery.setText(sql);
	    	      
	    	jdesign.setQuery(updateQuery);
	    	      
	    	JasperReport Jreport = JasperCompileManager.compileReport(jdesign);
	    	JasperPrint JasperPrint = JasperFillManager.fillReport(Jreport,null,con);
	    	      
	        JasperViewer.viewReport(JasperPrint,false);
	   }catch(Exception e){
	    	 System.out.println(e);
	   }
	}
}
