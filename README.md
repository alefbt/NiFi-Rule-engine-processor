בס"ד


[![MatrixBI](docs/matrixbi-logo.png)](http://www.matrixbi.co.il/)


# NiFi-Rule-engine-processor

[![Coverage Status](https://coveralls.io/repos/github/alefbt/NiFi-Rule-engine-processor/badge.svg?branch=master)](https://coveralls.io/github/alefbt/NiFi-Rule-engine-processor?branch=master)

## Overview
١. Drools is a Business Rules Management System (BRMS) solution. It provides a core Business Rules Engine (BRE), a web authoring and rules management application (Drools Workbench) and an Eclipse IDE plugin for core development.

٢. Apache NiFi (short for NiagaraFiles) is a software project from the Apache Software Foundation designed to automate the flow of data between software systems. 

٣. When talking data flow some times need apply rules on data, and we created NiFi Rule Engine

## How to install:
1. `mvn clean install package`
2. copy nar file from `/nifi-ruleengien-processor/nifi-ruleengien-processor-nar/target/nifi-ruleengien-processor-nar-{VERSION}.nar` to your NiFi folder `/nifi-{version}/lib`
3. (Re) start your NiFi
4. Add processor search for `RuleEngineProcessor`
5. Connect Json files and Tahh-dahh.. works

## Contributers:
Matrix BI Ltd

## Licence:
Apache2
