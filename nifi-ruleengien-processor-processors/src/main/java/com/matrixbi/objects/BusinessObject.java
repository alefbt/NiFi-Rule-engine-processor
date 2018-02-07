package com.matrixbi.objects;

import java.io.Serializable;

public abstract class BusinessObject implements Serializable {
	private static final long serialVersionUID = 5375497145393181727L;
	abstract public Boolean isChanged();
	abstract public String get(String path);
	abstract public float getAsFloat(String path);
	abstract public int getAsInt(String path) ;
	abstract public boolean getAsBoolean(String path);
	abstract public void set(String path, String value) ;
	
	abstract public void set(String path, Boolean value) ;
	abstract public void set(String path, Character value) ;
	abstract public void set(String path, Number value);
}
