package org.indiahackathon.healingfields.server.diagnoser;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.indiahackathon.healingfields.server.config.Database;

import com.google.common.collect.Lists;

public class DiagnoseServiceImpl implements DiagnoseService {
	
	private static final Logger log = Logger.getLogger(DiagnoseServiceImpl.class.getName());

	private static final String SELECT_DISEASES_START = "SELECT DISTINCT disease FROM diseases_with_symptoms WHERE symptom in (";
	private static final String SELECT_DISEASES_END = ")";
	@Override
	public List<String> findPotentialDiseases(String symptoms)
			throws DiagnoseServiceException {
		List<String> diseases = Lists.newArrayList();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql = SELECT_DISEASES_START + symptoms + SELECT_DISEASES_END;
		log.info("SQL: " + sql);
		try {
			conn = Database.getConnection();
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				String disease = rs.getString("disease");
				diseases.add(disease);
			}
		} catch (ClassNotFoundException | SQLException e) {
			log.log(Level.SEVERE, "Failed to connect to database", e);
		} finally {
			Database.close(rs);
			Database.close(stmt);
			Database.close(conn);
		}
		return diseases;
	}

}
