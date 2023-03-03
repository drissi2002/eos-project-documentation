package com.example.workflow.dto;

import lombok.Value;
import org.camunda.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.camunda.bpm.engine.repository.ProcessDefinition;

@Value
public class ProcessDefinitionDto {

    String id;
    String name;
    String key;
    String category;
    String tenantId;
    int version;

    public static ProcessDefinitionDto of(ProcessDefinition processDefinition) {
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) processDefinition;
        return new ProcessDefinitionDto(
                processDefinitionEntity.getId(),
                processDefinitionEntity.getName(),
                processDefinitionEntity.getKey(),
                processDefinitionEntity.getCategory(),
                processDefinitionEntity.getTenantId(),
                processDefinitionEntity.getVersion()
        );
    }
}
