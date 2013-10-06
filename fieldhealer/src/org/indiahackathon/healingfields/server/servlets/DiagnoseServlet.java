package org.indiahackathon.healingfields.server.servlets;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.indiahackathon.healingfields.server.diagnoser.DiagnoseService;
import org.indiahackathon.healingfields.server.diagnoser.DiagnoseServiceException;
import org.indiahackathon.healingfields.server.diagnoser.DiagnoseServiceImpl;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DiagnoseServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(DiagnoseServlet.class.getName());
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String data = req.getParameter("data");
		log.info("Data: " + data);
		DiagnoseService diagnoseService = new DiagnoseServiceImpl();
		List<String> diseases = null;
		try {
			diseases = diagnoseService.findPotentialDiseases(parseDataForSymptoms(data));
		} catch (DiagnoseServiceException e) {
			log.log(Level.SEVERE, "Failed to get diseases", e);
		}
		resp.getWriter().write(jsonify(diseases).toJSONString());
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	
	private JSONArray jsonify(List<String> values) {
		JSONArray array = new JSONArray();
		for (String value : values) {
			array.add(value);
		}
		return array;
	}
	
	private String parseDataForSymptoms(String data) {
		StringBuffer symptoms = new StringBuffer();
		JSONParser parser = new JSONParser();
		try {
			JSONObject obj = (org.json.simple.JSONObject) parser.parse(data);
			JSONArray symptomsArray = (JSONArray) obj.get("symptoms");
			boolean isFirst = true;
			for (Object symptom : symptomsArray) {
				if (!isFirst) {
					symptoms.append(", ");
				}
				symptoms.append("'" + symptom + "'");
				isFirst = false;
			}
			log.info("Symptoms: " + symptoms);
		} catch (ParseException e) {
			log.log(Level.SEVERE, "Failed to parse JSON", e);
		}
		return symptoms.toString();
	}

}
