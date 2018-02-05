package com.matrixbi.utils;

import org.kie.api.runtime.StatelessKieSession;

public class RuleEngine {
	
	public static RuleEngine createSession(String filepath) {
		return new RuleEngine(filepath);
	}
	
	private StatelessKieSession kiaSession;
	
	private RuleEngine(String filepath) {
		kiaSession = KieSessionFactory.getKieSession(filepath);
	}
	
	public void execute(Object object) {
		kiaSession.execute(object);
	}

    public void execute(Iterable objects) {
    	kiaSession.execute(objects);
    }
	
}
