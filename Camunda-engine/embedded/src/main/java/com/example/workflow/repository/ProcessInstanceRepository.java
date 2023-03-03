package com.example.workflow.repository;

import com.example.workflow.entity.ProcessInstance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessInstanceRepository  extends JpaRepository<ProcessInstance,String> {
}
