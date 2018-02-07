/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.matrixbi.nifi.processor.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.nifi.util.MockFlowFile;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.Before;
import org.junit.Test;

import com.matrixbi.nifi.processor.RuleEngineProcessor;

public class RuleEngienProcessorTest {

	private TestRunner runner;
	private TestUtils tu;

	@Before
	public void init() {
		runner = TestRunners.newTestRunner(RuleEngineProcessor.class);
		tu = new TestUtils();
	}


	@Test
	public void testProcessor() throws IOException {
		// Add properites
		runner.setProperty(RuleEngineProcessor.DRL_PATH, tu.getResourcePath("drl_files/business_object_json_test1.drl"));

		// Content to be mock a json file
		InputStream content =  TestUtils.getFileIS(tu.getResourcePath("drl_files/business_object_json_test1.json"));
		
		// Add the content to the runner
		runner.enqueue(content);

		// Run the enqueued content, it also takes an int = number of contents queued
		runner.run(1);

		// All results were processed with out failure
		runner.assertQueueEmpty();

		// If you need to read or do aditional tests on results you can access the
		// content
		List<MockFlowFile> results = runner.getFlowFilesForRelationship(RuleEngineProcessor.SUCCESS);
		assertTrue("1 match", results.size() == 1);
		MockFlowFile result = results.get(0);
		String resultValue = new String(runner.getContentAsByteArray(result));
		
		String expected = "{\"time\":11,\"greet\":\"G00d Morning Yehuda\",\"name\":\"Yehuda\"}";
		assertEquals(expected, resultValue);

		result.assertContentEquals(expected);
	}

}
