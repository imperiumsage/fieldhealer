package org.indiahackathon.healingfields.server.servlets;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

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

import com.google.common.base.Joiner;

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
		List<String> epidemics = null;
		String location = parseDataForLocation(data);
		try {
			diseases = diagnoseService.findPotentialDiseases(parseDataForSymptoms(data));
			epidemics = diagnoseService.isAnEpidemic(diseases, location);
		} catch (DiagnoseServiceException e) {
			log.log(Level.SEVERE, "Failed to get diseases", e);
		}
		resp.getWriter().write(diseases.toString());
		try {
		    URL url = new URL("http://fh-smsgateway.herokuapp.com/send?location=" + location + "&message=" +
		   	    			  getMessageForEpidemics(epidemics));
		    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		    String line;
		    while ((line = reader.readLine()) != null) {}
		    reader.close();
		} catch (MalformedURLException e) {
		    log.log(Level.SEVERE, "Failed to report epidemics", e);
		} catch (IOException e) {
		    log.log(Level.SEVERE, "Failed to report epidemics", e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	
	private String getMessageForEpidemics(List<String> epidemics) {
		String message = Joiner.on(",").join(epidemics.toArray()) + " headed your way.";
		return message;
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
	
	private String parseDataForLocation(String data) {
		JSONParser parser = new JSONParser();
		String location = null;
		try {
			JSONObject obj = (org.json.simple.JSONObject) parser.parse(data);
			location = (String) obj.get("location");
			log.info("Location: " + location);
		} catch (ParseException e) {
			log.log(Level.SEVERE, "Failed to parse JSON", e);
		}
		return location;
	}
}
