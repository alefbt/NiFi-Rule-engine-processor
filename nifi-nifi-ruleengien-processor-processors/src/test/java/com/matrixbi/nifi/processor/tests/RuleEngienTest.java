package com.matrixbi.nifi.processor.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

import com.matrixbi.objects.BusinessObjectJson;
import com.matrixbi.objects.JsonBusinessObjects;
import com.matrixbi.utils.RuleEngine;

public class RuleEngienTest {

	RuleEngine kieServices;
	TestUtils tu;

	@Before
	public void setUp() throws Exception {
		tu = new TestUtils();
		kieServices = RuleEngine.createSession(tu.getResourcePath("drl_files/person.drl"));
	}

	@Test
	public void test_person() {
		Person p = new Person();
		p.setName("Yehuda");
		p.setTime(11);
		kieServices.execute(p);
		assertEquals(p.getGreet(), "Good Morning Yehuda");

		p.setTime(12);
		kieServices.execute(p);
		assertEquals(p.getGreet(), "Good Afternoon Yehuda");
	}

	@Test
	public void test_bo() throws Exception {
		InputStream content = TestUtils.getFileIS(tu.getResourcePath("drl_files/business_object_json_test1.json"));

		InputStreamReader isr = new InputStreamReader(content);
		
		JsonBusinessObjects jbo = new JsonBusinessObjects(isr);
		assertTrue(jbo.hasNext());
		BusinessObjectJson c = jbo.next();
		
		RuleEngine re = RuleEngine.createSession(tu.getResourcePath("drl_files/business_object_json_test1.drl"));
		re.execute(c);
	}

	@Test
	public void test_bo_array() throws Exception {

		RuleEngine re = RuleEngine.createSession(tu.getResourcePath("drl_files/business_object_json_test2.drl"));

		InputStream content = TestUtils.getFileIS(tu.getResourcePath("drl_files/business_object_json_test2_array.json"));
		InputStreamReader isr = new InputStreamReader(content);
		JsonBusinessObjects jbo = new JsonBusinessObjects(isr);


		assertTrue(jbo.hasNext());
		while (jbo.hasNext()) {
			BusinessObjectJson businessObjectJson = (BusinessObjectJson) jbo.next();
			re.execute(businessObjectJson);			
		}
		
		assertTrue(jbo.hasChanged());
	}

}
