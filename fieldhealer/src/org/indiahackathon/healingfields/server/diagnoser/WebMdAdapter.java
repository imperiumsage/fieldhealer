import java.io.*;
import java.lang.*;
import java.net.*;
import java.util.ArrayList;
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
		/**
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
		*/
		
		String outputJson = "{  \"code\": 1,  \"data\": {    \"symptoms\": [      {        \"nm\": \"Cough\",        \"id\": 59,        \"rk\": 6,        \"txt\": \"\",        \"qclss\": [          {            \"ty\": \"Check\",            \"nm\": \"Detail\",            \"id\": 24,            \"q\": \"Cough:\",            \"mcl\": \"\",            \"mloc\": \"\",            \"quals\": [              {                \"id\": 323,                \"vl\": \"coughing up blood or blood in sputum\",                \"mcl\": \"\",                \"mloc\": \"\",                \"txt\": \"\",                \"ovrids\": null              },              {                \"id\": 195,                \"vl\": \"no sputum (non-productive)\",                \"mcl\": \"\",                \"mloc\": \"\",                \"txt\": \"\",                \"ovrids\": null              },              {                \"id\": 197,                \"vl\": \"yellow or green sputum\",                \"mcl\": \"\",                \"mloc\": \"\",                \"txt\": \"\",                \"ovrids\": null              }            ]          },          {            \"ty\": \"Check\",            \"nm\": \"Worse\",            \"id\": 15,            \"q\": \"Cough brought on\",            \"mcl\": \"\",            \"mloc\": \"\",            \"quals\": [              {                \"id\": 211,                \"vl\": \"or worse at night\",                \"mcl\": \"\",                \"mloc\": \"\",                \"txt\": \"\",                \"ovrids\": null              }            ]          },          {            \"ty\": \"Check\",            \"nm\": \"Exposure\",            \"id\": 19,            \"q\": \"Cough developed after exposure to\",            \"mcl\": \"\",            \"mloc\": \"\",            \"quals\": [              {                \"id\": 53,                \"vl\": \"someone with a possible infectious illness\",                \"mcl\": \"\",                \"mloc\": \"\",                \"txt\": \"\",                \"ovrids\": null              }            ]          }        ],        \"locs\": [          {            \"id\": 7,            \"nm\": \"Mouth\"          }        ],        \"bps\": null      },      {        \"nm\": \"Cough\",        \"id\": 59,        \"rk\": 2,        \"txt\": \"\",        \"qclss\": [          {            \"ty\": \"Check\",            \"nm\": \"Detail\",            \"id\": 24,            \"q\": \"Cough:\",            \"mcl\": \"\",            \"mloc\": \"\",            \"quals\": [              {                \"id\": 394,                \"vl\": \"hacking\",                \"mcl\": \"\",                \"mloc\": \"\",                \"txt\": \"\",                \"ovrids\": null              },              {                \"id\": 685,                \"vl\": \"whooping noise when inhaling\",                \"mcl\": \"Audio\",                \"mloc\": \"ad-0283-hz-001.mp3\",                \"txt\": \"\",                \"ovrids\": null              },              {                \"id\": 221,                \"vl\": \"high pitched or barking\",                \"mcl\": \"\",                \"mloc\": \"\",                \"txt\": \"If you have a high pitched or barking cough please seek prompt medical attention.\",                \"ovrids\": null              },              {                \"id\": 196,                \"vl\": \"white or pink sputum\",                \"mcl\": \"\",                \"mloc\": \"\",                \"txt\": \"\",                \"ovrids\": null              },              {                \"id\": 197,                \"vl\": \"yellow or green sputum\",                \"mcl\": \"\",                \"mloc\": \"\",                \"txt\": \"\",                \"ovrids\": null              },              {                \"id\": 195,                \"vl\": \"no sputum (non-productive)\",                \"mcl\": \"\",                \"mloc\": \"\",                \"txt\": \"\",                \"ovrids\": null              },              {                \"id\": 323,                \"vl\": \"coughing up blood or blood in sputum\",                \"mcl\": \"\",                \"mloc\": \"\",                \"txt\": \"If you are coughing up blood or blood in sputum please seek prompt medical attention.\",                \"ovrids\": null              }            ]          },          {            \"ty\": \"Check\",            \"nm\": \"Worse\",            \"id\": 15,            \"q\": \"Cough brought on\",            \"mcl\": \"\",            \"mloc\": \"\",            \"quals\": [              {                \"id\": 57,                \"vl\": \"or made worse by secondhand smoke\",                \"mcl\": \"\",                \"mloc\": \"\",                \"txt\": \"\",                \"ovrids\": null              },              {                \"id\": 444,                \"vl\": \"or made worse by lying down\",                \"mcl\": \"\",                \"mloc\": \"\",                \"txt\": \"\",                \"ovrids\": null              },              {                \"id\": 211,                \"vl\": \"or worse at night\",                \"mcl\": \"\",                \"mloc\": \"\",                \"txt\": \"\",                \"ovrids\": null              }            ]          },          {            \"ty\": \"Check\",            \"nm\": \"Exposure\",            \"id\": 19,            \"q\": \"Cough developed after exposure to\",            \"mcl\": \"\",            \"mloc\": \"\",            \"quals\": [              {                \"id\": 53,                \"vl\": \"someone with a possible infectious illness\",                \"mcl\": \"\",                \"mloc\": \"\",                \"txt\": \"\",                \"ovrids\": null              }            ]          },          {            \"ty\": \"Check\",            \"nm\": \"Associated\",            \"id\": 17,            \"q\": \"Cough associated with …\",            \"mcl\": \"\",            \"mloc\": \"\",            \"quals\": [              {                \"id\": 1476,                \"vl\": \"eating undercooked crabs/crayfish\",                \"mcl\": \"\",                \"mloc\": \"\",                \"txt\": \"\",                \"ovrids\": null              },              {                \"id\": 411,                \"vl\": \"history of exposure to asbestos\",                \"mcl\": \"\",                \"mloc\": \"\",                \"txt\": \"\",                \"ovrids\": null              }            ]          }        ],        \"locs\": [          {            \"id\": 10,            \"nm\": \"Neck (front\"          }        ],        \"bps\": null      },      {        \"nm\": \"Cough\",        \"id\": 59,        \"rk\": 2,        \"txt\": \"\",        \"qclss\": [          {            \"ty\": \"Check\",            \"nm\": \"Detail\",            \"id\": 24,            \"q\": \"Cough:\",            \"mcl\": \"\",            \"mloc\": \"\",            \"quals\": [              {                \"id\": 685,                \"vl\": \"whooping noise when inhaling\",                \"mcl\": \"Audio\",                \"mloc\": \"ad-0283-hz-001.mp3\",                \"txt\": \"If you have a cough associated with a whooping noise when you inhale please seek emergency medical attention.\",                \"ovrids\": null              },              {                \"id\": 394,                \"vl\": \"hacking\",                \"mcl\": \"\",                \"mloc\": \"\",                \"txt\": \"\",                \"ovrids\": null              },              {                \"id\": 197,                \"vl\": \"yellow or green sputum\",                \"mcl\": \"\",                \"mloc\": \"\",                \"txt\": \"\",                \"ovrids\": null              },              {                \"id\": 196,                \"vl\": \"white or pink sputum\",                \"mcl\": \"\",                \"mloc\": \"\",                \"txt\": \"\",                \"ovrids\": null              },              {                \"id\": 195,                \"vl\": \"no sputum (non-productive)\",                \"mcl\": \"\",                \"mloc\": \"\",                \"txt\": \"\",                \"ovrids\": null              },              {                \"id\": 221,                \"vl\": \"high pitched or barking\",                \"mcl\": \"\",                \"mloc\": \"\",                \"txt\": \"If you have a high pitched or barking cough please seek prompt medical attention.\",                \"ovrids\": null              },              {                \"id\": 323,                \"vl\": \"coughing up blood or blood in sputum\",                \"mcl\": \"\",                \"mloc\": \"\",                \"txt\": \"If you are coughing up blood please seek prompt medical attention.\",                \"ovrids\": null              }            ]          },          {            \"ty\": \"Check\",            \"nm\": \"Worse\",            \"id\": 15,            \"q\": \"Cough brought on\",            \"mcl\": \"\",            \"mloc\": \"\",            \"quals\": [              {                \"id\": 211,                \"vl\": \"or worse at night\",                \"mcl\": \"\",                \"mloc\": \"\",                \"txt\": \"\",                \"ovrids\": null              },              {                \"id\": 57,                \"vl\": \"or made worse by secondhand smoke\",                \"mcure to\",            \"mcl\": \"\",            \"mloc\": \"\",            \"quals\": [              {                \"id\": 53,                \"vl\": \"someone with a possible infectious illness\",                \"mcl\": \"\",ocs\": [          {            \"id\": 14,            \"nm\": \"Chest\"          }        ],        \"bps\": null      }    ]  },  \"status\": \"ok\"}";

		JSONParser parser = new JSONParser();
		System.out.println(outputJson);
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
			arr = (JSONArray)obj.get("locs");
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

    public static ArrayList<String> GetWebMdDiseasesForSymptoms(ArrayList<BodyPartIdAndSymptomIdInfo> bodyPartIdAndSymptomIdInfo) {
	ArrayList<String> retval = new ArrayList<String>();

	return retval;
    }
    
    public static void main(String[] args) {
	ArrayList<String> listOfSymptoms = new ArrayList<String>();
	listOfSymptoms.add("cough");
	ArrayList<BodyPartIdAndSymptomIdInfo> bodyPartIdAndSymptomIdInfo = GetWebMdIdsForSymptoms(24, "M", listOfSymptoms);
	for (BodyPartIdAndSymptomIdInfo value : bodyPartIdAndSymptomIdInfo) {
	    System.out.println(value.getBodyPartId() + " " + value.getSymptomId());
	}
	GetWebMdDiseasesForSymptoms(bodyPartIdAndSymptomIdInfo);
    }
};