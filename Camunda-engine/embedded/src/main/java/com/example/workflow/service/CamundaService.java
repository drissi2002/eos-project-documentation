package com.example.workflow.service;

import com.example.workflow.dto.ProcessDefinitionDto;
import com.example.workflow.dto.ServiceTaskDto;
import com.example.workflow.dto.TaskDto;
import com.example.workflow.dto.UserTaskDto;
import com.example.workflow.entity.Workflow;
import com.example.workflow.repository.WorkflowRepository;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;

import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;


import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public
class CamundaService {

    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final HistoryService historyService;


    @Autowired
    private WorkflowRepository workflowRepository;


    //deploy a process
    public void deployProcessDefinition(String processDefinitionName, String processDefinitionBpmnFile) {
        repositoryService.createDeployment()
                .name(processDefinitionName)
                .addClasspathResource(processDefinitionBpmnFile)
                .deploy();
    }

    //start a process
    public void startProcess(final String processKey){
        runtimeService.startProcessInstanceByKey(processKey);
    }

    // get processes definitions
    public List<ProcessDefinitionDto> getDeployedProcessDefinitions() {
        return repositoryService
                .createProcessDefinitionQuery()
                .list()
                .stream()
                .map(ProcessDefinitionDto::of)
                .collect(toList());
    }

    //delete a process by Id
    public void deleteProcessDefinition(String processDefinitionId) {
        try {
            // Delete the process definition and all its versions
            repositoryService.deleteProcessDefinition(processDefinitionId, true);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete process definition with id " + processDefinitionId, e);
        }
    }

    //delete all processes
    public void deleteAllProcessDefinitions() {
        repositoryService.createProcessDefinitionQuery().list().stream()
                .forEach(processDefinition -> {
                    repositoryService.deleteProcessDefinitions()
                            .byKey(processDefinition.getKey())
                            .delete();
                });
    }

    //delete all processes Instances
    public void deleteAllProcessInstances() {
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().list();
        for (ProcessInstance processInstance : processInstances) {
            runtimeService.deleteProcessInstance(processInstance.getId(), "Deleting all process instances");
        }
    }

    //process Instances Historic
    public List<HistoricProcessInstance> getCompletedProcesses() {
        return historyService.createHistoricProcessInstanceQuery().finished().list();
    }

    //delete all processes Instances Historic
    public void deleteAllHistoricProcessInstances() {
        List<HistoricProcessInstance> processInstances = historyService.createHistoricProcessInstanceQuery().list();
        List<String> processInstanceIds = processInstances.stream().map(HistoricProcessInstance::getId).collect(Collectors.toList());
        historyService.deleteHistoricProcessInstances(processInstanceIds);
    }

    //list of active Tasks
    public List<TaskDto> getActiveTasks() {
        return taskService
                .createTaskQuery()
                .active()
                .list()
                .stream()
                .map(TaskDto::of)
                .collect(toList());
    }

    //claiming a task
    public void claimTask(String taskId) {
        String userId = taskService.createTaskQuery().taskId(taskId).singleResult().getAssignee();
        taskService.claim(taskId, userId);
    }

    //complete a task
    public void completeTask(String taskId, Map<String, Object> variables) {
        taskService.complete(taskId, variables);
    }

    //getting service tasks
    public List<ServiceTaskDto> getServiceTaskDefinitions(String processDefinitionId) {
        List<ServiceTaskDto> serviceTaskDefinitions = new ArrayList<>();

        BpmnModelInstance bpmnModelInstance = repositoryService.getBpmnModelInstance(processDefinitionId);

        Collection<ServiceTask> serviceTasks = bpmnModelInstance.getModelElementsByType(ServiceTask.class);
        for (ServiceTask serviceTask : serviceTasks) {

            ServiceTaskDto serviceTaskDto = new ServiceTaskDto(
                    serviceTask.getId(),
                    serviceTask.getName(),
                    serviceTask.getCamundaTopic(),
                    serviceTask.getCamundaExpression(),
                    serviceTask.getCamundaDelegateExpression(),
                    serviceTask.getCamundaTaskPriority());

            serviceTaskDefinitions.add(serviceTaskDto);
        }
        return serviceTaskDefinitions;
    }

    //getting user tasks
    public List<UserTaskDto> getUserTaskDefinitions(String processDefinitionId) {
        List<UserTaskDto> userTaskDefinitions = new ArrayList<>();

        BpmnModelInstance bpmnModelInstance = repositoryService.getBpmnModelInstance(processDefinitionId);

        Collection<UserTask> userTasks = bpmnModelInstance.getModelElementsByType(UserTask.class);
        for (UserTask userTask : userTasks) {

            UserTaskDto userTaskDto = new UserTaskDto(
                    userTask.getId(),
                    userTask.getName(),
                    userTask.getCamundaAssignee(),
                    userTask.getCamundaCandidateGroupsList(),
                    userTask.getCamundaCandidateUsersList(),
                    userTask.getCamundaFormKey(),
                    userTask.getCamundaPriority(),
                    userTask.getCamundaDueDate() != null,
                    userTask.getCamundaDueDate(),
                    userTask.getCamundaFollowUpDate() != null,
                    userTask.getCamundaFollowUpDate());

            userTaskDefinitions.add(userTaskDto);
        }
        return userTaskDefinitions;
    }

    //delete all tasks
    public void deleteAllTasks() {
        List<Task> tasks = taskService.createTaskQuery().list();
        for (Task task : tasks) {
            taskService.deleteTask(task.getId(), true);
        }
    }
    //get completed tasks historic
    public List<HistoricTaskInstance> getCompletedTasks() {
        return historyService.createHistoricTaskInstanceQuery().finished().list();
    }

    //save a workflow
    public Workflow saveWorkflow(Workflow workflow) {
        return workflowRepository.save(workflow);
    }

    //get a workflow Object to treat it in front
    public Optional<Workflow> getBpmnModelInstance(String workflowId) {
        Optional<Workflow> bpmn = workflowRepository.findById(workflowId);
        if (bpmn == null) {
            throw new IllegalArgumentException("Workflow not found for id: " + workflowId);
        }
        return bpmn;
    }

    //save a workflow and deploy it by Camunda engine
    public Workflow saveWorkflowBpmn(Workflow workflow) throws IOException {

        System.out.println(workflow.getXmlContent());
        // Deploy process definition
        Deployment deployment = repositoryService.createDeployment()
                .name(workflow.getName())
                .addString(workflow.getXmlName(), workflow.getXmlContent())
                .deploy();
        // Get process definition ID
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();


        // setting workflow deploymentId
        workflow.setDeploymentId(deployment.getId());
        //save xmlContent into an XML file

        // Save workflow.xmlContent to an XML file in the specified path
        String fileName = workflow.getXmlName() + ".xml";
        File xmlFile = new File("src/main/resources/static/bpmn", fileName);
        FileWriter writer = new FileWriter(xmlFile);
        writer.write(workflow.getXmlContent());
        writer.close();

        // Save workflow entity in database
        return workflowRepository.save(workflow);
    }

}
