package com.example.workflow.repository;

import com.example.workflow.entity.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkflowRepository  extends JpaRepository<Workflow,String> {
    //String findXmlContentById(String id);
}
