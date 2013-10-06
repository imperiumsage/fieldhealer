package org.indiahackathon.healingfields.server.diagnoser;

import java.util.List;

import org.indiahackathon.healingfields.server.model.Disease;

public interface DiagnoseService {
	
	List<Disease> findPotentialDiseases(String... symptoms) 
			throws DiagnoseServiceException;
	
}
