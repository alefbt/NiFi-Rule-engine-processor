package com.matrixbi.nifi.processor.tests;


public class Person implements java.io.Serializable{
	private static final long serialVersionUID = -4134023269207200884L;

	private String name;

	private int time;

	private String greet;

	public String getGreet() {
		return greet;
	}

	public void setGreet(String greet) {
		this.greet = greet;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
}