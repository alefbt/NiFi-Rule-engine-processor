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
package com.matrixbi.nifi.processor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.nifi.annotation.behavior.SideEffectFree;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.annotation.lifecycle.OnScheduled;
import org.apache.nifi.annotation.lifecycle.OnStopped;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.logging.ComponentLog;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.ProcessorInitializationContext;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.io.InputStreamCallback;
import org.apache.nifi.processor.io.OutputStreamCallback;
import org.apache.nifi.processor.util.StandardValidators;

import com.matrixbi.objects.JsonBusinessObjects;
import com.matrixbi.utils.RuleEngine;


@SideEffectFree
@Tags({"Rule Engine","Processor","Drools","drl","MatrixBI"})
@CapabilityDescription("Rule engine for nifi")

public class RuleEngineProcessor extends AbstractProcessor {


   public static final PropertyDescriptor CHARACTER_SET = new PropertyDescriptor.Builder()
        .name("Character Set")
        .description("The Character Set in which the file is encoded")
        .required(true)
        .addValidator(StandardValidators.CHARACTER_SET_VALIDATOR)
        .defaultValue("UTF-8")
        .build();
   
   
    public static final PropertyDescriptor DRL_PATH = new PropertyDescriptor
        .Builder().name("DRL file path")
        .displayName("DRL file path")
        .description("File ends with .drl or .xls that contines drools rules")
        .required(true)
        .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
        .addValidator(StandardValidators.FILE_EXISTS_VALIDATOR)
        .build();


    public static final Relationship SUCCESS = new Relationship.Builder()
        .name("success")
        .description("Success relationship")
        .build();

    public static final Relationship FAILD = new Relationship.Builder()
        .name("failed")
        .description("Failed relationship")
        .build();

    
    private List<PropertyDescriptor> descriptors;

    private Set<Relationship> relationships;

    private final BlockingQueue<byte[]> bufferQueue = new LinkedBlockingQueue<>();
    
    private static HashMap<String,RuleEngine> ruleEngineServices = new HashMap<>();
    
    		//RuleEngine.createSession(file.getAbsolutePath());
    private ComponentLog log;
    
    
    private static RuleEngine getRuleEngineService(String filepath) {
    	if(!ruleEngineServices.containsKey(filepath))
    		ruleEngineServices.put(filepath, RuleEngine.createSession(filepath));
    	
    	return ruleEngineServices.get(filepath);
    }
    
    @Override
    protected void init(final ProcessorInitializationContext context) {
    	log = getLogger();
    	log.debug("Init MatrixBI's RuleEngineProcesor");

    	final List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
	        descriptors.add(DRL_PATH);
	        descriptors.add(CHARACTER_SET);
        this.descriptors = Collections.unmodifiableList(descriptors);

        final Set<Relationship> relationships = new HashSet<Relationship>();
	        relationships.add(SUCCESS);
	        relationships.add(FAILD);
        this.relationships = Collections.unmodifiableSet(relationships);
    }

    @Override
    public Set<Relationship> getRelationships() {
        return this.relationships;
    }

    @Override
    public final List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        return descriptors;
    }

    @OnScheduled
    public void onScheduled(final ProcessContext context) {
    	log.debug("onscheduled");
    }

    @Override
    public void onTrigger(final ProcessContext context, final ProcessSession session) throws ProcessException {
        FlowFile flowFile = session.get();
        
        
        if ( flowFile == null ) {
            return;
        }
       
              
        final AtomicReference<JsonBusinessObjects> value = new AtomicReference<>();
        
        session.read(flowFile, new InputStreamCallback() {
            @Override
            public void process(InputStream flowfileInputStream) throws IOException {
                try{
                    InputStreamReader flowfileInputStreamReader = new InputStreamReader(flowfileInputStream);
                    JsonBusinessObjects jsonBusinessObjects = new JsonBusinessObjects(flowfileInputStreamReader);
                    
                    String drl_path = context.getProperty(DRL_PATH).getValue();
                    while(jsonBusinessObjects.hasNext()) {
                    	getRuleEngineService(drl_path).execute(jsonBusinessObjects.next());
                    }
                    
                    value.set(jsonBusinessObjects);
                }catch(Exception ex){
                    ex.printStackTrace();
                    getLogger().error("Failed to read json string.");
                }
            }
        });

        // Write the results to an attribute
        JsonBusinessObjects results = value.get();
        
        if(results==null)
        {
        	log.error("Failed to get results");
        	session.transfer(flowFile, FAILD);	
        	return;
        }

        
        // if changed
        if(results.hasChanged()) {
	        flowFile = session.write(flowFile, new OutputStreamCallback() {
	            @Override
	            public void process(OutputStream out) throws IOException {
	                out.write(value.get().getJson().getBytes());
	            }
	        });
        }
        
        session.transfer(flowFile, SUCCESS);
        
    }
    

    @OnStopped
    public void onStopped() {
        bufferQueue.clear();
    }
}
