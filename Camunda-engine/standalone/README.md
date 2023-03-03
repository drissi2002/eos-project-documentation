# Standalone-camunda-process
To get started with Camunda, we used its modeler to create an example workflow, parameterized each task with appropriate variables, forms, and delegated tasks with functions and entities developed in the Spring Boot backend. The workflow was executed using the Camunda engine standalone triggered by the Spring Boot app, tracing each task (start process, claim data, complete task, move to next task, etc.), and visualizing the history of execution. 
## The implemented workflow example

![image](https://user-images.githubusercontent.com/84160502/215140030-b3b14a3e-3bac-4ec9-9427-0a9b2f171748.png)