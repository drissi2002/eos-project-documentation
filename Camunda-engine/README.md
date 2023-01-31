# Camunda 
 Camunda is a Java-based framework supporting BPMN for workflow

## Camunda components overview

![image](https://user-images.githubusercontent.com/84160502/215856811-c3f8dd39-e8c3-490f-8e5c-372344026e13.png)

 ### Process Engine
 
The process engine is a Java library responsible for executing BPMN 2.0 processes,It has a lightweight POJO core and uses a relational database for persistence. ORM mapping is provided by the MyBatis mapping framework.

#### Process Engine Overview

![image](https://user-images.githubusercontent.com/84160502/215856552-44cc01d8-cd19-470d-b581-c8404d0751d0.png)



 ### Modeler
 
 Camunda Modeler: Modeling tool for BPMN 2.0 diagrams

### Web Applications 
- <b>REST API :</b>  The REST API allows you to use the process engine from a remote application or a JavaScript application. (Note: The documentation of the REST API is factored out into own documents.)
- <b>Camunda Tasklist:</b> A web application for human workflow management and user tasks that allows process participants to inspect their workflow tasks and navigate to task forms in order to work on the tasks and provide data input.
- <b>Camunda Cockpit:</b> A web application for process monitoring and operations that allows you to search for process instances, inspect their state and repair broken instances.
- <b>Camunda Admin:</b> A web application that allows you to manage users, groups and authorizations.
