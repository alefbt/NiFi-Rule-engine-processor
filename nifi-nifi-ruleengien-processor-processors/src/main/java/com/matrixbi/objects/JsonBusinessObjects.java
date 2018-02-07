package com.matrixbi.objects;

import java.io.InputStreamReader;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Business object
 * 
 * @author Yehuda Korotkin
 *
 */
public class JsonBusinessObjects implements Iterator<BusinessObjectJson> {
	private JsonElement element;

	private Boolean change = false;
	private Gson gson;

	public JsonBusinessObjects(InputStreamReader inputStreamReader) throws Exception {
		this.gson = new Gson();

		element = gson.fromJson(inputStreamReader, JsonElement.class);

		if (element == null || !(element.isJsonObject() || element.isJsonArray()))
			throw new Exception("Cannot parse json from inputStreamReader  ");

		init();

	}

	public boolean hasChanged() {
		return this.change;
	}

	public JsonBusinessObjects(String json) throws Exception {
		this.gson = new Gson();

		element = gson.fromJson(json, JsonElement.class);

		if (element == null || !(element.isJsonObject() || element.isJsonArray()))
			throw new Exception("Cannot parse json :  " + json);

		init();
	}

	public String getJson() {
		return gson.toJson(this.element);
	}

	private void init() {
		idx = 0;

		if (element.isJsonArray())
			size = element.getAsJsonArray().size();
		else
			size = 1;
	}

	int idx = 0;
	int size = 0;

	@Override
	public boolean hasNext() {
		return size > idx;
	}

	@Override
	public BusinessObjectJson next() {
		try {
			if (element.isJsonArray())
				return new BusinessObjectJson(this, element.getAsJsonArray().get(idx));
			else
				return new BusinessObjectJson(this, element.getAsJsonObject());
		} finally {
			idx++;
		}
	}

	public void trigger_change() {
		this.change = true;

	}

}
