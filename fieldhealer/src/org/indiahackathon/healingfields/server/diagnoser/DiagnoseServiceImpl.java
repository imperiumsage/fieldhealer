package org.indiahackathon.healingfields.server.diagnoser;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.indiahackathon.healingfields.server.config.Database;

import com.google.common.collect.Lists;

public class DiagnoseServiceImpl implements DiagnoseService {
	
	private static final Logger log = Logger.getLogger(DiagnoseServiceImpl.class.getName());

	private static final String ADD_DISEASE_NOTIFICATION = "INSERT INTO diseases (location, disease) VALUES (?, ?)";
	private static final String FIND_EPIDEMICS =
			"SELECT disease, count(disease) as instances FROM diseases GROUP BY disease, location HAVING location == ? and instances >= 3";
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
			stmt = conn.createStatement();
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

	@Override
	public List<String> isAnEpidemic(List<String> diseases, String location) {
		List<String> epidemics = Lists.newArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = Database.getConnection();
			for (String disease : diseases) {
				stmt = conn.prepareStatement(ADD_DISEASE_NOTIFICATION);
				stmt.setString(1, location);
				stmt.setString(2, disease);
				stmt.execute();
				Database.close(stmt);
			}
			stmt = conn.prepareStatement(FIND_EPIDEMICS);
			stmt.setString(1, location);
			rs = stmt.executeQuery();
			while (rs.next()) {
				String epidemic = rs.getString("disease");
				epidemics.add(epidemic);
			}
		} catch (ClassNotFoundException | SQLException e) {
			log.log(Level.SEVERE, "Failed to connect to database", e);
		} finally {
			Database.close(conn);
		}
		return epidemics;
	}
}
