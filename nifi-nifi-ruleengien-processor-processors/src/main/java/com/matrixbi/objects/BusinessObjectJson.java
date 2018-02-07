package com.matrixbi.objects;

import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

/**
 * Business object
 * 
 * @author Yehuda Korotkin
 *
 */
public class BusinessObjectJson extends BusinessObject {
	private static final long serialVersionUID = -856082492778433564L;
	private JsonObject jObj;
	private Boolean change = false;
	private Gson gson;
	private JsonBusinessObjects parent;
	
	
	public BusinessObjectJson(JsonBusinessObjects jsonBusinessObjects, JsonElement je) {
		this.gson = new Gson();
		this.jObj = (JsonObject) je;
		this.parent = jsonBusinessObjects;
	}
	private void trigger_change() {
		this.parent.trigger_change();
		this.change = true;
	}
//	public BusinessObjectJson(InputStreamReader inputStreamReader) throws Exception {
//		this.gson = new Gson();
//
//		JsonElement element = gson.fromJson(inputStreamReader, JsonElement.class);
//
//		if (element == null || !element.isJsonObject())
//			throw new Exception("Cannot parse json from inputStreamReader  ");
//
//		this.jObj = (JsonObject) element;
//
//	}
//
//	public BusinessObjectJson(String json) throws Exception {
//		this.gson = new Gson();
//
//		JsonElement element = gson.fromJson(json, JsonElement.class);
//
//		if (element == null || !element.isJsonObject())
//			throw new Exception("Cannot parse json :  " + json);
//
//		this.jObj = (JsonObject) element;
//	}

	public String get(String path) {
		return getPath(path).getAsString();
	}
	public float getAsFloat(String path) {
		return getPath(path).getAsFloat();
	}
	public int getAsInt(String path) {
		return getPath(path).getAsInt();
	}

	public boolean getAsBoolean(String path) {
		return getPath(path).getAsBoolean();
	}

	public void set(String path, String value) {
		String[] s = getParentPath(path);
		JsonObject o = (JsonObject) getPath(s[0]);
		o.addProperty( s[1], value);
		trigger_change();
	}
	
	public void set(String path, Boolean value) {
		String[] s = getParentPath(path);
		JsonObject o = (JsonObject) getPath(s[0]);
		o.addProperty(s[1], value);
		trigger_change();
	}

	public void set(String path, Character value) {
		String[] s = getParentPath(path);
		JsonObject o = (JsonObject) getPath(s[0]);
		o.addProperty(s[1], value);
		trigger_change();
	}

	public void set(String path, Number value) {
		String[] s = getParentPath(path);
		JsonObject o = (JsonObject) getPath(s[0]);
		o.addProperty(s[1], value);
		trigger_change();
	}

	public String getJson() {
		return gson.toJson(this.jObj);
	}

	@Override
	public Boolean isChanged() {
		return this.change;
	}

	private String[] getParentPath(String path) {
		String[] seg = path.split("\\.");
		
		if(seg.length <= 1) {
			return new String[]{"", path};
		}
		
		String[] seg2 = Arrays.copyOfRange(seg, 0, seg.length - 1);
		String x1 = String.join(".", seg2);

		String[] seg3 = Arrays.copyOfRange(seg, seg.length - 1, seg.length);
		String x2 = String.join(".", seg3);

		return new String[]{x1, x2};
	}

	private JsonElement getPath(String path) throws JsonSyntaxException {
		if(path.isEmpty())
			return this.jObj;
		
		JsonObject obj = this.jObj;

		String[] seg = path.split("\\.");
		for (String element : seg) {			
			if (obj != null) {
				JsonElement ele = obj.get(element);
				
				if (!ele.isJsonObject())
					return ele;
				else
					obj = ele.getAsJsonObject();
			} else {
				return null;
			}
		}

		return obj;
	}
}
