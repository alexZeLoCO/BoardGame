package Utils;

import org.json.simple.JSONObject;

public class JSONParser {

	public static void main (String [] args) {
		JSONObject obj = new JSONObject ();
		
		obj.put("desc", "This is a test card");
		
		System.out.println(obj);
	}
}
