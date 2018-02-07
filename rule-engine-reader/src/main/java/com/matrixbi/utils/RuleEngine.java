package com.matrixbi.utils;

import java.io.Serializable;

import org.kie.api.runtime.StatelessKieSession;

public class RuleEngine implements Serializable {

	private static final long serialVersionUID = 5067156438156998327L;
	private StatelessKieSession kiaSession;

	public static RuleEngine createSession(String filepath) {
		return new RuleEngine(filepath);
	}

	private RuleEngine(String filepath) {
		kiaSession = KieSessionFactory.getNewKieSession(filepath);
	}

	public void execute(Object object) {
		kiaSession.execute(object);
	}

	public void execute(Iterable objects) {
		kiaSession.execute(objects);
	}

}
