package com.example.workflow.dto;

import lombok.Value;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import java.util.List;

@Value
public class UserTaskDto {
    private String id;
    private String name;
    private String assignee;
    private List<String> candidateGroups;
    private List<String> candidateUsers;
    private String formKey;
    private String priority;
    private boolean isDueDateSet;
    private String dueDate;
    private boolean isFollowUpDateSet;
    private String followUpDate;

    public static UserTaskDto of(UserTask userTask) {
        return new UserTaskDto (
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
    }
}
