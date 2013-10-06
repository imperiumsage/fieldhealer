package org.indiahackathon.healingfields.server.diagnoser;

import java.util.List;

public interface DiagnoseService {
	List<String> findPotentialDiseases(String symptoms) 
			throws DiagnoseServiceException;

	List<String> isAnEpidemic(List<String> diseases, String location);
}
