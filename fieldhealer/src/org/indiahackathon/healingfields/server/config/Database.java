package org.indiahackathon.healingfields.server.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.GoogleDriver");
		String url = "jdbc:google:mysql://fieldhealer:fieldhealerdata/fieldhealerdata?user=root";
		return DriverManager.getConnection(url);
	}
	
	public static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch(SQLException e) {
				// ignore error
			}
		}
	}
	
	public static void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch(SQLException e) {
				// ignore error
			}
		}
	}
	
	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch(SQLException e) {
				// ignore error
			}
		}
	}

}
