package com.matrixbi.nifi.processor.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

import com.matrixbi.objects.BusinessObjectJson;
import com.matrixbi.objects.JsonBusinessObjects;


public class BusinessObjectJsonTest {

	private static String JSON_TEST_1 = "{\"name\":\"MatrixBI rocks\"}";
	
	@Before
	public void setup() {
		
	}
	
	@Test
	public void test_stream_input() throws Exception {
        InputStream content = new ByteArrayInputStream(JSON_TEST_1.getBytes());
        InputStreamReader isr = new InputStreamReader(content);
        JsonBusinessObjects o = new JsonBusinessObjects(isr);
        assertTrue(o.hasNext());
		BusinessObjectJson v = o.next();
		
		assertNotNull(v);
	}
	
	@Test
	public void test_string_input() throws Exception {
		
        JsonBusinessObjects o = new JsonBusinessObjects(JSON_TEST_1);
        assertTrue(o.hasNext());
		BusinessObjectJson v = o.next();

		assertNotNull(v);
		assertEquals(v.getJson(), JSON_TEST_1);
	}
	
	@Test
	public void test_string_input_getExect() throws Exception {
        JsonBusinessObjects o = new JsonBusinessObjects(JSON_TEST_1);
        assertTrue(o.hasNext());
		BusinessObjectJson v = o.next();
		assertNotNull(v);
		assertEquals(v.get("name"), "MatrixBI rocks");
	}
	
	@Test
	public void test_string_complex_get() throws Exception {
		String json_str = "{\"name\":\"MatrixBI rocks\", \"obj1\":{\"obj11\":\"A\", \"obj12\":\"B\"}}";
        JsonBusinessObjects o = new JsonBusinessObjects(json_str);
        assertTrue(o.hasNext());
		BusinessObjectJson v = o.next();

		assertNotNull(v);
		assertEquals(v.get("obj1.obj12"), "B");
		
	}
	
	@Test
	public void test_string_complex_update() throws Exception {
		String json_str = "{\"name\":\"MatrixBI rocks\", \"obj1\":{\"obj11\":\"A\", \"obj12\":\"B\"}}";

        JsonBusinessObjects o = new JsonBusinessObjects(json_str);
        assertTrue(o.hasNext());
		BusinessObjectJson v = o.next();
		
		assertNotNull(v);
		assertEquals(v.get("obj1.obj12"), "B");
		
		v.set("obj1.obj12", "Z");
		assertEquals(v.get("obj1.obj12"), "Z");
		
	}
}
