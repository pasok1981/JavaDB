package org.test.windows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.table.DefaultTableModel;

public class SQLiteConn {

	private static final String PATH = "jdbc:sqlite:C:\\Users\\Michael Scott\\eclipse-workspace\\TestWindows\\DB_Users\\users.db";
	
	private Connection connect() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(PATH);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

	public boolean findUser(String username, String pass) {
		String query = "SELECT id FROM ActiveUsers WHERE Username=? AND Password=?";
		
		try (Connection conn = this.connect();
			 PreparedStatement stmt = conn.prepareStatement(query)) {
				 stmt.setString(1, username);
				 stmt.setString(2, pass);
			 
				 ResultSet rs = stmt.executeQuery();
				 
				 if (!rs.isBeforeFirst()) {
					 return false;
				 }
				 
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return true;
	}
	
	public int countRows() {
		String sql = "SELECT * FROM ActiveUsers";
		int count = 0;
		
		try (Connection con = this.connect();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
					
			while(rs.next()) 
				count++;
			
		} catch (SQLException e){
			e.printStackTrace();
		}
		
		return count;
	}
	
	public void fillTable() {
		String sql = "SELECT * FROM ActiveUsers";
		DefaultTableModel dtm = ShowUsers.getTable_model();
		
		try (Connection c = this.connect();
				Statement s = c.createStatement();
				ResultSet rs = s.executeQuery(sql)){
			
			while (rs.next()) {
				dtm.addRow(new Object[] {
					rs.getInt("ID"), rs.getString("Username"), rs.getString("Password")});
			}
			
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
}

