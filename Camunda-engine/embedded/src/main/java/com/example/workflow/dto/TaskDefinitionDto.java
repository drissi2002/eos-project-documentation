package com.example.workflow.dto;

import lombok.Value;
import org.camunda.bpm.engine.impl.task.TaskDefinition;

@Value
public class TaskDefinitionDto {
    String key;
    String name;
    String description;
    String assignee;
    String priority;
    String dueDate;
    String followUpDate;
    String formKey;

    public static TaskDefinitionDto of(TaskDefinition taskDefinition) {
        return new TaskDefinitionDto(
                taskDefinition.getKey(),
                taskDefinition.getNameExpression().getExpressionText(),
                taskDefinition.getDescriptionExpression().getExpressionText(),
                taskDefinition.getAssigneeExpression().getExpressionText(),
                taskDefinition.getPriorityExpression().getExpressionText(),
                taskDefinition.getDueDateExpression().getExpressionText(),
                taskDefinition.getFollowUpDateExpression().getExpressionText(),
                taskDefinition.getFormKey().getExpressionText()
        );
    }
}