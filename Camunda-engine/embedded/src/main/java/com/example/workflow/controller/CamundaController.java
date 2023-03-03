package com.example.workflow.controller;

import com.example.workflow.dto.*;
import com.example.workflow.entity.Workflow;
import com.example.workflow.service.CamundaService;

import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;

import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/camunda")
@CrossOrigin(origins = "http://localhost:4200")
class CamundaController {

    @Autowired
    private CamundaService camundaService;

    @GetMapping("/process-definitions")
    public List<ProcessDefinitionDto> getProcessDefinitions() {
        return camundaService.getDeployedProcessDefinitions();
    }

    @PostMapping("/processes/{processKey}/start")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void startProcess(@PathVariable String processKey){
        camundaService.startProcess(processKey);
    }


    @GetMapping("/tasks/active")
    public List<TaskDto> getProcessInstances(){
        return camundaService.getActiveTasks();
    }


    @DeleteMapping("/tasks")
    public ResponseEntity<String> deleteAllTasks() {
        try {
            camundaService.deleteAllTasks();
            return ResponseEntity.ok("All tasks deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/tasks/{taskId}/claim")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void claimTask(@PathVariable String taskId) {
        camundaService.claimTask(taskId);
    }


    @PostMapping("/{taskId}/complete")
    public void completeTask(@PathVariable String taskId, @RequestBody Map<String, Object> variables) {
        camundaService.completeTask(taskId, variables);
    }

    @DeleteMapping("/process-instances")
    public ResponseEntity<String> deleteAllProcessInstances() {
        try {
            camundaService.deleteAllProcessInstances();
            return ResponseEntity.ok("All process instances deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/process-definitions")
    public ResponseEntity<String> deleteAllProcessDefinitions() {
        try {
            camundaService.deleteAllProcessDefinitions();
            return ResponseEntity.ok("All process definitions and their versions deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/process-definitions/{id}")
    public ResponseEntity<String> deleteProcessDefinition(@PathVariable("id") String processDefinitionId) {
        try {
            camundaService.deleteProcessDefinition(processDefinitionId);
            return ResponseEntity.ok("Process definition with id: " + processDefinitionId + " and all its versions are deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/deploy")
    public ResponseEntity<String> deployProcessDefinition(@RequestParam("processDefinitionName") String processDefinitionName,
                                                          @RequestParam("processDefinitionBpmnFile") String processDefinitionBpmnFile) {
        try {
            camundaService.deployProcessDefinition(processDefinitionName, processDefinitionBpmnFile);
            return ResponseEntity.ok("Process definition deployed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("history/tasks")
    public List<HistoricTaskInstance> getCompletedTasks() {
        return camundaService.getCompletedTasks();
    }

    @GetMapping("history/processes")
    public List<HistoricProcessInstance> getCompletedProcesses() {
        return camundaService.getCompletedProcesses();
    }

    @DeleteMapping("/historic-process-instances")
    public void deleteAllHistoricProcessInstances() {
        camundaService.deleteAllHistoricProcessInstances();
    }

    @PostMapping("/save/workflow")
    public Workflow saveWorkflow(@RequestBody Workflow workflow) {
        return camundaService.saveWorkflow(workflow);
    }

    @GetMapping("/workflow/bpmn/{id}")
    public ResponseEntity<Optional<Workflow>> getWorkflowXmlContent(@PathVariable String id) {
        Optional<Workflow> bpmn = camundaService.getBpmnModelInstance(id);
        return ResponseEntity.ok(bpmn);
    }

    @PostMapping("/save/workflow/bpmn")
    public ResponseEntity<Workflow> createWorkflow(@RequestBody Workflow workflow) throws IOException, JAXBException {
        Workflow savedWorkflowBpmn = camundaService.saveWorkflowBpmn(workflow);
        return ResponseEntity.created(URI.create("/workflows/" + savedWorkflowBpmn.getId())).body(savedWorkflowBpmn);
    }


    @GetMapping("/process/{processDefinitionId}/service-tasks")
    public ResponseEntity<List<ServiceTaskDto>> getServiceTasks(@PathVariable String processDefinitionId) {
        List<ServiceTaskDto> serviceTasks = camundaService.getServiceTaskDefinitions(processDefinitionId);
        return ResponseEntity.ok(serviceTasks);
    }

    @GetMapping("/process/{processDefinitionId}/user-tasks")
    public ResponseEntity<List<UserTaskDto>> getUserTasks(@PathVariable String processDefinitionId) {
        List<UserTaskDto> userTasks = camundaService.getUserTaskDefinitions(processDefinitionId);
        return ResponseEntity.ok(userTasks);
    }


}
