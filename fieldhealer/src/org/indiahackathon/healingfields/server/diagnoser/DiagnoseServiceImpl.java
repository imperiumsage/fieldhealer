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

}
