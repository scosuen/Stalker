package com.scott.stalker.utils;

public class CommonJSONBuilder {
	
	public static String getSuccResponseJSON() {
		return getResponseJSON(0, null);
	}

	/**
	 *  {"success": true, "payload": { }}
		{"success": false, "payload": { }, "error": { "code": 1, "message": "sdfsdfsd"} }
	 * @param errorCode
	 * @param errorMsg
	 * @return
	 */
	public static String getResponseJSON(int errorCode, String errorMsg) {
		StringBuffer json = new StringBuffer("{\"success\": ");

		if (errorCode > 0)
			json.append("\"false\"");
		else
			json.append("\"true\"");

		json.append(", \"payload\": { }");

		if (errorCode > 0) {
			json.append(", \"error\": { \"code\": ").append(errorCode)
					.append(", ");
			json.append("\"message\": \"").append(errorMsg).append("\"} ");
		}

		json.append("}");
		return json.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(getSuccResponseJSON());
		System.out.println(getResponseJSON(1,"sdfsdfsd"));
	}

}
