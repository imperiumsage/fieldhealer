import java.io.*;
import java.lang.*;
import java.net.*;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

class BodyPartIdAndSymptomIdInfo {
    public BodyPartIdAndSymptomIdInfo(Long bodyPartId, Long symptomId) {
	this.bodyPartId = bodyPartId;
	this.symptomId = symptomId;
    }

    public Long getBodyPartId() {
	return bodyPartId;
    }

    public Long getSymptomId() {
	return symptomId;
    }

    private Long bodyPartId;
    private Long symptomId;
};

class WebMdAdapter {
    public static ArrayList<BodyPartIdAndSymptomIdInfo> GetWebMdIdsForSymptoms(int age,
									       String gender,
									       ArrayList<String> listOfSymptoms) {
	ArrayList<BodyPartIdAndSymptomIdInfo> retval = new ArrayList<BodyPartIdAndSymptomIdInfo>();
	for (String symptom : listOfSymptoms) {
	    JSONObject usrObj = new JSONObject();
	    usrObj.put("age", age);
	    usrObj.put("gender", gender);
	    usrObj.put("zip", "");
	    usrObj.put("vid", "a80094c2-a2a9-497e-82ce-be30ca86ca06");

	    JSONObject obj = new JSONObject();
	    obj.put("user", usrObj);
	    obj.put("locale", "us");
	    obj.put("searchterm", symptom);

	    JSONObject requestObj = new JSONObject();
	    requestObj.put("request", obj);

	    String requestString = JSONValue.toJSONString(requestObj);
	    try {
		HttpURLConnection connection = (HttpURLConnection)
		    ((new URL("http://symptoms.webmd.com/scapp/SymptomCheckerAPI.svc/symptomsearch")
		      .openConnection()));
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Accept", "application/json");
		connection.setUseCaches(false);
		connection.setDoOutput(true);
		connection.connect();
		byte[] outputBytes = requestString.getBytes("UTF-8");
		OutputStream os = connection.getOutputStream();
		os.write(outputBytes);
		os.flush();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String outputJson = "";
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
		    outputJson += inputLine;
                }
		outputJson = outputJson.substring(1);

		JSONParser parser = new JSONParser();
		Object jsonAsObject = parser.parse(outputJson);
		obj = (JSONObject)jsonAsObject;
		Long bodyPartId = -1L;
		Long symptomId = -1L;
		if ((Long)obj.get("code") == 1){
		    obj = (JSONObject)obj.get("data");
		    if (obj != null) {
			JSONArray arr = (JSONArray)obj.get("symptoms");
			if (arr != null) {
			    symptomId = (Long)(((JSONObject)arr.get(0)).get("id"));
			}
			arr = (JSONArray)((JSONObject)arr.get(0)).get("locs");
			if (arr != null) {
			    bodyPartId = (Long)(((JSONObject)arr.get(0)).get("id"));
			}
			BodyPartIdAndSymptomIdInfo bodyPartIdAndSymptomIdInfo = new BodyPartIdAndSymptomIdInfo(bodyPartId, symptomId);
			retval.add(bodyPartIdAndSymptomIdInfo);
		    }
		}
	    }
	    catch (Exception e) {
		e.printStackTrace();
	    }
	}

	return retval;
    }

    public static ArrayList<String> GetWebMdDiseasesForSymptoms(int age,
								String gender,
								ArrayList<BodyPartIdAndSymptomIdInfo> bodyPartIdAndSymptomIdInfoList) {
	ArrayList<String> retval = new ArrayList<String>();
        Map<Long, ArrayList<Long> > mapBodyPartToSymptom = new HashMap<Long, ArrayList<Long> >();
	for (BodyPartIdAndSymptomIdInfo bodyPartAndSymptomIdInfo : bodyPartIdAndSymptomIdInfoList) {
		if (mapBodyPartToSymptom.containsKey(bodyPartAndSymptomIdInfo.getBodyPartId())) {
			mapBodyPartToSymptom.get(bodyPartAndSymptomIdInfo.getBodyPartId()).add(bodyPartAndSymptomIdInfo.getSymptomId());
 		} else {
			ArrayList<Long> symptomIdList = new ArrayList<Long>();
			symptomIdList.add(bodyPartAndSymptomIdInfo.getSymptomId());
			mapBodyPartToSymptom.put(bodyPartAndSymptomIdInfo.getBodyPartId(), symptomIdList);
		}
        }
	JSONObject usrObj = new JSONObject();
	usrObj.put("age", age);
	usrObj.put("gender", gender);
	usrObj.put("zip", "");
	usrObj.put("vid", "daad130c-d218-46ad-ad46-cc7fb4f114a1");
	JSONObject obj = new JSONObject();
	obj.put("user", usrObj);
	obj.put("locale", "us");
	obj.put("maxconditions", 200);

	JSONArray arr = new JSONArray();
	Iterator<Long> it = mapBodyPartToSymptom.keySet().iterator();
	while (it.hasNext()) {
		JSONObject bodyPartObj = new JSONObject();
		Long id = it.next();
		bodyPartObj.put("id", id.longValue());
		ArrayList<Long> symptomIdList = mapBodyPartToSymptom.get(id);
		JSONArray symptomArray = new JSONArray();
		for (Long symptomId : symptomIdList) {
			JSONObject symptomObj = new JSONObject();
			symptomObj.put("id", symptomId.longValue());
			symptomArray.add(symptomObj);
			JSONArray qclssArray = new JSONArray();
			JSONObject qualsObject = new JSONObject();
			qualsObject.put("quals", new JSONArray());
			qclssArray.add(qualsObject);
			qualsObject = new JSONObject();
			qualsObject.put("quals", new JSONArray());
			qclssArray.add(qualsObject);
			qualsObject = new JSONObject();
			qualsObject.put("quals", new JSONArray());
			qclssArray.add(qualsObject);
			symptomObj.put("qclss", qclssArray);
		}
		bodyPartObj.put("symptoms", symptomArray);
		arr.add(bodyPartObj);
	}
	obj.put("bodyparts", arr);

	JSONObject requestObj = new JSONObject();
	requestObj.put("request", obj);

	String requestString = JSONValue.toJSONString(requestObj);
	System.out.println(requestString);

	try {
		HttpURLConnection connection = (HttpURLConnection)
			((new URL("http://symptoms.webmd.com/scapp/SymptomCheckerAPI.svc/conditions")
			.openConnection()));
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Accept", "application/json");
		connection.setUseCaches(false);
		connection.setDoOutput(true);
		connection.connect();
		byte[] outputBytes = requestString.getBytes("UTF-8");
		OutputStream os = connection.getOutputStream();
		os.write(outputBytes);
		os.flush();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String outputJson = "";
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
		    outputJson += inputLine;
                }
		outputJson = outputJson.substring(1);
		JSONParser parser = new JSONParser();
		Object jsonAsObject = parser.parse(outputJson);
		obj = (JSONObject)jsonAsObject;
		if ((Long)obj.get("code") == 1){
		    	obj = (JSONObject)obj.get("data");
		    	if (obj != null) {
				arr = (JSONArray)obj.get("conditions");
				if (arr != null) {
					for (int i = 0; i < arr.size(); ++i) {
						JSONObject condition = (JSONObject)arr.get(i);
						retval.add((String)condition.get("name"));
					}
				}
			}
		}
	}
	catch (Exception e) {
		e.printStackTrace();
	}
	return retval;
    }
    
    public static void main(String[] args) {
	ArrayList<String> listOfSymptoms = new ArrayList<String>();
	listOfSymptoms.add("cough");
	listOfSymptoms.add("high fever");
	listOfSymptoms.add("body ache");
	ArrayList<BodyPartIdAndSymptomIdInfo> bodyPartIdAndSymptomIdInfo = GetWebMdIdsForSymptoms(24, "M", listOfSymptoms);
	for (BodyPartIdAndSymptomIdInfo value : bodyPartIdAndSymptomIdInfo) {
	    System.out.println(value.getBodyPartId() + " " + value.getSymptomId());
	}
	ArrayList<String> conditionNameList = GetWebMdDiseasesForSymptoms(24, "M", bodyPartIdAndSymptomIdInfo);
	for (String name : conditionNameList) {
		System.out.println(name);
	}
    }
};
