package com.example.workflow.dto;

import lombok.Value;
import org.camunda.bpm.engine.task.Task;

import java.util.Date;


@Value
public class TaskDto {

    String id;
    String name;
    String assignee;
    Date   createdTime;
    String processInstanceId;
    String taskDefinitionKey;
    String processDefinitionId;

    public static TaskDto of (Task task) {
        return new TaskDto(
                task.getId(),
                task.getName(),
                task.getAssignee(),
                task.getCreateTime(),
                task.getProcessInstanceId(),
                task.getTaskDefinitionKey(),
                task.getProcessDefinitionId()
        );
    }
}
