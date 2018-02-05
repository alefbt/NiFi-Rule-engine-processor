package com.matrixbi.utils.tests;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.matrixbi.utils.RuleEngine;

public class RuleEngienTest {

	RuleEngine kieServices;

	@Before
	public void setUp() throws Exception {

		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("drl_files/person.drl").getFile());

		kieServices = RuleEngine.createSession(file.getAbsolutePath());
	}

	@Test
	public void test_person() {
		Person p = new Person();
		p.setName("Yehuda");
		p.setTime(11);
		kieServices.execute(p);
		assertEquals(p.getGreet(),"Good Morning Yehuda");
		
		p.setTime(12);
		kieServices.execute(p);
		assertEquals(p.getGreet(),"Good Afternoon Yehuda");
	}

}
