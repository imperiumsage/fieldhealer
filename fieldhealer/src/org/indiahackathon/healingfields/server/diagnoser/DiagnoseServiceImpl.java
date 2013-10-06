package org.indiahackathon.healingfields.server.diagnoser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.indiahackathon.healingfields.server.config.Database;

import com.google.common.collect.Lists;

public class DiagnoseServiceImpl implements DiagnoseService {
	
	private static final Logger log = Logger.getLogger(DiagnoseServiceImpl.class.getName());

	private static final String SELECT_DISEASES = "SELECT DISTINCT disease FROM diseases_with_symptoms WHERE symptom in (?)";
	private static final String ADD_DISEASE_NOTIFICATION = "INSERT INTO diseases (location, disease) VALUES (?, ?)";
	private static final String FIND_EPIDEMICS =
			"SELECT disease, count(disease) as instances FROM diseases GROUP BY disease, location HAVING location == (?) and instances >= 3";
	@Override
	public List<String> findPotentialDiseases(String symptoms)
			throws DiagnoseServiceException {
		List<String> diseases = Lists.newArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = Database.getConnection();
			stmt = conn.prepareStatement(SELECT_DISEASES);
			stmt.setString(0, symptoms);
			rs = stmt.executeQuery();
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
	public List<String> isAnEpidemic(String diseases, String location) {
		List<String> epidemics = Lists.newArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = Database.getConnection();
			for (disease : diseases) {
				stmt = conn.prepareStatement(ADD_DISEASE_NOTIFICATION);
				stmt.setString(0, location);
				stmt.setString(1, disease);
				stmt.execute();
				Database.close(stmt);
			}
			stmt = conn.prepareStatement(FIND_EPIDEMICS);
			stmt.setString(0, location);
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
