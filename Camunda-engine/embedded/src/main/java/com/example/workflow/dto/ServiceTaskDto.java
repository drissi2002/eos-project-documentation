package com.example.workflow.dto;

import lombok.Value;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;

@Value
public class ServiceTaskDto {
    private String id;
    private String name;
    private String topic;
    private String expression;
    private String delegateExpression;
    private String taskPriority;

    // Constructors, getters and setters

    public static ServiceTaskDto of(ServiceTask serviceTask) {
        return new ServiceTaskDto(
                serviceTask.getId(),
                serviceTask.getName(),
                serviceTask.getCamundaTopic(),
                serviceTask.getCamundaExpression(),
                serviceTask.getCamundaDelegateExpression(),
                serviceTask.getCamundaTaskPriority());
    }
}
